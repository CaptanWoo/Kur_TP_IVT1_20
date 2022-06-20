package ru.db;

import ru.Server;
import ru.db.objects.CitizenShip;
import ru.db.objects.Language;
import ru.db.objects.Profession;
import ru.db.objects.Skill;
import ru.dto.control.CommonObjectDTO;
import ru.objects.AuthData;
import ru.utils.enums.EnumDataPaths;
import ru.utils.enums.EnumRole;
import ru.utils.enums.EnumStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProfessionsTable extends DataBaseTable {

    public ProfessionsTable() {
        //При создании базы данных передаём родительскому классу путь до файла таблицы и заголовок с названиями столбцов.
        //Названия столбцов берём через метод getHeader у класса от аннотаций @Column, которые висят над переменными в классе.
        super(EnumDataPaths.PROFESSIONS_TABLE, Profession.getHeader());
        //Отправляем заголовок таблицы в метод, который проверяет, нужно ли пересобрать таблицу
        rebase(Profession.getHeader());
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
                Profession profession = Server.cm.loadObject(data, Profession.class);
                if (profession.getStatus().equals(EnumStatus.ACTIVE.getUnlocalizedName())) {
                    idToNameMap.put(profession.getId(), profession.getDisplayName());
                    nameToIdMap.put(profession.getDisplayName(), profession.getId());
                }
            }
        });
    }

    public void addProfession(Profession profession) {
        appendRecord(data -> {
            if (data != null) {
                Profession lastProfession = Server.cm.loadObject(data, Profession.class);
                profession.setId(lastProfession.getId()+1);
            } else profession.setId(1);
            return Server.cm.saveObject(profession);
        });
        updateHashData();
    }

    public void replaceObject(AuthData authData, CommonObjectDTO dto) throws Exception {
        if (!Server.USERS_TABLE.hasAccess(authData, EnumRole.ADMIN)) throw new Exception("Недостаточно прав для выполнения этого действия");
        if (dto == null) throw new Exception("Нет данных для добавления");

        Profession profession = new Profession();
        profession.setId(dto.getId());
        profession.setStatus(dto.getStatus());
        profession.setDisplayName(dto.getDisplayName().trim());

        rewrite((reader, writer) -> {
            Profession lastProfession;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                lastProfession = Server.cm.loadObject(line, Profession.class);
                if (lastProfession.getId() == profession.getId()) {
                    line = Server.cm.saveObject(profession);
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
            Profession profession = Server.cm.loadObject(data, Profession.class);
            return profession.getDisplayName().toLowerCase().equals(smallDisplayName);
        });

        if (hasSameObject) throw new Exception("Объект с таким именем уже существует");

        Profession profession = new Profession();
        profession.setStatus(dto.getStatus());
        profession.setDisplayName(dto.getDisplayName().trim());
        addProfession(profession);
    }

    public List<CommonObjectDTO> getObjectList(AuthData authData) {

        if (!Server.USERS_TABLE.hasAccess(authData, EnumRole.MODERATOR)) return null;

        List<CommonObjectDTO> list = new ArrayList<>();
        readRecord(data -> {
            if (data != null) {
                Profession profession = Server.cm.loadObject(data, Profession.class);
                list.add(new CommonObjectDTO(
                        profession.getId(),
                        profession.getStatus(),
                        profession.getDisplayName()
                ));
            }
        });
        return list;
    }

}
