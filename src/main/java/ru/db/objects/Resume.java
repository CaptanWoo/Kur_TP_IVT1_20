package ru.db.objects;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.Server;
import ru.annotations.Column;
import ru.db.DataBaseTable;
import lombok.Data;
import ru.utils.Utils;
import ru.utils.enums.EnumEducationLevel;
import ru.utils.enums.EnumEmploymentType;
import ru.utils.enums.EnumSchedule;
import ru.utils.enums.EnumStatus;
import ru.utils.objects.Address;
import ru.utils.objects.Education;
import ru.utils.objects.Recommendation;
import ru.utils.objects.Work;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class Resume extends DBObject {

    @Column(name = "id") private long id;
    @Column(name = "status") private EnumStatus status = EnumStatus.NOT_STATED;
    @Column(name = "creatorId") private long creatorId;
    @Column(name = "displayName") private String displayName;
    @Column(name = "employmentType") private EnumEmploymentType employmentType = EnumEmploymentType.NOT_STATED;
    @Column(name = "schedule")  private EnumSchedule schedule = EnumSchedule.NOT_STATED;
    @Column(name = "description") private String description;
    @Column(name = "salary") private int salary;
    @Column(name = "address") private final Address address = new Address();
    @Column(name = "educations") private Education[] educations = new Education[0];
    @Column(name = "works") private Work[] works = new Work[0];
    @Column(name = "professionsId") private long[] professionsId = new long[0];
    @Column(name = "skillsId") private long[] skillsId = new long[0];
    @Column(name = "recommendations") private Recommendation[] recommendations = new Recommendation[0];
    @Column(name = "creationDate") private LocalDateTime creationDate = LocalDateTime.now();
    @Column(name = "updateDate") private LocalDateTime updateDate = LocalDateTime.now();

    public Period getSumWorkPeriod() {
        Period period = Period.ZERO;
        for (Work work: works) period = period.plus(work.getPeriod());
        return period;
    }

    public void setStatus(String name) {
        status = EnumStatus.getEnum(name);
    }

    public void setCreationDate(String text) {
        creationDate = LocalDateTime.parse(text);
    }
    public void setUpdateDate(String text) {
        updateDate = LocalDateTime.parse(text);
    }

    public void setProfessionsId(long[] professionsId) {
        this.professionsId = professionsId;
    }

    public void setProfessionsId(String text) {
        String[] data = text.split(DataBaseTable.numberArraySeparator);
        professionsId = new long[data.length];
        for (int i = 0; i < professionsId.length; i++) {
            if (data[i].equals("")) continue;
            professionsId[i] = Long.parseLong(data[i]);
        }
    }

    public void setAddress(String text) {
        address.load(text);
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

    public void setSkillsId(long[] skillsId) {
        this.skillsId = skillsId;
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
    public void setEmploymentType(String name) {
        employmentType = EnumEmploymentType.getEnum(name);
    }
    public void setSchedule(String name) {
        schedule = EnumSchedule.getEnum(name);
    }

    public String getStatus() {
        return status.getUnlocalizedName();
    }


    public EnumStatus getStatusEnum() {
        return status;
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

    public String getProfessionsId() {
        StringBuilder sb = new StringBuilder();
        for (long professionId: professionsId) {
            sb.append(professionId).append(DataBaseTable.numberArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public String getAddress() {
        return address.save();
    }

    public String getDisplayAddress() {
        return address.getDisplayAddress();
    }

    public String getRegion() {
        StringBuilder sb = new StringBuilder();
        if (!address.getCountry().equals("")) sb.append(address.getCountry()).append(", ");
        if (!address.getRegion().equals("")) sb.append(address.getRegion()).append(", ");
        if (!address.getCity().equals("")) sb.append(address.getCity()).append(", ");
        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public String getProfessions() {
        StringBuilder sb = new StringBuilder();
        for (long id: professionsId) {
            sb.append(", ").append(Server.PROFESSION_TABLE.getNameById(id));
        }
        return sb.substring(2);
    }

    public String getEducations() {
        StringBuilder sb = new StringBuilder();
        for (Education education: educations) {
            sb.append(education.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public String getMainEducation() {
        if (educations.length == 0) return "Отсутствует";
        Education education = educations[0];
        return education.getInstitute() + ", " + education.getYear();
    }

    public String getWorks() {
        StringBuilder sb = new StringBuilder();
        for (Work work: works) {
            sb.append(work.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public String getLastWork() {
        if (works.length == 0) return "Отсутствует";
        Work work = works[0];
        return work.getCompany() + ", " + work.getPosition() + ", " +
                work.getStartDate() + " - " + work.getEndDate();
    }

    public String getSkills() {
        StringBuilder sb = new StringBuilder();
        for (long id: skillsId) {
            sb.append(", ").append(Server.SKILLS_TABLE.getNameById(id));
        }
        return sb.substring(2);
    }
    public String getSkillsId() {
        StringBuilder sb = new StringBuilder();
        for (long id: skillsId) {
            sb.append(id).append(DataBaseTable.numberArraySeparator);
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
    public String getEmploymentType() {
        return employmentType.getUnlocalizedName();
    }
    public String getDisplayEmploymentType() {
        return employmentType.getDisplayName();
    }

    public String getSchedule() {
        return schedule.getUnlocalizedName();
    }

    public String getDisplaySchedule() {
        return schedule.getDisplayName();
    }

    public boolean hasAnySchedule(Set<EnumSchedule> enumSchedules) {
        if (enumSchedules.size() == 0) return true;
        return enumSchedules.contains(schedule);
    }

    public boolean hasAnyEducationLevels(Set<EnumEducationLevel> enumEducationLevels) {
        if (enumEducationLevels.size() == 0) return true;
        for (Education education: educations) {
            if (enumEducationLevels.contains(education.getLevel())) return true;
        }
        return false;
    }

    public boolean hasAllEducationLevels(Set<EnumEducationLevel> enumEducationLevels) {
        if (enumEducationLevels.size() == 0) return true;

        for (EnumEducationLevel enumLevel: enumEducationLevels) {
            if (!hasEducationLevel(enumLevel)) return false;
        }
        return true;
    }

    public boolean hasEducationLevel(EnumEducationLevel enumEducationLevel) {
        for (Education education: educations) {
            if (education.getLevel() == enumEducationLevel) return true;
        }
        return false;
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

    public static String getHeader() {
        return getFields(Resume.class);
    }
}
