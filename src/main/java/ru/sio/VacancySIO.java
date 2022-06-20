package ru.sio;

import ru.Server;
import ru.db.DataBaseTable;
import ru.db.objects.User;
import ru.db.objects.Vacancy;
import ru.dto.vacancy.VacancySearchDTO;
import ru.utils.enums.EnumEmploymentType;
import ru.utils.enums.EnumSchedule;
import ru.utils.enums.EnumStatus;
import ru.utils.interfaces.ICheckVacancy;

import java.time.Period;
import java.util.HashSet;
import java.util.Set;

public class VacancySIO implements ICheckVacancy {

    private final int minSalary, maxSalary, minWorkPeriod, maxWorkPeriod;
    private final Set<EnumEmploymentType> employmentTypes = new HashSet<>();
    private final Set<EnumSchedule> enumSchedules = new HashSet<>();
    private final Set<Long> professionsId = new HashSet<>(), skillsId = new HashSet<>(), languagesId = new HashSet<>(), citizenShipsId = new HashSet<>();
    private final boolean professionsAll, skillsAll, languagesAll, citizenShipsAll;

    public VacancySIO(VacancySearchDTO dto) {
        this.minSalary = dto.getMinSalary();
        this.maxSalary = dto.getMaxSalary();
        this.minWorkPeriod = dto.getMinWorkPeriod();
        this.maxWorkPeriod = dto.getMaxWorkPeriod();

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
        professionsAll = dto.isProfessionsAll();
        skillsAll = dto.isSkillsAll();
        languagesAll = dto.isLanguagesAll();
        citizenShipsAll = dto.isCitizenShipsAll();
    }

    /**
     * Метод для проверки, подходит ли проверяемую вакансию по условиям поиска вакансий
     * @param vacancy проверяемая вакансия
     * @return результат проверки вакансии
     */
    @Override
    public boolean checkVacancy(Vacancy vacancy, User user) {
        if (vacancy == null) return false;
        if (vacancy.getStatusEnum() != EnumStatus.ACTIVE) return false;

        Period minVacancyWorkPeriod = vacancy.getMinWorkPeriodObject();
        Period maxVacancyWorkPeriod = vacancy.getMaxWorkPeriodObject();

        boolean
                salaryFlag = minSalary == 0 && maxSalary == 0 ||
                minSalary > 0 && vacancy.getMinSalary() >= minSalary && maxSalary == 0 ||
                vacancy.getMinSalary() >= minSalary && vacancy.getMaxSalary() <= maxSalary,

                workPeriodFlag = minWorkPeriod == 0 && maxWorkPeriod == 0 ||
                        minWorkPeriod > 0 && minVacancyWorkPeriod.getYears() >= minWorkPeriod && maxWorkPeriod == 0 ||
                        minVacancyWorkPeriod.getYears() >= minWorkPeriod && maxVacancyWorkPeriod.getYears() <= maxWorkPeriod,

                employmentTypeFlag = vacancy.hasAnyEmploymentType(employmentTypes),
                schedulesFlag = vacancy.hasAnySchedule(enumSchedules),
                professionsIdFlag = professionsAll ? vacancy.hasAllProfessionsId(professionsId) : vacancy.hasAnyProfessionId(professionsId),
                skillsIdFlag = skillsAll ? vacancy.hasAllSkillsId(skillsId) : vacancy.hasAnySkillId(skillsId),
                languagesIdFlag = languagesAll ? vacancy.hasAllLanguagesId(languagesId) : vacancy.hasAnyLanguageId(languagesId),
                citizenShipsIdFlag = citizenShipsAll ? vacancy.hasAllCitizenShipsId(citizenShipsId) : vacancy.hasAnyCitizenShipId(citizenShipsId);

        return salaryFlag && workPeriodFlag && employmentTypeFlag && schedulesFlag &&
                professionsIdFlag && skillsIdFlag && languagesIdFlag && citizenShipsIdFlag;
    }
}
