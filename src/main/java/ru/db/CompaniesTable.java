package ru.db;

import ru.Server;
import ru.db.objects.Company;
import ru.db.objects.User;
import ru.dto.company.CompanyCreateDTO;
import ru.dto.company.CompanyInfoDTO;
import ru.dto.company.CompanyPreviewDTO;
import ru.dto.company.CompanySearchDTO;
import ru.dto.objects.AddressDTO;
import ru.dto.objects.ContactInfoDTO;
import ru.dto.vacancy.VacancyPreviewDTO;
import ru.dto.vacancy.VacancySearchDTO;
import ru.objects.AuthData;
import ru.objects.Page;
import ru.sio.CompanySIO;
import ru.sio.VacancySIO;
import ru.utils.enums.EnumDataPaths;
import ru.utils.enums.EnumStatus;
import ru.utils.interfaces.ICheckCompany;
import ru.utils.interfaces.ICheckVacancy;
import ru.utils.objects.Address;
import ru.utils.objects.ContactInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompaniesTable extends DataBaseTable {

    public CompaniesTable() {
        //При создании базы данных передаём родительскому классу путь до файла таблицы и заголовок с названиями столбцов.
        //Названия столбцов берём через метод getHeader у класса от аннотаций @Column, которые висят над переменными в классе.
        super(EnumDataPaths.COMPANIES_TABLE, Company.getHeader());
        //Отправляем заголовок таблицы в метод, который проверяет, нужно ли пересобрать таблицу
        rebase(Company.getHeader());
    }

    public void addCompany(Company company) {
        appendRecord(data -> {
            if (data != null) {
                Company lastCompany = Server.cm.loadObject(data, Company.class);
                company.setId(lastCompany.getId()+1);
            } else company.setId(1);
            return Server.cm.saveObject(company);
        });
        Server.USERS_TABLE.updateUserAddCompanyId(company.getCreatorId(), company.getId());
    }

    protected void updateCompanyAddVacancyId(long id, long vacancyId) {
        rewrite((reader, writer) -> {
            Company company;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                company = Server.cm.loadObject(line, Company.class);
                if (company.getId() == id) company.addVacancyId(vacancyId);
                writer.write(Server.cm.saveObject(company) + '\n');
            }
        });
    }
    protected void updateCompanyRemoveVacancyId(long id, long vacancyId) {
        rewrite((reader, writer) -> {
            Company company;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                company = Server.cm.loadObject(line, Company.class);
                if (company.getId() == id) company.removeVacancyId(vacancyId);
                writer.write(Server.cm.saveObject(company) + '\n');
            }
        });
    }

    protected Company getCompany(long id) {
        String line = getRecord(data -> {
            Company company = Server.cm.loadObject(data, Company.class);
            return company.getId() == id;
        });

        if (line != null) return Server.cm.loadObject(line, Company.class);
        else return null;
    }

    public AddressDTO getCompanyAddressDTO(AuthData authData, long companyId) {
        if (companyId == 0) return null;

        User user = Server.USERS_TABLE.getUser(authData);
        if (user == null) return null;

        Company company = Server.COMPANIES_TABLE.getCompany(companyId);
        if (company == null) return null;

        Address address = company.getAddressObject();
        return new AddressDTO(address.getCountry(), address.getRegion(), address.getCity(), address.getStreet(), address.getHouse());
    }

    public ContactInfoDTO getCompanyContactInfoDTO(AuthData authData, long companyId) {
        if (companyId == 0) return null;

        User user = Server.USERS_TABLE.getUser(authData);
        if (user == null) return null;

        Company company = Server.COMPANIES_TABLE.getCompany(companyId);
        if (company == null) return null;

        ContactInfo contactInfo = company.getContactInfoObject();
        return new ContactInfoDTO(contactInfo.getFio(), contactInfo.getEmail(), contactInfo.getPhoneNumber(), contactInfo.getDescription());
    }

    public CompanyInfoDTO getCompanyInfoDTO(AuthData authData, long companyId) {
        if (companyId == 0) return null;

        User user = Server.USERS_TABLE.getUser(authData);
        if (user == null) return null;

        Company company = Server.COMPANIES_TABLE.getCompany(companyId);
        if (company == null) return null;

        return new CompanyInfoDTO(
                company.getId(),
                company.getVacanciesCount(),
                company.getDisplayName(),
                company.getCompanyType(),
                company.getWebsite(),
                company.getDisplayAddress(),
                company.getContactInfo(),
                company.getDescription(),
                company.getProfessions()
        );
    }

    public void createCompany(AuthData authData, CompanyCreateDTO dto) {
        User user = Server.USERS_TABLE.getUser(authData);

        if (user == null) return;

        Company company = new Company();
        company.setStatus(EnumStatus.ACTIVE.getUnlocalizedName());
        company.setCreatorId(authData.getId());
        company.setCompanyTypeId(Server.COMPANY_TYPES_TABLE.getIdByName(dto.getCompanyType()));
        company.setDisplayName(dto.getDisplayName());
        company.setDescription(dto.getDescription());
        company.setWebsite(dto.getWebsite());
        company.setAddress(dto.getAddress());
        company.setContactInfo(dto.getContactInfo());

        long id;
        StringBuilder sb = new StringBuilder();
        Set<Long> idHashSet = new HashSet<>();
        for (String name: dto.getProfessions().split(DataBaseTable.objectArraySeparator)) {
            id = Server.PROFESSION_TABLE.getIdByName(name);
            if (id > 0) idHashSet.add(id);
        }
        assembleIds(idHashSet, sb);
        company.setProfessionsId(sb.toString());

        addCompany(company);
    }

    protected String getCompaniesDisplayNames(String sids) {
        long id;
        List<Long> ids = new ArrayList<>();
        for (String s1: sids.split(DataBaseTable.numberArraySeparator)) {
            id = Long.parseLong(s1);
            if (id > 0) ids.add(id);
        }

        if (ids.size() == 0) return null;

        StringBuilder sb = new StringBuilder();
        readRecord(line -> {
            if (line != null) {
                Company company = Server.cm.loadObject(line, Company.class);
                if (company != null && ids.contains(company.getId())) {
                    sb.append(company.getDisplayName()).append(DataBaseTable.objectArraySeparator);
                }
            }
        });

        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public Page<CompanyPreviewDTO> getVacancyPage(CompanySearchDTO dto, int pageSize) {
        CompanySIO companySIO = new CompanySIO(dto);
        return getPage(companySIO, pageSize);
    }

    public Page<CompanyPreviewDTO> getAllVacancyPage(int pageSize) {
        ICheckCompany iCheckCompany = (company) -> company.getStatusEnum() == EnumStatus.ACTIVE;
        return getPage(iCheckCompany, pageSize);
    }

    public Page<CompanyPreviewDTO> getUserVacancyPage(AuthData authData, int pageSize) {
        User tempUser = Server.USERS_TABLE.getUser(authData);
        if (tempUser == null) return null;

        ICheckCompany iCheckCompany = (company) -> company.getStatusEnum() == EnumStatus.ACTIVE && company.getCreatorId() == tempUser.getId();
        return getPage(iCheckCompany, pageSize);
    }

    public Page<CompanyPreviewDTO> getPage(ICheckCompany iCheckCompany, int pageSize) {
        Page<CompanyPreviewDTO> page = new Page<>(pageSize);
        readRecord(data -> {
            if (data != null) {
                Company company = Server.cm.loadObject(data, Company.class);

                if (iCheckCompany.checkCompany(company)) {
                    page.add(new CompanyPreviewDTO(
                            company.getId(),
                            company.getDisplayName(),
                            company.getCompanyType(),
                            company.getProfessions(),
                            company.getVacanciesCount(),
                            company.getWebsite(),
                            company.getDisplayAddress(),
                            company.getUpdateDisplayDate(),
                            company.getCreationDisplayDate()
                    ));
                }
            }
        });
        return page;
    }

}
