package ru.db;

import ru.Server;
import ru.db.objects.Resume;
import ru.db.objects.User;
import ru.dto.resume.ResumeCreateDTO;
import ru.dto.resume.ResumeInfoDTO;
import ru.dto.resume.ResumePreviewDTO;
import ru.dto.resume.ResumeSearchDTO;
import ru.gui.elements.GuiDateObject;
import ru.objects.AuthData;
import ru.objects.Page;
import ru.sio.ResumeSIO;
import ru.utils.enums.*;
import ru.utils.interfaces.ICheckResume;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResumesTable extends DataBaseTable {

    public ResumesTable() {
        //При создании базы данных передаём родительскому классу путь до файла таблицы и заголовок с названиями столбцов.
        //Названия столбцов берём через метод getHeader у класса от аннотаций @Column, которые висят над переменными в классе.
        super(EnumDataPaths.RESUMES_TABLE, Resume.getHeader());
        //Отправляем заголовок таблицы в метод, который проверяет, нужно ли пересобрать таблицу
        rebase(Resume.getHeader());
    }

    private void addResume(Resume resume) {
        appendRecord(data -> {
            if (data != null) {
                Resume lastResume = Server.cm.loadObject(data, Resume.class);
                resume.setId(lastResume.getId()+1);
            } else {
                resume.setId(1);
            }
            return Server.cm.saveObject(resume);
        });
        Server.USERS_TABLE.updateUserAddResumeId(resume.getCreatorId(), resume.getId());
    }

    public Page<ResumePreviewDTO> getResumePage(ResumeSearchDTO dto, int pageSize) {
        ResumeSIO resumeSIO = new ResumeSIO(dto);
        return getPage(resumeSIO, pageSize);
    }

    public Page<ResumePreviewDTO> getAllResumePage(int pageSize) {
        ICheckResume iCheckResume = (resume, user) -> resume.getStatusEnum() == EnumStatus.ACTIVE;
        return getPage(iCheckResume, pageSize);
    }

    public Page<ResumePreviewDTO> getUserResumePage(AuthData authData, int pageSize) {
        User tempUser = Server.USERS_TABLE.getUser(authData);
        if (tempUser == null) return null;

        ICheckResume iCheckResume = (resume, user) -> resume.getStatusEnum() == EnumStatus.ACTIVE && user.getId() == tempUser.getId();
        return getPage(iCheckResume, pageSize);
    }

    public Page<ResumePreviewDTO> getPage(ICheckResume iCheckResume, int pageSize) {
        Page<ResumePreviewDTO> page = new Page<>(pageSize);
        readRecord(data -> {
            if (data != null) {
                Resume resume = Server.cm.loadObject(data, Resume.class);
                User user = Server.USERS_TABLE.getUser(resume.getCreatorId());
                if (iCheckResume.checkResume(resume, user)) {
                    page.add(new ResumePreviewDTO(
                            resume.getId(),
                            resume.getDisplayName(), String.valueOf(resume.getSalary()),
                            GuiDateObject.formatDisplayDate(user.getAge(), 0, 0, false),
                            GuiDateObject.formatDisplayDate(resume.getSumWorkPeriod(), false), resume.getDisplaySchedule(),
                            resume.getDisplayEmploymentType(), resume.getProfessions(), resume.getLastWork(),
                            resume.getMainEducation(), user.getCitizenShips(), resume.getRegion(),
                            user.getLanguages(), resume.getCreationDisplayDate(), resume.getUpdateDisplayDate()
                    ));
                }
            }
        });
        return page;
    }

    public void createResume(User user, ResumeCreateDTO dto) {
        if (user == null) return;

        Resume resume = new Resume();
        resume.setStatus(EnumStatus.ACTIVE.getUnlocalizedName());
        resume.setCreatorId(user.getId());
        resume.setDisplayName(dto.getDisplayName());

        Set<Long> idHashSet = new HashSet<>();
        long id;
        long[] ids;
        List<Long> idList;

        for (String name: dto.getProfessions().split(DataBaseTable.objectArraySeparator)) {
            id = Server.PROFESSION_TABLE.getIdByName(name);
            if (id > 0) idHashSet.add(id);
        }
        idList = new ArrayList<>(idHashSet);
        ids = new long[idList.size()];

        for (int i = 0; i < ids.length; i++) ids[i] = idList.get(i);
        resume.setProfessionsId(ids);
        idHashSet.clear();
        idList.clear();

        resume.setDescription(dto.getDescription());
        resume.setSalary(dto.getSalary());
        resume.setAddress(user.getAddress());
        resume.setEducations(dto.getEducations());
        resume.setWorks(dto.getWorks());

        for (String name: dto.getSkills().split(DataBaseTable.objectArraySeparator)) {
            id = Server.SKILLS_TABLE.getIdByName(name);
            if (id > 0) idHashSet.add(id);
        }
        idList = new ArrayList<>(idHashSet);
        ids = new long[idList.size()];

        for (int i = 0; i < ids.length; i++) ids[i] = idList.get(i);
        resume.setSkillsId(ids);
        idHashSet.clear();
        idList.clear();

        resume.setRecommendations(dto.getRecommendations());
        resume.setEmploymentType(dto.getEmploymentType());
        resume.setSchedule(dto.getSchedule());

        addResume(resume);
    }

    public void createResume(AuthData authData, ResumeCreateDTO dto) {
        createResume(Server.USERS_TABLE.getUser(authData), dto);
    }

    public ResumeInfoDTO getResumeInfo(AuthData authData, long resumeId) {

        User user = Server.USERS_TABLE.getUser(authData);
        if (user == null) return null;

        Resume resume = Server.cm.loadObject(getRecord(data -> {
            if (data != null) {
               return Server.cm.loadObject(data, Resume.class).getId() == resumeId;
            }
            return false;
        }), Resume.class);

        if (resume == null) return null;

        user = Server.USERS_TABLE.getUser(resume.getCreatorId());

        if (user == null) return null;

        return new ResumeInfoDTO(
                resume.getDisplayName(),
                resume.getSalary() + " руб.",
                EnumEmploymentType.getEnum(resume.getEmploymentType()).getDisplayName(),
                EnumSchedule.getEnum(resume.getSchedule()).getDisplayName(),
                GuiDateObject.formatDisplayDate(resume.getSumWorkPeriod(), false),
                user.getLastName() + " " + user.getFirstName() + " " + user.getMiddleName(),
                GuiDateObject.formatDisplayDate(user.getAge(), 0, 0, false),
                EnumSex.getEnum(user.getSex()).getDisplayName(),
                user.getEmail(),
                user.getPhoneNumber(),
                resume.getDisplayAddress(),
                resume.getDescription(),
                resume.getProfessions(),
                resume.getSkills(),
                user.getLanguages(),
                user.getCitizenShips(),
                resume.getEducations(),
                resume.getWorks(),
                resume.getRecommendations()
        );
    }

}
