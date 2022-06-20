package ru.db;

import ru.Server;
import ru.db.objects.Company;
import ru.db.objects.User;
import ru.db.objects.Vacancy;
import ru.dto.vacancy.VacancyCreateDTO;
import ru.dto.vacancy.VacancyInfoDTO;
import ru.dto.vacancy.VacancyPreviewDTO;
import ru.dto.vacancy.VacancySearchDTO;
import ru.gui.elements.GuiDateObject;
import ru.objects.AuthData;
import ru.objects.Page;
import ru.sio.VacancySIO;
import ru.utils.enums.*;
import ru.utils.interfaces.ICheckVacancy;
import ru.utils.objects.ContactInfo;

import java.time.Period;
import java.util.HashSet;
import java.util.Set;

public class VacanciesTable extends DataBaseTable {

    public VacanciesTable() {
        //При создании базы данных передаём родительскому классу путь до файла таблицы и заголовок с названиями столбцов.
        //Названия столбцов берём через метод getHeader у класса от аннотаций @Column, которые висят над переменными в классе.
        super(EnumDataPaths.VACANCIES_TABLE, Vacancy.getHeader());
        //Отправляем заголовок таблицы в метод, который проверяет, нужно ли пересобрать таблицу
        rebase(Vacancy.getHeader());
    }

    public void addVacancy(Vacancy vacancy) {
        appendRecord(data -> {
            if (data != null) {
                Vacancy lastVacancy = Server.cm.loadObject(data, Vacancy.class);
                vacancy.setId(lastVacancy.getId()+1);
            } else vacancy.setId(1);
            return Server.cm.saveObject(vacancy);
        });
        Server.USERS_TABLE.updateUserAddVacancyId(vacancy.getCreatorId(), vacancy.getId());
        Server.COMPANIES_TABLE.updateCompanyAddVacancyId(vacancy.getCreatorId(), vacancy.getId());
    }

    public Page<VacancyPreviewDTO> getVacancyPage(VacancySearchDTO dto, int pageSize) {
        VacancySIO vacancySIO = new VacancySIO(dto);
        return getPage(vacancySIO, pageSize);
    }

    public Page<VacancyPreviewDTO> getAllVacancyPage(int pageSize) {
        ICheckVacancy iCheckVacancy = (resume, user) -> resume.getStatusEnum() == EnumStatus.ACTIVE;
        return getPage(iCheckVacancy, pageSize);
    }

    public Page<VacancyPreviewDTO> getUserVacancyPage(AuthData authData, int pageSize) {
        User tempUser = Server.USERS_TABLE.getUser(authData);
        if (tempUser == null) return null;

        ICheckVacancy iCheckVacancy = (resume, user) -> resume.getStatusEnum() == EnumStatus.ACTIVE && user.getId() == tempUser.getId();
        return getPage(iCheckVacancy, pageSize);
    }

    public Page<VacancyPreviewDTO> getPage(ICheckVacancy iCheckVacancy, int pageSize) {
        Page<VacancyPreviewDTO> page = new Page<>(pageSize);
        readRecord(data -> {
            if (data != null) {
                Vacancy vacancy = Server.cm.loadObject(data, Vacancy.class);
                User user = Server.USERS_TABLE.getUser(vacancy.getCreatorId());

                String salary = formatSalary(
                        vacancy.getMinSalary(),
                        vacancy.getMaxSalary()
                );

                String workPeriod = formatWorkPeriod(
                        vacancy.getMinWorkPeriodObject().getYears(),
                        vacancy.getMaxWorkPeriodObject().getYears(),
                        vacancy.getMinWorkPeriodObject(),
                        vacancy.getMaxWorkPeriodObject()
                );

                String company = vacancy.getCompanyDisplayName();
                if (company == null) company = "не указано";

                if (iCheckVacancy.checkVacancy(vacancy, user)) {
                    page.add(new VacancyPreviewDTO(
                            vacancy.getId(),
                            vacancy.getDisplayName(),
                            salary,
                            workPeriod,
                            vacancy.getSchedule(),
                            vacancy.getEmploymentType(),
                            vacancy.getProfessions(),
                            company,
                            vacancy.getDisplayAddress(),
                            vacancy.getCitizenShips(),
                            vacancy.getLanguages(),
                            vacancy.getUpdateDisplayDate(),
                            vacancy.getCreationDisplayDate(),
                            vacancy.getShortDescription()
                    ));
                }
            }
        });
        return page;
    }

    public void createVacancy(User user, VacancyCreateDTO dto) {
        if (user == null) return;

        Vacancy vacancy = new Vacancy();
        vacancy.setStatus(EnumStatus.ACTIVE.getUnlocalizedName());
        vacancy.setCreatorId(user.getId());
        vacancy.setCompanyId(dto.getCompanyId());
        vacancy.setDisplayName(dto.getDisplayName());
        vacancy.setEmploymentType(dto.getEmploymentType());
        vacancy.setSchedule(dto.getSchedule());
        vacancy.setMinSalary(dto.getMinSalary());
        vacancy.setMaxSalary(dto.getMaxSalary());
        vacancy.setMinWorkPeriodOfYears(dto.getMinWorkPeriod());
        vacancy.setMaxWorkPeriodOfYears(dto.getMaxWorkPeriod());
        vacancy.setAddress(dto.getAddress());
        vacancy.setContactInfo(dto.getContactInfo());
        vacancy.setDescription(dto.getDescription());
        vacancy.setShortDescription(dto.getShortDescription());

        long id;
        Set<Long> idHashSet = new HashSet<>();
        StringBuilder sb = new StringBuilder();

        for (String name: dto.getProfessions().split(DataBaseTable.objectArraySeparator)) {
            id = Server.PROFESSION_TABLE.getIdByName(name);
            if (id > 0) idHashSet.add(id);
        }
        vacancy.setProfessionsId(assembleIds(idHashSet, sb));

        for (String name: dto.getSkills().split(DataBaseTable.objectArraySeparator)) {
            id = Server.SKILLS_TABLE.getIdByName(name);
            if (id > 0) idHashSet.add(id);
        }
        vacancy.setSkillsId(assembleIds(idHashSet, sb));

        for (String name: dto.getLanguages().split(DataBaseTable.objectArraySeparator)) {
            id = Server.LANGUAGES_TABLE.getIdByName(name);
            if (id > 0) idHashSet.add(id);
        }
        vacancy.setLanguagesId(assembleIds(idHashSet, sb));

        for (String name: dto.getCitizenShips().split(DataBaseTable.objectArraySeparator)) {
            id = Server.CITIZEN_SHIPS_TABLE.getIdByName(name);
            if (id > 0) idHashSet.add(id);
        }
        vacancy.setCitizenShipsId(assembleIds(idHashSet, sb));

        addVacancy(vacancy);
    }

    public void createVacancy(AuthData authData, VacancyCreateDTO dto) {
        createVacancy(Server.USERS_TABLE.getUser(authData), dto);
    }

    public VacancyInfoDTO getVacancyInfo(AuthData authData, long vacancyId) {

        User user = Server.USERS_TABLE.getUser(authData);
        if (user == null) return null;

        Vacancy vacancy = Server.cm.loadObject(getRecord(data -> {
            if (data != null) {
                return Server.cm.loadObject(data, Vacancy.class).getId() == vacancyId;
            }
            return false;
        }), Vacancy.class);

        if (vacancy == null) return null;

        Company company = Server.COMPANIES_TABLE.getCompany(vacancy.getCompanyId());

        String companyName;
        if (company == null) companyName = "не указано";
        else companyName = company.getDisplayName();

        String salary = formatSalary(
                vacancy.getMinSalary(),
                vacancy.getMaxSalary()
        );

        String workPeriod = formatWorkPeriod(
                vacancy.getMinWorkPeriodObject().getYears(),
                vacancy.getMaxWorkPeriodObject().getYears(),
                vacancy.getMinWorkPeriodObject(),
                vacancy.getMaxWorkPeriodObject()
        );

        ContactInfo contactInfo = vacancy.getContactInfoObject();

        return new VacancyInfoDTO(
                vacancy.getDisplayName(),
                companyName,
                salary,
                workPeriod,
                vacancy.getSchedule(),
                vacancy.getEmploymentType(),
                contactInfo.getFio(),
                contactInfo.getEmail(),
                contactInfo.getPhoneNumber(),
                contactInfo.getDescription(),
                vacancy.getDisplayAddress(),
                vacancy.getDescription(),
                vacancy.getProfessions(),
                vacancy.getSkills(),
                vacancy.getCitizenShips(),
                vacancy.getLanguages()
        );
    }

    private String formatSalary(int min, int max) {
        String salary;
        if (min == 0 && max == 0) salary = "не указано";
        else if (min > 0 && max == 0) salary = "от " + min + " руб.";
        else if (min == 0 && max > 0) salary = "до " + max + " руб.";
        else salary = min + " - " + max + " руб.";

        return salary;
    }

    private String formatWorkPeriod(int min, int max, Period minPeriod, Period maxPeriod) {
        String workPeriod;
        if (min == 0 && max == 0) workPeriod = "не указано";
        else if (min > 0 && max == 0) workPeriod = "от " + GuiDateObject.formatDisplayDate(minPeriod, true);
        else if (min == 0 && max > 0) workPeriod = "до " + GuiDateObject.formatDisplayDate(maxPeriod, true);
        else workPeriod = "от " + GuiDateObject.formatDisplayDate(minPeriod, true) +
                    "до " + GuiDateObject.formatDisplayDate(maxPeriod, true);
        return workPeriod;
    }

}
