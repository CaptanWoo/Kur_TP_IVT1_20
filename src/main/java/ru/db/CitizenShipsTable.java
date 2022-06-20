package ru.db;

import ru.Server;
import ru.db.objects.CitizenShip;
import ru.db.objects.Profession;
import ru.dto.control.CommonObjectDTO;
import ru.objects.AuthData;
import ru.utils.enums.EnumDataPaths;
import ru.utils.enums.EnumRole;
import ru.utils.enums.EnumStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CitizenShipsTable extends DataBaseTable {

    public CitizenShipsTable() {
        //При создании базы данных передаём родительскому классу путь до файла таблицы и заголовок с названиями столбцов.
        //Названия столбцов берём через метод getHeader у класса от аннотаций @Column, которые висят над переменными в классе.
        super(EnumDataPaths.CITIZEN_SHIPS_TABLE, CitizenShip.getHeader());
        rebase(CitizenShip.getHeader());
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
                CitizenShip citizenShip = Server.cm.loadObject(data, CitizenShip.class);
                if (citizenShip.getStatus().equals(EnumStatus.ACTIVE.getUnlocalizedName())) {
                    idToNameMap.put(citizenShip.getId(), citizenShip.getDisplayName());
                    nameToIdMap.put(citizenShip.getDisplayName(), citizenShip.getId());
                }
            }
        });
    }

    public void addCitizenShip(CitizenShip citizenShip) {
        appendRecord(data -> {
            if (data != null) {
                CitizenShip lastCitizenShip = Server.cm.loadObject(data, CitizenShip.class);
                citizenShip.setId(lastCitizenShip.getId()+1);
            } else citizenShip.setId(1);
            return Server.cm.saveObject(citizenShip);
        });
        updateHashData();
    }

    public void replaceObject(AuthData authData, CommonObjectDTO dto) throws Exception {
        if (!Server.USERS_TABLE.hasAccess(authData, EnumRole.ADMIN)) throw new Exception("Недостаточно прав для выполнения этого действия");
        if (dto == null) throw new Exception("Нет данных для добавления");

        CitizenShip citizenShip = new CitizenShip();
        citizenShip.setId(dto.getId());
        citizenShip.setStatus(dto.getStatus());
        citizenShip.setDisplayName(dto.getDisplayName().trim());

        rewrite((reader, writer) -> {
            CitizenShip lastCitizenShip;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                lastCitizenShip = Server.cm.loadObject(line, CitizenShip.class);
                if (lastCitizenShip.getId() == citizenShip.getId()) {
                    line = Server.cm.saveObject(citizenShip);
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
            CitizenShip citizenShip = Server.cm.loadObject(data, CitizenShip.class);
            return citizenShip.getDisplayName().toLowerCase().equals(smallDisplayName);
        });

        if (hasSameObject) throw new Exception("Объект с таким именем уже существует");

        CitizenShip citizenShip = new CitizenShip();
        citizenShip.setStatus(dto.getStatus());
        citizenShip.setDisplayName(dto.getDisplayName().trim());
        addCitizenShip(citizenShip);
    }

    public List<CommonObjectDTO> getObjectList(AuthData authData) {

        if (!Server.USERS_TABLE.hasAccess(authData, EnumRole.MODERATOR)) return null;

        List<CommonObjectDTO> list = new ArrayList<>();
        readRecord(data -> {
            if (data != null) {
                CitizenShip citizenShip = Server.cm.loadObject(data, CitizenShip.class);
                list.add(new CommonObjectDTO(
                        citizenShip.getId(),
                        citizenShip.getStatus(),
                        citizenShip.getDisplayName()
                ));
            }
        });
        return list;
    }

}
