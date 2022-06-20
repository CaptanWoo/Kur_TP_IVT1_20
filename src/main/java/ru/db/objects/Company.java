package ru.db.objects;

import lombok.EqualsAndHashCode;
import ru.Server;
import ru.annotations.Column;
import ru.db.DataBaseTable;
import lombok.Data;
import ru.utils.Utils;
import ru.utils.enums.EnumStatus;
import ru.utils.objects.Address;
import ru.utils.objects.ContactInfo;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class Company extends DBObject {

    @Column(name = "id") private long id;
    @Column(name = "status") private EnumStatus status = EnumStatus.NOT_STATED;
    @Column(name = "creatorId") private long creatorId;
    @Column(name = "companyTypeId") private long companyTypeId;
    @Column(name = "displayName") private String displayName;
    @Column(name = "description") private String description;
    @Column(name = "website") private String website;
    @Column(name = "address") private final Address address = new Address();
    @Column(name = "contactInfo") private ContactInfo contactInfo = new ContactInfo();
    @Column(name = "professionsId") private long[] professionsId = new long[0];
    @Column(name = "usersId") private long[] usersId = new long[0];
    @Column(name = "vacanciesId") private long[] vacanciesId = new long[0];
    @Column(name = "creationDate") private LocalDateTime creationDate = LocalDateTime.now();
    @Column(name = "updateDate") private LocalDateTime updateDate = LocalDateTime.now();

    public Company() {
    }

    public void addUserId(long id) {
        usersId = Utils.addLongToArray(usersId, id);
    }
    public void removeUserId(long id) {
        usersId = Utils.removeLongFromArray(usersId, id);
    }
    public void addVacancyId(long id) {
        vacanciesId = Utils.addLongToArray(vacanciesId, id);
    }
    public void removeVacancyId(long id) {
        vacanciesId = Utils.removeLongFromArray(vacanciesId, id);
    }

    public void setStatus(String name) {
        status = EnumStatus.getEnum(name);
    }

    public void setAddress(String text) {
        address.load(text);
    }
    public void setContactInfo(String text) {
        contactInfo.load(text);
    }

    public void setCreationDate(String text) {
        creationDate = LocalDateTime.parse(text);
    }
    public void setUpdateDate(String text) {
        updateDate = LocalDateTime.parse(text);
    }

    public void setProfessionsId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        professionsId = new long[data.length];
        for (int i = 0; i < professionsId.length; i++) {
            if (data[i].equals("")) continue;
            professionsId[i] = Long.parseLong(data[i]);
        }
    }

    public void setUsersId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        usersId = new long[data.length];
        for (int i = 0; i < usersId.length; i++) {
            if (data[i].equals("")) continue;
            usersId[i] = Long.parseLong(data[i]);
        }
    }

    public void setVacanciesId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        vacanciesId = new long[data.length];
        for (int i = 0; i < vacanciesId.length; i++) {
            if (data[i].equals("")) continue;
            vacanciesId[i] = Long.parseLong(data[i]);
        }
    }


    public String getStatus() {
        return status.getUnlocalizedName();
    }
    public EnumStatus getStatusEnum() {
        return status;
    }

    public String getCompanyType() {
        return Server.COMPANY_TYPES_TABLE.getNameById(companyTypeId);
    }

    public String getProfessionsId() {
        StringBuilder sb = new StringBuilder();
        for (long professionId: professionsId) {
            sb.append(professionId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public String getProfessions() {
        StringBuilder sb = new StringBuilder();
        for (long id: professionsId) {
            sb.append(", ").append(Server.PROFESSION_TABLE.getNameById(id));
        }
        return sb.substring(2);
    }

    public String getUsersId() {
        StringBuilder sb = new StringBuilder();
        for (long userId: usersId) {
            sb.append(userId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }
    public String getVacanciesId() {
        StringBuilder sb = new StringBuilder();
        for (long vacancyId: vacanciesId) {
            sb.append(vacancyId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }
    public int getVacanciesCount() {
        return vacanciesId.length;
    }

    public String getAddress() {
        return address.save();
    }

    public String getDisplayAddress() {
        return address.getDisplayAddress();
    }
    public Address getAddressObject() {
        return address;
    }

    public String getContactInfo() {
        return contactInfo.save();
    }
    public ContactInfo getContactInfoObject() {
        return contactInfo;
    }

    public String getCreationDate() {
        return creationDate.toString();
    }
    public String getCreationDisplayDate() {
        return creationDate.toLocalDate().toString();
    }
    public String getUpdateDate() {
        return updateDate.toString();
    }
    public String getUpdateDisplayDate() {
        return updateDate.toLocalDate().toString();
    }

    public static String getHeader() {
        return getFields(Company.class);
    }
}
