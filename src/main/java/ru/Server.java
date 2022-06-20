package ru;

import ru.db.*;
import ru.managers.ColumnManager;

public class Server {

    //Менеджер для сохранения и загрузки объектов в базу данных. Автоматически разделяет их по столбцам
    public static final ColumnManager cm = new ColumnManager();

    //Таблицы базы данных
    public static final UsersTable USERS_TABLE = new UsersTable();
    public static final CompaniesTable COMPANIES_TABLE = new CompaniesTable();
    public static final ResumesTable RESUME_TABLE = new ResumesTable();
    public static final VacanciesTable VACANCIES_TABLE = new VacanciesTable();
    public static final SkillsTable SKILLS_TABLE = new SkillsTable();
    public static final ProfessionsTable PROFESSION_TABLE = new ProfessionsTable();
    public static final LanguagesTable LANGUAGES_TABLE = new LanguagesTable();
    public static final CitizenShipsTable CITIZEN_SHIPS_TABLE = new CitizenShipsTable();
    public static final CompanyTypesTable COMPANY_TYPES_TABLE = new CompanyTypesTable();

}
