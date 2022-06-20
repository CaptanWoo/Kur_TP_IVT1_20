package ru.db;

import ru.Server;
import ru.db.objects.User;
import ru.dto.objects.AddressDTO;
import ru.dto.objects.ContactInfoDTO;
import ru.dto.resume.ResumeCreateUserInfoDTO;
import ru.dto.user.UserCompaniesDTO;
import ru.dto.user.UserCreateDTO;
import ru.dto.user.UserProfileUpdateDTO;
import ru.objects.AuthData;
import ru.utils.enums.EnumDataPaths;
import ru.utils.enums.EnumRole;
import ru.utils.enums.EnumStatus;
import ru.utils.objects.Address;

import java.util.HashSet;
import java.util.Set;

public class UsersTable extends DataBaseTable {

    public UsersTable() {
        //При создании базы данных передаём родительскому классу путь до файла таблицы и заголовок с названиями столбцов.
        //Названия столбцов берём через метод getHeader у класса от аннотаций @Column, которые висят над переменными в классе.
        super(EnumDataPaths.USERS_TABLE, User.getHeader());
        //Отправляем заголовок таблицы в метод, который проверяет, нужно ли пересобрать таблицу
        rebase(User.getHeader());
    }

    /**
     * Метод создания и добавления пользователя по данным из DTO объекта
     * @param userCreateDTO объект с данными о пользователе
     * @throws Exception ошибка создания пользователя
     */
    public void createUser(UserCreateDTO userCreateDTO) throws Exception {
        User user = new User();
        user.setStatus(EnumStatus.ACTIVE.getUnlocalizedName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPhoneNumber(userCreateDTO.getPhoneNumber());
        user.setPassword(userCreateDTO.getPassword());
        user.setLastName(userCreateDTO.getLastName());
        user.setFirstName(userCreateDTO.getFirstName());
        user.setMiddleName(userCreateDTO.getMiddleName());
        user.setBirthDate(userCreateDTO.getBirthDate());
        user.setSex(userCreateDTO.getSex());
        addUser(user);
    }

    /**
     * Метод проверки, имеет ли пользователь необходимую роль для получения доступа к каким-либо данным
     * @param authData ключ авторизации
     * @param minRole минимальная требуемая роль
     * @return результат проверки
     */
    public boolean hasAccess(AuthData authData, EnumRole minRole) {
        if (authData == null || minRole == null) return false;
        User user = getUser(authData.getId());
        if (authData.check(user.getId(), user.getEmail(), user.getPhoneNumber(), user.getPassword())) {
            return user.getEnumRole().hasAccess(minRole);
        }
        return false;
    }

    /**
     * Метод добавления пользователя в таблицу базы данных.
     * Сначала проверяется, нет ли пользователя с таким же логином почты или номером телефона в базе данных.
     * Если есть, то выкидывает ошибку. Если нет, то добавляем пользователя в базу данных
     * @param user пользователь
     * @throws Exception ошибка, если пользователь с такой почтой или номером телефона уже существует
     */
    public void addUser(User user) throws Exception {
        if (checkRecord(data -> {
            if (data != null) {
                User tempUser = Server.cm.loadObject(data, User.class);
                return tempUser.getEmail().equals(user.getEmail()) || tempUser.getPhoneNumber().equals(user.getPhoneNumber());
            }
            return false;
        })) throw new Exception("Пользователь с такой почтой или номером телефона уже существует.");

        appendRecord(data -> {
            if (data != null) {
                User lastUser = Server.cm.loadObject(data, User.class);
                user.setId(lastUser.getId()+1);
            } else user.setId(1);
            return Server.cm.saveObject(user);
        });
    }

    /**
     * Метод получения пользователя по его id.
     * @param id пользователя
     * @return пользователь
     */
    protected User getUser(long id) {
        String line = getRecord(data -> {
            User user = Server.cm.loadObject(data, User.class);
            return user.getId() == id;
        });

        if (line != null) return Server.cm.loadObject(line, User.class);
        else return null;
    }

    /**
     * Метод получения пользователя по ключу авторизации.
     * Если совпадает id && (email && пароль || номерТелефона && пароль), то возвращает пользователя
     */
    protected User getUser(AuthData authData) {
        User user = getUser(authData.getId());
        if (user == null) return null;
        if (authData.check(user.getId(), user.getEmail(), user.getPhoneNumber(), user.getPassword())) return user;
        else return null;
    }

    /**
     * Метод получения объекта DTO для передачи данных о пользоветеле в окно создания вакансии
     * @param authData ключ авторизации пользователя
     * @return DTO объект с данными пользователя
     */
    public ResumeCreateUserInfoDTO getResumeCreateUserInfoDTO(AuthData authData) {
        User user = getUser(authData);
        if (user == null) return null;

        return new ResumeCreateUserInfoDTO(
                user.getProfessions(),
                user.getSkills(),
                user.getEducations(),
                user.getWorks(),
                user.getRecommendations()
        );
    }

    public AddressDTO getUserAddressDTO(AuthData authData) {
        User user = getUser(authData);

        if (user != null) {
            Address address = user.getAddressObject();
            return new AddressDTO(address.getCountry(), address.getRegion(), address.getCity(), address.getStreet(), address.getHouse());
        } else return null;
    }

    public UserCompaniesDTO getUserCompaniesDTO(AuthData authData) {
        User user = getUser(authData);
        if (user == null) return null;
        String sids = user.getCompaniesId();
        String companiesDisplayNames = Server.COMPANIES_TABLE.getCompaniesDisplayNames(sids);
        return new UserCompaniesDTO(sids, companiesDisplayNames);
    }

    public ContactInfoDTO getUserContactInfoDTO(AuthData authData) {
        User user = getUser(authData);

        if (user != null) {
            return new ContactInfoDTO(user.getLastName() + " " + user.getFirstName() + " " + user.getMiddleName(), user.getEmail(), user.getPhoneNumber(), "");
        } else return null;
    }

    /**
     * Метод получения объекта DTO для передачи данных о профиле пользователя клиенту
     * @param authData ключ авторизации
     * @return DTO объект с данными о профиле
     */
    public UserProfileUpdateDTO getUserProfileUpdateDTO(AuthData authData) {
        User user = getUser(authData);
        if (user == null) return null;

        return new UserProfileUpdateDTO(
                user.getEmail(),
                user.getPhoneNumber(),
                user.getLastName(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getBirthDate(),
                user.getSex(),
                user.getAddress(),
                user.getEducations(),
                user.getWorks(),
                user.getRecommendations(),
                user.getProfessions(),
                user.getSkills(),
                user.getLanguages(),
                user.getCitizenShips()
        );
    }

    protected void updateUserAddResumeId(long id, long resumeId) {
        rewrite((reader, writer) -> {
            User user;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                user = Server.cm.loadObject(line, User.class);
                if (user.getId() == id) user.addResumeId(resumeId);
                writer.write(Server.cm.saveObject(user) + '\n');
            }
        });
    }
    protected void updateUserRemoveResumeId(long id, long resumeId) {
        rewrite((reader, writer) -> {
            User user;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                user = Server.cm.loadObject(line, User.class);
                if (user.getId() == id) user.removeResumeId(resumeId);
                writer.write(Server.cm.saveObject(user) + '\n');
            }
        });
    }
    protected void updateUserAddVacancyId(long id, long vacancyId) {
        rewrite((reader, writer) -> {
            User user;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                user = Server.cm.loadObject(line, User.class);
                if (user.getId() == id) user.addVacancyId(vacancyId);
                writer.write(Server.cm.saveObject(user) + '\n');
            }
        });
    }
    protected void updateUserRemoveVacancyId(long id, long vacancyId) {
        rewrite((reader, writer) -> {
            User user;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                user = Server.cm.loadObject(line, User.class);
                if (user.getId() == id) user.removeVacancyId(vacancyId);
                writer.write(Server.cm.saveObject(user) + '\n');
            }
        });
    }
    protected void updateUserAddCompanyId(long id, long companyId) {
        rewrite((reader, writer) -> {
            User user;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                user = Server.cm.loadObject(line, User.class);
                if (user.getId() == id) user.addCompanyId(companyId);
                writer.write(Server.cm.saveObject(user) + '\n');
            }
        });
    }
    protected void updateUserRemoveCompanyId(long id, long companyId) {
        rewrite((reader, writer) -> {
            User user;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                user = Server.cm.loadObject(line, User.class);
                if (user.getId() == id) user.removeCompanyId(companyId);
                writer.write(Server.cm.saveObject(user) + '\n');
            }
        });
    }

    /**
     * Метод для обновления данных о пользователе по id. Новые данные о профиле получаются от клиента
     * @param id пользователя
     * @param dto объект с новыми данными о профиле
     */
    public void updateUserData(long id, UserProfileUpdateDTO dto) {
        rewrite((reader, writer) -> {
            User user;
            String line = reader.readLine();
            writer.write(line + '\n');

            while ((line = reader.readLine()) != null) {
                user = Server.cm.loadObject(line, User.class);
                if (user.getId() == id) {
                    user.setEmail(dto.getEmail());
                    user.setPhoneNumber(dto.getPhoneNumber());
                    user.setLastName(dto.getLastName());
                    user.setFirstName(dto.getFirstName());
                    user.setMiddleName(dto.getMiddleName());
                    user.setBirthDate(dto.getBirthDate());
                    user.setSex(dto.getSex());
                    user.setAddress(dto.getAddress());
                    user.setEducations(dto.getEducations());
                    user.setWorks(dto.getWorks());
                    user.setRecommendations(dto.getRecommendations());

                    Set<Long> idHashSet = new HashSet<>();
                    StringBuilder sb = new StringBuilder();
                    long lid;

                    for (String name: dto.getProfessions().split(DataBaseTable.objectArraySeparator)) {
                        lid = Server.PROFESSION_TABLE.getIdByName(name);
                        if (lid > 0) idHashSet.add(lid);
                    }

                    user.setProfessionsId(assembleIds(idHashSet, sb));

                    for (String name: dto.getSkills().split(DataBaseTable.objectArraySeparator)) {
                        lid = Server.SKILLS_TABLE.getIdByName(name);
                        if (lid > 0) idHashSet.add(lid);
                    }

                    user.setSkillsId(assembleIds(idHashSet, sb));

                    for (String name: dto.getLanguages().split(DataBaseTable.objectArraySeparator)) {
                        lid = Server.LANGUAGES_TABLE.getIdByName(name);
                        if (lid > 0) idHashSet.add(lid);
                    }

                    user.setLanguagesId(assembleIds(idHashSet, sb));

                    for (String name: dto.getCitizenShips().split(DataBaseTable.objectArraySeparator)) {
                        lid = Server.CITIZEN_SHIPS_TABLE.getIdByName(name);
                        if (lid > 0) idHashSet.add(lid);
                    }

                    user.setCitizenShipsId(assembleIds(idHashSet, sb));
                }
                writer.write(Server.cm.saveObject(user) + '\n');
            }
        });
    }

    /**
     * Метод авторизации клиента по логину и паролю и получения ключа авторизации.
     * @param login логин (почта или телефон)
     * @param password пароль
     * @return ключ авторизации, если удалось авторизоваться по данным
     * @throws Exception ошибка, если не удалось авторизоваться по данным
     */
    public AuthData logIn(String login, String password) throws Exception {
        //Находим пользователя в базе данных по логину и паролю
        String line = getRecord((data) -> {
            User user = Server.cm.loadObject(data, User.class);
            if (user.getEmail().equals(login) || user.getPhoneNumber().equals(login)) {
                return user.getPassword().equals(password);
            }
            return false;
        });

        //Если удалось найти пользователя, создаём ключ авторизации с id, почтой, номером телефона и паролем
        if (line != null) {
            User user = Server.cm.loadObject(line, User.class);
            return new AuthData(user.getId(), user.getEmail(), user.getPhoneNumber(), user.getPassword());
        }
        //Иначе сообщаем о том, что не нашли пользователя
        else throw new Exception("Пользователь не существует или введены некорректные данные.");
    }

    /**
     * Метод для обновления данных о пользователе по id. Новые данные о профиле получаются от клиента
     * @param authData ключ авторизации пользователя
     * @param userProfileUpdateDTO объект с новыми данными о профиле
     */
    public AuthData updateUser(AuthData authData, UserProfileUpdateDTO userProfileUpdateDTO) {
        updateUserData(authData.getId(), userProfileUpdateDTO);
        return new AuthData(authData.getId(), userProfileUpdateDTO.getEmail(), userProfileUpdateDTO.getPhoneNumber(), authData.getPassword());
    }

}
