package ru.sio;

import lombok.Getter;
import ru.Server;
import ru.db.DataBaseTable;
import ru.db.objects.Resume;
import ru.db.objects.User;
import ru.dto.resume.ResumeSearchDTO;
import ru.utils.enums.*;
import ru.utils.interfaces.ICheckResume;

import java.time.Period;
import java.util.HashSet;
import java.util.Set;

@Getter
public class ResumeSIO implements ICheckResume {

    private final int minSalary, maxSalary, minWorkPeriod, maxWorkPeriod, minAge, maxAge;
    private final EnumSex sex;
    private final Set<EnumEducationLevel> educationLevels = new HashSet<>();
    private final Set<EnumEmploymentType> employmentTypes = new HashSet<>();
    private final Set<EnumSchedule> enumSchedules = new HashSet<>();
    private final Set<Long> professionsId = new HashSet<>(), skillsId = new HashSet<>(), languagesId = new HashSet<>(), citizenShipsId = new HashSet<>();
    private final boolean educationsAll, professionsAll, skillsAll, languagesAll, citizenShipsAll;

    public ResumeSIO(ResumeSearchDTO dto) {
        this.minSalary = dto.getMinSalary();
        this.maxSalary = dto.getMaxSalary();
        this.minWorkPeriod = dto.getMinWorkPeriod();
        this.maxWorkPeriod = dto.getMaxWorkPeriod();
        this.minAge = dto.getMinAge();
        this.maxAge = dto.getMaxAge();
        this.sex = EnumSex.getEnum(dto.getSex());

        for (String educationLevel: dto.getEducationLevels().split(DataBaseTable.objectArraySeparator)) {
            if (!educationLevel.equals("")) educationLevels.add(EnumEducationLevel.getEnum(educationLevel));
        }
        for (String employmentType: dto.getEmploymentTypes().split(DataBaseTable.objectArraySeparator)) {
            if (!employmentType.equals("")) employmentTypes.add(EnumEmploymentType.getEnum(employmentType));
        }
        for (String schedule: dto.getSchedules().split(DataBaseTable.objectArraySeparator)) {
            if (!schedule.equals("")) enumSchedules.add(EnumSchedule.getEnum(schedule));
        }
        for (String profession: dto.getProfessions().split(DataBaseTable.objectArraySeparator)) {
            if (!profession.equals("")) professionsId.add(Server.PROFESSION_TABLE.getIdByName(profession));
        }
        for (String skill: dto.getSkills().split(DataBaseTable.objectArraySeparator)) {
            if (!skill.equals("")) skillsId.add(Server.SKILLS_TABLE.getIdByName(skill));
        }
        for (String language: dto.getLanguages().split(DataBaseTable.objectArraySeparator)) {
            if (!language.equals("")) languagesId.add(Server.LANGUAGES_TABLE.getIdByName(language));
        }
        for (String citizenShip: dto.getCitizenShips().split(DataBaseTable.objectArraySeparator)) {
            if (!citizenShip.equals("")) citizenShipsId.add(Server.CITIZEN_SHIPS_TABLE.getIdByName(citizenShip));
        }
        educationsAll = dto.isEducationsAll();
        professionsAll = dto.isProfessionsAll();
        skillsAll = dto.isSkillsAll();
        languagesAll = dto.isLanguagesAll();
        citizenShipsAll = dto.isCitizenShipsAll();
    }

    /**
     * Метод для проверки, подходит ли проверяемое резюме по условиям поиска резюме
     * @param resume проверяемое резюме
     * @param user пользователь, создавший резюме, из которого берутся некоторые данные
     * @return результат проверки резюме
     */
    @Override
    public boolean checkResume(Resume resume, User user) {
        if (resume == null || user == null) return false;
        if (resume.getStatusEnum() != EnumStatus.ACTIVE) return false;
        Period workPeriod = resume.getSumWorkPeriod();

        boolean
                salaryFlag = minSalary == 0 && maxSalary == 0 ||
                        minSalary > 0 && resume.getSalary() >= minSalary && maxSalary == 0 ||
                        resume.getSalary() >= minSalary && resume.getSalary() <= maxSalary,

                workPeriodFlag = minWorkPeriod == 0 && maxWorkPeriod == 0 ||
                        minWorkPeriod > 0 && workPeriod.getYears() >= minWorkPeriod && maxWorkPeriod == 0 ||
                        workPeriod.getYears() >= minWorkPeriod && workPeriod.getYears() <= maxWorkPeriod,

                ageFlag = minAge == 0 && maxAge == 0 ||
                        minAge > 0 && user.getAge() >= minAge && maxAge == 0 ||
                        user.getAge() >= minAge && user.getAge() <= maxAge,

                sexFlag = (sex == EnumSex.NOT_STATED || sex == user.getEnumSex()),
                educationLevelsFlag = educationsAll ? resume.hasAllEducationLevels(educationLevels) : resume.hasAnyEducationLevels(educationLevels),
                employmentTypeFlag = resume.hasAnyEmploymentType(employmentTypes),
                schedulesFlag = resume.hasAnySchedule(enumSchedules),
                professionsIdFlag = professionsAll ? resume.hasAllProfessionsId(professionsId) : resume.hasAnyProfessionId(professionsId),
                skillsIdFlag = skillsAll ? user.hasAllSkillsId(skillsId) : user.hasAnySkillId(skillsId),
                languagesIdFlag = languagesAll ? user.hasAllLanguagesId(languagesId) : user.hasAnyLanguageId(languagesId),
                citizenShipsIdFlag = citizenShipsAll ? user.hasAllCitizenShipsId(citizenShipsId) : user.hasAnyCitizenShipId(citizenShipsId);

        return salaryFlag && workPeriodFlag && ageFlag && sexFlag && educationLevelsFlag && employmentTypeFlag &&
                schedulesFlag && professionsIdFlag && skillsIdFlag && languagesIdFlag && citizenShipsIdFlag;
    }
}
