package ru.utils.interfaces;

import ru.db.objects.Resume;
import ru.db.objects.User;

public interface ICheckResume {

    boolean checkResume(Resume resume, User user);

}
