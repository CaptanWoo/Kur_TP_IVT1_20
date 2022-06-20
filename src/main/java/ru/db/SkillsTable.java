package ru.db;

import ru.Server;
import ru.db.objects.CompanyType;
import ru.db.objects.Skill;
import ru.dto.control.CommonObjectDTO;
import ru.objects.AuthData;
import ru.utils.enums.EnumDataPaths;
import ru.utils.enums.EnumRole;
import ru.utils.enums.EnumStatus;

import java.util.ArrayList;
import java.util.List;

public class SkillsTable extends DataBaseTable {

    public SkillsTable() {
        //При создании базы данных передаём родительскому классу путь до файла таблицы и заголовок с названиями столбцов.
        //Названия столбцов берём через метод getHeader у класса от аннотаций @Column, которые висят над переменными в классе.
        super(EnumDataPaths.SKILLS_TABLE, Skill.getHeader());
        //Отправляем заголовок таблицы в метод, который проверяет, нужно ли пересобрать таблицу
        rebase(Skill.getHeader());
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
                Skill skill = Server.cm.loadObject(data, Skill.class);
                if (skill.getStatus().equals(EnumStatus.ACTIVE.getUnlocalizedName())) {
                    idToNameMap.put(skill.getId(), skill.getDisplayName());
                    nameToIdMap.put(skill.getDisplayName(), skill.getId());
                }
            }
        });
    }

    public void addSkill(Skill skill) {
        appendRecord(data -> {
            if (data != null) {
                Skill lastSkill = Server.cm.loadObject(data, Skill.class);
                skill.setId(lastSkill.getId()+1);
            } else skill.setId(1);
            return Server.cm.saveObject(skill);
        });
        updateHashData();
    }

    public void replaceObject(AuthData authData, CommonObjectDTO dto) throws Exception {
        if (!Server.USERS_TABLE.hasAccess(authData, EnumRole.ADMIN)) throw new Exception("Недостаточно прав для выполнения этого действия");
        if (dto == null) throw new Exception("Нет данных для добавления");

        Skill skill = new Skill();
        skill.setId(dto.getId());
        skill.setStatus(dto.getStatus());
        skill.setDisplayName(dto.getDisplayName().trim());

        rewrite((reader, writer) -> {
            Skill lastSkill;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                lastSkill = Server.cm.loadObject(line, Skill.class);
                if (lastSkill.getId() == skill.getId()) {
                    line = Server.cm.saveObject(skill);
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
            Skill skill = Server.cm.loadObject(data, Skill.class);
            return skill.getDisplayName().toLowerCase().equals(smallDisplayName);
        });

        if (hasSameObject) throw new Exception("Объект с таким именем уже существует");

        Skill skill = new Skill();
        skill.setStatus(dto.getStatus());
        skill.setDisplayName(dto.getDisplayName().trim());
        addSkill(skill);
    }

    public List<CommonObjectDTO> getObjectList(AuthData authData) {

        if (!Server.USERS_TABLE.hasAccess(authData, EnumRole.MODERATOR)) return null;

        List<CommonObjectDTO> list = new ArrayList<>();
        readRecord(data -> {
            if (data != null) {
                Skill skill = Server.cm.loadObject(data, Skill.class);
                list.add(new CommonObjectDTO(
                        skill.getId(),
                        skill.getStatus(),
                        skill.getDisplayName()
                ));
            }
        });
        return list;
    }

}
