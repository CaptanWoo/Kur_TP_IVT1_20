package ru.db.objects;

import lombok.EqualsAndHashCode;
import ru.Server;
import ru.annotations.Column;
import lombok.Data;
import ru.db.DataBaseTable;
import ru.utils.Utils;
import ru.utils.enums.EnumEmploymentType;
import ru.utils.enums.EnumSchedule;
import ru.utils.enums.EnumStatus;
import ru.utils.objects.Address;
import ru.utils.objects.ContactInfo;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class Vacancy extends DBObject {

    @Column(name = "id") private long id;
    @Column(name = "status") private EnumStatus status = EnumStatus.NOT_STATED;
    @Column(name = "creatorId") private long creatorId;
    @Column(name = "companyId") private long companyId;
    @Column(name = "displayName") private String displayName;
    @Column(name = "employmentType") private EnumEmploymentType employmentType = EnumEmploymentType.NOT_STATED;
    @Column(name = "schedule") private EnumSchedule schedule = EnumSchedule.NOT_STATED;
    @Column(name = "minSalary") private int minSalary;
    @Column(name = "maxSalary") private int maxSalary;
    @Column(name = "minWorkPeriod") private Period minWorkPeriod = Period.ZERO;
    @Column(name = "maxWorkPeriod") private Period maxWorkPeriod = Period.ZERO;
    @Column(name = "address") private final Address address = new Address();
    @Column(name = "contactInfo") private ContactInfo contactInfo = new ContactInfo();
    @Column(name = "description") private String description;
    @Column(name = "shortDescription") private String shortDescription;
    @Column(name = "professionsId") private long[] professionsId = new long[0];
    @Column(name = "skillsId") private long[] skillsId = new long[0];
    @Column(name = "languagesId") private long[] languagesId = new long[0];
    @Column(name = "citizenShipsId") private long[] citizenShipsId = new long[0];
    @Column(name = "creationDate") private LocalDateTime creationDate = LocalDateTime.now();
    @Column(name = "updateDate") private LocalDateTime updateDate = LocalDateTime.now();

    public Vacancy() {
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

    public void setProfessionsId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        professionsId = new long[data.length];
        for (int i = 0; i < professionsId.length; i++) {
            if (data[i].equals("")) continue;
            professionsId[i] = Long.parseLong(data[i]);
        }
    }
    public void setSkillsId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        skillsId = new long[data.length];
        for (int i = 0; i < skillsId.length; i++) {
            if (data[i].equals("")) continue;
            skillsId[i] = Long.parseLong(data[i]);
        }
    }
    public void setLanguagesId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        languagesId = new long[data.length];
        for (int i = 0; i < languagesId.length; i++) {
            if (data[i].equals("")) continue;
            languagesId[i] = Long.parseLong(data[i]);
        }
    }
    public void setCitizenShipsId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        citizenShipsId = new long[data.length];
        for (int i = 0; i < citizenShipsId.length; i++) {
            if (data[i].equals("")) continue;
            citizenShipsId[i] = Long.parseLong(data[i]);
        }
    }

    public void setEmploymentType(String text) {
        employmentType = EnumEmploymentType.getEnum(text);
    }

    public void setSchedule(String text) {
        schedule = EnumSchedule.getEnum(text);
    }

    public void setMinWorkPeriod(String text) {
        minWorkPeriod = Period.parse(text);
    }

    public void setMinWorkPeriodOfYears(int years) {
        minWorkPeriod = Period.ofYears(years);
    }

    public void setMaxWorkPeriod(String text) {
        maxWorkPeriod = Period.parse(text);
    }
    public void setMaxWorkPeriodOfYears(int years) {
        maxWorkPeriod = Period.ofYears(years);
    }
    public void setCreationDate(String text) {
        creationDate = LocalDateTime.parse(text);
    }
    public void setUpdateDate(String text) {
        updateDate = LocalDateTime.parse(text);
    }

    public String getStatus() {
        return status.getUnlocalizedName();
    }
    public EnumStatus getStatusEnum() {
        return status;
    }

    public String getAddress() {
        return address.save();
    }

    public String getDisplayAddress() {
        return address.getDisplayAddress();
    }

    public String getContactInfo() {
        return contactInfo.save();
    }

    public ContactInfo getContactInfoObject() {
        return contactInfo;
    }

    public String getCompanyDisplayName() {
        return Server.COMPANIES_TABLE.getNameById(companyId);
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
    public String getSkillsId() {
        StringBuilder sb = new StringBuilder();
        for (long skillId: skillsId) {
            sb.append(skillId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }
    public String getSkills() {
        StringBuilder sb = new StringBuilder();
        for (long id: skillsId) {
            sb.append(", ").append(Server.SKILLS_TABLE.getNameById(id));
        }
        return sb.substring(2);
    }
    public String getLanguagesId() {
        StringBuilder sb = new StringBuilder();
        for (long languageId: languagesId) {
            sb.append(languageId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }
    public String getLanguages() {
        StringBuilder sb = new StringBuilder();
        for (long id: languagesId) {
            sb.append(", ").append(Server.LANGUAGES_TABLE.getNameById(id));
        }
        return sb.substring(2);
    }
    public String getCitizenShipsId() {
        StringBuilder sb = new StringBuilder();
        for (long citizenShipId: citizenShipsId) {
            sb.append(citizenShipId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }
    public String getCitizenShips() {
        StringBuilder sb = new StringBuilder();
        for (long id: citizenShipsId) {
            sb.append(", ").append(Server.CITIZEN_SHIPS_TABLE.getNameById(id));
        }
        return sb.substring(2);
    }

    public String getEmploymentType() {
        return employmentType.getUnlocalizedName();
    }

    public String getSchedule() {
        return schedule.getUnlocalizedName();
    }
    public String getMinWorkPeriod() {
        return minWorkPeriod.toString();
    }
    public Period getMinWorkPeriodObject() {
        return minWorkPeriod;
    }
    public String getMaxWorkPeriod() {
        return maxWorkPeriod.toString();
    }
    public Period getMaxWorkPeriodObject() {
        return maxWorkPeriod;
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
        return getFields(Vacancy.class);
    }

    public boolean hasAnySchedule(Set<EnumSchedule> enumSchedules) {
        if (enumSchedules.size() == 0) return true;
        return enumSchedules.contains(schedule);
    }

    public boolean hasAnyEmploymentType(Set<EnumEmploymentType> enumEmploymentTypes) {
        if (enumEmploymentTypes.size() == 0) return true;
        return enumEmploymentTypes.contains(employmentType);
    }

    public boolean hasAnyProfessionId(Set<Long> ids) {
        return Utils.hasAnyInArray(professionsId, ids);
    }
    public boolean hasAllProfessionsId(Set<Long> ids) {
        return Utils.hasAllInArray(professionsId, ids);
    }
    public boolean hasAnySkillId(Set<Long> ids) {
        return Utils.hasAnyInArray(skillsId, ids);
    }
    public boolean hasAllSkillsId(Set<Long> ids) {
        return Utils.hasAllInArray(skillsId, ids);
    }
    public boolean hasAnyLanguageId(Set<Long> ids) {
        return Utils.hasAnyInArray(languagesId, ids);
    }
    public boolean hasAllLanguagesId(Set<Long> ids) {
        return Utils.hasAllInArray(languagesId, ids);
    }
    public boolean hasAnyCitizenShipId(Set<Long> ids) {
        return Utils.hasAnyInArray(citizenShipsId, ids);
    }
    public boolean hasAllCitizenShipsId(Set<Long> ids) {
        return Utils.hasAllInArray(citizenShipsId, ids);
    }
}
