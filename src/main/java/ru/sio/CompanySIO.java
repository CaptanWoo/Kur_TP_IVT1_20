package ru.sio;

import ru.Server;
import ru.db.DataBaseTable;
import ru.db.objects.Company;
import ru.db.objects.Resume;
import ru.dto.company.CompanySearchDTO;
import ru.utils.interfaces.ICheckCompany;
import ru.utils.objects.Address;

import java.util.HashSet;
import java.util.Set;

public class CompanySIO implements ICheckCompany {

    private final String[] name;
    private final Address address;
    private final Set<Long> professionsIdSet = new HashSet<>();
    private final Set<Long> companyTypesIdSet = new HashSet<>();
    private final boolean professionsAll;

    public CompanySIO(CompanySearchDTO dto) {
        this.name = dto.getName().split(" ");
        this.address = new Address(dto.getAddress());
        long id;
        for (String name: dto.getProfessions().split(DataBaseTable.objectArraySeparator)) {
            id = Server.PROFESSION_TABLE.getIdByName(name);
            if (id > 0) this.professionsIdSet.add(id);
        }
        for (String name: dto.getCompanyTypes().split(DataBaseTable.objectArraySeparator)) {
            id = Server.COMPANY_TYPES_TABLE.getIdByName(name);
            if (id > 0) this.companyTypesIdSet.add(id);
        }
        this.professionsAll = dto.isProfessionsAll();
    }

    @Override
    public boolean checkCompany(Company company) {

        long id;
        Set<Long> professionsId = new HashSet<>();
        for (String sid: company.getProfessionsId().split(DataBaseTable.numberArraySeparator)) {
            id = Long.parseLong(sid);
            if (id > 0) professionsId.add(id);
        }

        boolean nameFlag = true,
                addressFlag = true,
                professionsFlag = professionsAll ? hasAll(professionsIdSet, professionsId) : hasAny(professionsIdSet, professionsId),
                companyTypesFlag = companyTypesIdSet.size() == 0 || companyTypesIdSet.contains(company.getCompanyTypeId());

        String[] companyName = company.getDisplayName().split(" ");
        for (String s: name) {
            if (!s.equals("")) nameFlag = nameFlag & hasAny(companyName, s);
        }

        String country = address.getCountry();
        String region = address.getRegion();
        String city = address.getCity();

        Address companyAddress = company.getAddressObject();

        if (!country.equals("")) addressFlag = country.equals(companyAddress.getCountry());
        if (!region.equals("")) addressFlag = addressFlag & region.equals(companyAddress.getRegion());
        if (!city.equals("")) addressFlag = addressFlag & city.equals(companyAddress.getCity());

        return nameFlag && addressFlag && professionsFlag && companyTypesFlag;
    }

    private boolean hasAny(String[] strings, String data) {
        if (data.length() == 0) return true;
        for (String s: strings) {
            if (s.equals(data)) return true;
        }
        return false;
    }

    private boolean hasAny(Set<Long> set, Set<Long> ids) {
        if (set.size() == 0) return true;
        for (long id: ids) {
            if (set.contains(id)) return true;
        }
        return false;
    }

    private boolean hasAll(Set<Long> set, Set<Long> ids) {
        if (set.size() == 0) return true;
        for (long id: ids) {
            if (!set.contains(id)) return false;
        }
        return true;
    }
}
