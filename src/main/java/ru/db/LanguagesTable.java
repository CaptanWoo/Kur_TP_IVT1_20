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

public class LanguagesTable extends DataBaseTable {

    public LanguagesTable() {
        //При создании базы данных передаём родительскому классу путь до файла таблицы и заголовок с названиями столбцов.
        //Названия столбцов берём через метод getHeader у класса от аннотаций @Column, которые висят над переменными в классе.
        super(EnumDataPaths.LANGUAGES_TABLE, Language.getHeader());
        //Отправляем заголовок таблицы в метод, который проверяет, нужно ли пересобрать таблицу
        rebase(Language.getHeader());
        //Обновляем хеш-сеты с набором данных для быстрого доступа
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
                //Загружает объект из строки через находящийся на сервере ColumnManager (cm)
                Language language = Server.cm.loadObject(data, Language.class);
                //Добавляем загруженный объект в хеш-сеты, чтобы потом быстро достать его имя по id и id по имени
                if (language.getStatus().equals(EnumStatus.ACTIVE.getUnlocalizedName())) {
                    idToNameMap.put(language.getId(), language.getDisplayName());
                    nameToIdMap.put(language.getDisplayName(), language.getId());
                }
            }
        });
    }

    public void addLanguage(Language language) {
        appendRecord(data -> {
            if (data != null) {
                Language lastLanguage = Server.cm.loadObject(data, Language.class);
                language.setId(lastLanguage.getId()+1);
            } else language.setId(1);
            return Server.cm.saveObject(language);
        });
        updateHashData();
    }

    public void replaceObject(AuthData authData, CommonObjectDTO dto) throws Exception {
        if (!Server.USERS_TABLE.hasAccess(authData, EnumRole.ADMIN)) throw new Exception("Недостаточно прав для выполнения этого действия");
        if (dto == null) throw new Exception("Нет данных для добавления");

        Language language = new Language();
        language.setId(dto.getId());
        language.setStatus(dto.getStatus());
        language.setDisplayName(dto.getDisplayName().trim());

        rewrite((reader, writer) -> {
            Language lastLanguage;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                lastLanguage = Server.cm.loadObject(line, Language.class);
                if (lastLanguage.getId() == language.getId()) {
                    line = Server.cm.saveObject(language);
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
            Language language = Server.cm.loadObject(data, Language.class);
            return language.getDisplayName().toLowerCase().equals(smallDisplayName);
        });

        if (hasSameObject) throw new Exception("Объект с таким именем уже существует");

        Language language = new Language();
        language.setStatus(dto.getStatus());
        language.setDisplayName(dto.getDisplayName().trim());
        addLanguage(language);
    }

    public List<CommonObjectDTO> getObjectList(AuthData authData) {

        if (!Server.USERS_TABLE.hasAccess(authData, EnumRole.MODERATOR)) return null;

        List<CommonObjectDTO> list = new ArrayList<>();
        readRecord(data -> {
            if (data != null) {
                Language language = Server.cm.loadObject(data, Language.class);
                list.add(new CommonObjectDTO(
                        language.getId(),
                        language.getStatus(),
                        language.getDisplayName()
                ));
            }
        });
        return list;
    }

}
