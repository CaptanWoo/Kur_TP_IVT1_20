package ru.db;

import ru.Server;
import ru.db.objects.*;
import ru.dto.company.CompanyPreviewDTO;
import ru.dto.control.CommonObjectDTO;
import ru.dto.vacancy.VacancyPreviewDTO;
import ru.dto.vacancy.VacancySearchDTO;
import ru.objects.AuthData;
import ru.objects.Page;
import ru.sio.VacancySIO;
import ru.utils.enums.EnumDataPaths;
import ru.utils.enums.EnumRole;
import ru.utils.enums.EnumStatus;
import ru.utils.interfaces.ICheckVacancy;

import java.util.ArrayList;
import java.util.List;

public class CompanyTypesTable extends DataBaseTable {

    public CompanyTypesTable() {
        //При создании базы данных передаём родительскому классу путь до файла таблицы и заголовок с названиями столбцов.
        //Названия столбцов берём через метод getHeader у класса от аннотаций @Column, которые висят над переменными в классе.
        super(EnumDataPaths.COMPANY_TYPES_TABLE, CompanyType.getHeader());
        //Отправляем заголовок таблицы в метод, который проверяет, нужно ли пересобрать таблицу
        rebase(CompanyType.getHeader());
        updateHashData();
    }

    /**
     * Метод, заполняющий хеш-сеты данными для быстрого доступа, чтобы потом при каждом обращении не нужно было
     * полностью проверять файл таблицы. В шех-сетах хранится связь id = отображаемое_имя.
     *
     * К примеру: id = 1, отображаемое_имя = Русский язык.
     *
     * Потом из этих хеш-сетов мы можем быстро получить отображаемое_имя по id и id по отображаемому_имени.
     */
    public void updateHashData() {
        idToNameMap.clear();
        nameToIdMap.clear();
        readRecord(data -> {
            if (data != null) {
                CompanyType companyType = Server.cm.loadObject(data, CompanyType.class);
                idToNameMap.put(companyType.getId(), companyType.getDisplayName());
                nameToIdMap.put(companyType.getDisplayName(), companyType.getId());
            }
        });
    }

    public void addCompanyType(CompanyType companyType) {
        appendRecord(data -> {
            if (data != null) {
                CompanyType lastCompanyType = Server.cm.loadObject(data, CompanyType.class);
                companyType.setId(lastCompanyType.getId()+1);
            } else companyType.setId(1);
            return Server.cm.saveObject(companyType);
        });
        updateHashData();
    }

    public void replaceObject(AuthData authData, CommonObjectDTO dto) throws Exception {
        if (!Server.USERS_TABLE.hasAccess(authData, EnumRole.ADMIN)) throw new Exception("Недостаточно прав для выполнения этого действия");
        if (dto == null) throw new Exception("Нет данных для добавления");

        CompanyType companyType = new CompanyType();
        companyType.setId(dto.getId());
        companyType.setStatus(dto.getStatus());
        companyType.setDisplayName(dto.getDisplayName().trim());

        rewrite((reader, writer) -> {
            CompanyType lastCompanyType;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                lastCompanyType = Server.cm.loadObject(line, CompanyType.class);
                if (lastCompanyType.getId() == companyType.getId()) {
                    line = Server.cm.saveObject(companyType);
                }

                writer.write(line + '\n');
            }
        });
        updateHashData();
    }

    public void addObject(AuthData authData, CommonObjectDTO dto) throws Exception {
        if (!Server.USERS_TABLE.hasAccess(authData, EnumRole.ADMIN)) throw new Exception("Недостаточно прав для выполнения этого действия");
        if (dto == null) throw new Exception("Нет данных для добавления");
        String smallDisplayName = dto.getDisplayName().toLowerCase().trim();
        boolean hasSameObject = checkRecord(data -> {
            CompanyType companyType = Server.cm.loadObject(data, CompanyType.class);
            return companyType.getDisplayName().toLowerCase().equals(smallDisplayName);
        });

        if (hasSameObject) throw new Exception("Объект с таким именем уже существует");

        CompanyType companyType = new CompanyType();
        companyType.setStatus(dto.getStatus());
        companyType.setDisplayName(dto.getDisplayName().trim());
        addCompanyType(companyType);
    }

    public List<CommonObjectDTO> getObjectList(AuthData authData) {

        if (!Server.USERS_TABLE.hasAccess(authData, EnumRole.MODERATOR)) return null;

        List<CommonObjectDTO> list = new ArrayList<>();
        readRecord(data -> {
            if (data != null) {
                CompanyType companyType = Server.cm.loadObject(data, CompanyType.class);
                list.add(new CommonObjectDTO(
                        companyType.getId(),
                        companyType.getStatus(),
                        companyType.getDisplayName()
                ));
            }
        });
        return list;
    }

}
