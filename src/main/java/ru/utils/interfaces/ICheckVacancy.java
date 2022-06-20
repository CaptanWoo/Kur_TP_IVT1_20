package ru.utils.interfaces;

import ru.db.objects.User;
import ru.db.objects.Vacancy;

public interface ICheckVacancy {

    boolean checkVacancy(Vacancy vacancy, User user);
}
