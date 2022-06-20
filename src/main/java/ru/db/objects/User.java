package ru.db.objects;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.Server;
import ru.annotations.Column;
import ru.db.DataBaseTable;
import lombok.Data;
import ru.utils.Utils;
import ru.utils.enums.EnumRole;
import ru.utils.enums.EnumSex;
import ru.utils.enums.EnumStatus;
import ru.utils.objects.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class User extends DBObject {

    @Column(name = "id") private long id;
    @Column(name = "status") private EnumStatus status = EnumStatus.NOT_STATED;
    @Column(name = "email") private String email;
    @Column(name = "phoneNumber") private String phoneNumber;
    @Column(name = "password") private String password;
    @Column(name = "lastName") private String lastName;
    @Column(name = "firstName") private String firstName;
    @Column(name = "middleName") private String middleName;
    @Column(name = "birthDate") private LocalDate birthDate;
    @Column(name = "creationDate") private LocalDateTime creationDate = LocalDateTime.now();
    @Column(name = "sex") private EnumSex sex = EnumSex.NOT_STATED;
    @Column(name = "professionsId") private long[] professionsId = new long[0];
    @Column(name = "skillsId") private long[] skillsId = new long[0];
    @Column(name = "languagesId") private long[] languagesId = new long[0];
    @Column(name = "citizenShipsId") private long[] citizenShipsId = new long[0];
    @Column(name = "address") private final Address address = new Address();
    @Column(name = "educations") private Education[] educations = new Education[0];
    @Column(name = "works") private Work[] works = new Work[0];
    @Column(name = "recommendations") private Recommendation[] recommendations = new Recommendation[0];
    @Column(name = "resumesId") private long[] resumesId = new long[0];
    @Column(name = "vacanciesId") private long[] vacanciesId = new long[0];
    @Column(name = "companiesId") private long[] companiesId = new long[0];
    @Column(name = "role") private EnumRole role = EnumRole.USER;

    public void addResumeId(long id) {
        resumesId = Utils.addLongToArray(resumesId, id);
    }
    public void removeResumeId(long id) {
        resumesId = Utils.removeLongFromArray(resumesId, id);
    }
    public void addVacancyId(long id) {
        vacanciesId = Utils.addLongToArray(vacanciesId, id);
    }
    public void removeVacancyId(long id) {
        vacanciesId = Utils.removeLongFromArray(vacanciesId, id);
    }
    public void addCompanyId(long id) {
        companiesId = Utils.addLongToArray(companiesId, id);
    }
    public void removeCompanyId(long id) {
        companiesId = Utils.removeLongFromArray(companiesId, id);
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public void setStatus(String name) {
        status = EnumStatus.getEnum(name);
    }

    public void setSex(String name) {
        sex = EnumSex.getEnum(name);
    }

    public void setProfessionsId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        professionsId = new long[data.length];
        for (int i = 0; i < professionsId.length; i++) {
            if (data[i].equals("")) continue;
            professionsId[i] = Long.parseLong(data[i]);
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
    public void setLanguagesId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        languagesId = new long[data.length];
        for (int i = 0; i < languagesId.length; i++) {
            if (data[i].equals("")) continue;
            languagesId[i] = Long.parseLong(data[i]);
        }
    }

    public void setAddress(String text) {
        address.load(text);
    }

    public void setBirthDate(String text) {
        birthDate = LocalDate.parse(text);
    }

    public void setCreationDate(String text) {
        creationDate = LocalDateTime.parse(text);
    }

    public void setEducations(String text) {
        String[] data = text.split(DataBaseTable.objectArraySeparator);
        educations = new Education[data.length];
        for (int i = 0; i < educations.length; i++) educations[i] = new Education(data[i]);
    }
    public void setWorks(String text) {
        String[] data = text.split(DataBaseTable.objectArraySeparator);
        works = new Work[data.length];
        for (int i = 0; i < works.length; i++) works[i] = new Work(data[i]);
    }

    public void setSkillsId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        skillsId = new long[data.length];
        for (int i = 0; i < skillsId.length; i++) {
            if (data[i].equals("")) continue;
            skillsId[i] = Long.parseLong(data[i]);
        }
    }
    public void setRecommendations(String text) {
        String[] data = text.split(DataBaseTable.objectArraySeparator);
        recommendations = new Recommendation[data.length];
        for (int i = 0; i < recommendations.length; i++) recommendations[i] = new Recommendation(data[i]);
    }
    public void setResumesId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        resumesId = new long[data.length];
        for (int i = 0; i < resumesId.length; i++) {
            if (data[i].equals("")) continue;
            resumesId[i] = Long.parseLong(data[i]);
        }
    }
    public void setCompaniesId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        companiesId = new long[data.length];
        for (int i = 0; i < companiesId.length; i++) {
            if (data[i].equals("")) continue;
            companiesId[i] = Long.parseLong(data[i]);
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
    public void setRole(String text) {
        role = EnumRole.getEnumByUnlocalizedName(text);
    }

    public String getStatus() {
        return status.getUnlocalizedName();
    }

    public String getSex() {
        return sex.getUnlocalizedName();
    }

    public EnumSex getEnumSex() {
        return sex;
    }

    public String getProfessionsId() {
        StringBuilder sb = new StringBuilder();
        for (long professionId: professionsId) {
            sb.append(professionId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public String getSkillsId() {
        StringBuilder sb = new StringBuilder();
        for (long skillId: skillsId) {
            sb.append(skillId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public String getLanguagesId() {
        StringBuilder sb = new StringBuilder();
        for (long languageId: languagesId) {
            sb.append(languageId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public String getCitizenShipsId() {
        StringBuilder sb = new StringBuilder();
        for (long citizenShipId: citizenShipsId) {
            sb.append(citizenShipId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public String getProfessions() {
        if (professionsId.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (long professionId: professionsId) {
            sb.append(DataBaseTable.objectArraySeparator).append(Server.PROFESSION_TABLE.getNameById(professionId));
        }
        return sb.substring(1);
    }

    public String getSkills() {
        StringBuilder sb = new StringBuilder();
        for (long skillId: skillsId) {
            sb.append(DataBaseTable.objectArraySeparator).append(Server.SKILLS_TABLE.getNameById(skillId));
        }
        return sb.substring(1);
    }


    public String getLanguages() {
        if (languagesId.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (long id: languagesId) {
            sb.append(DataBaseTable.objectArraySeparator).append(Server.LANGUAGES_TABLE.getNameById(id));
        }
        return sb.substring(1);
    }

    public String getCitizenShips() {
        if (citizenShipsId.length == 0) return "Отсутствует";
        StringBuilder sb = new StringBuilder();
        for (long id: citizenShipsId) {
            sb.append(DataBaseTable.objectArraySeparator).append(Server.CITIZEN_SHIPS_TABLE.getNameById(id));
        }
        return sb.substring(1);
    }

    public String getAddress() {
        return address.save();
    }

    public Address getAddressObject() {
        return address;
    }

    public String getBirthDate() {
        return birthDate.toString();
    }

    public String getCreationDate() {
        return creationDate.toString();
    }

    public String getEducations() {
        StringBuilder sb = new StringBuilder();
        for (Education education: educations) {
            sb.append(education.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public String getWorks() {
        StringBuilder sb = new StringBuilder();
        for (Work work: works) {
            sb.append(work.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public String getRecommendations() {
        StringBuilder sb = new StringBuilder();
        for (Recommendation recommendation: recommendations) {
            sb.append(recommendation.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public String getResumesId() {
        StringBuilder sb = new StringBuilder();
        for (long resumeId: resumesId) {
            sb.append(resumeId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }
    public String getCompaniesId() {
        StringBuilder sb = new StringBuilder();
        for (long companyId: companiesId) {
            sb.append(companyId).append(DataBaseTable.numberArraySeparator);
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

    public String getRole() {
        return role.getUnlocalizedName();
    }

    public EnumRole getEnumRole() {
        return role;
    }

    public static String getHeader() {
        return getFields(User.class);
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
