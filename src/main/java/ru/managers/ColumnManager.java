package ru.managers;

import ru.annotations.Column;
import ru.db.DataBaseTable;
import ru.db.objects.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ColumnManager {

    /**
     * Хеш-карты хеш-карты методов установки значений (Setters) и получения значений (Getters)
     *
     * Пример структуры:
     *
     *      Map:
     *      |-- User   =  Map:
     *      |             |-- 1 = setter1()
     *      |             |-- 2 = setter2()
     *      |-- Company = Map:
     *                    |-- 1 = setter1()
     *                    |-- 2 = setter2()
     */
    private final Map<Class<?>, Map<Integer, Method>> settersMap = new HashMap<>();
    private final Map<Class<?>, Map<Integer, Method>> gettersMap = new HashMap<>();

    /*
    * При создании ColumnManager регистрируем классы по типу User в хеш-карты сеттеров и геттеров, чтобы потом с их
    * помощью с помощью рефлексии автоматически загрузать и сохранять объекты этих типов в таблицы базы данных
     */
    public ColumnManager() {
        registerClass(CitizenShip.class, CitizenShip.getHeader());
        registerClass(Company.class, Company.getHeader());
        registerClass(Language.class, Language.getHeader());
        registerClass(Skill.class, Skill.getHeader());
        registerClass(Profession.class, Profession.getHeader());
        registerClass(Resume.class, Resume.getHeader());
        registerClass(User.class, User.getHeader());
        registerClass(Vacancy.class, Vacancy.getHeader());
        registerClass(CompanyType.class, CompanyType.getHeader());
    }

    /**
     * Метод регистрации сеттеров и геттеров класса clazz по заголовку таблицы (его получаем из объекта через getHeader())
     * @param clazz класс объекта (User, Company и т.п.)
     * @param header заголовок столбцов таблицы с этим объектом
     * @return результат регистрации
     */
    public boolean registerClass(Class<?> clazz, String header) {
        //Разделяем заголовок по символу-разделителю таблиц базы данных
        String[] headers = header.split(DataBaseTable.fieldSeparator);
        //Получаем список полей объекта и идём по ним в цикле
        for (Field field: clazz.getDeclaredFields()) {
            //Получаем аннотацию над полем (аннотация @Column)
            Column annotation = field.getAnnotation(Column.class);
            //Если есть аннотация
            if (annotation != null) {
                /*То получаем из её значения названия сеттеров и геттеров
                * Пример:
                *   1. Над полем находится аннотация @Column("name"). Получаем её параметр "name";
                *   2. Через метод substring делаем из него строку "Name";
                *   3. Получаем название сеттера как "set" + "Name" = "setName";
                *   4. Получаем название геттера как "get" + "Name" = "getName";
                */
                String fieldPostfix = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                String setterName = "set" + fieldPostfix;
                String getterName = "get" + fieldPostfix;
                try {
                    Method setter;
                    //Если поле примитивного типа (int, float, char и т.д.) или String то создаём объект сеттера
                    if (field.getType().isPrimitive() || field.getType().equals(String.class)) {
                        setter = clazz.getDeclaredMethod(setterName, field.getType());
                    } //Если поле какого-то другого кастомного типа, то делаем что этот сеттер принимает значения типа String
                    else setter = clazz.getDeclaredMethod(setterName, String.class);
                    //Создаём объект геттера (получения) значения
                    Method getter = clazz.getDeclaredMethod(getterName);

                    //Получаем значение аннотации (к примеру "name")
                    String headerName = annotation.name();
                    //Получаем индекс этого столбца
                    int headerIndex = Arrays.asList(headers).indexOf(headerName);

                    //Получаем хеш-карты сеттеров и геттеров из глобальных хеш-карт по классу объекта
                    Map<Integer, Method> setters = settersMap.getOrDefault(clazz, new HashMap<>());
                    Map<Integer, Method> getters = gettersMap.getOrDefault(clazz, new HashMap<>());

                    //Если в хеш-карте уже есть метод с таким индексом, то заканчиваем регистрацию
                    if (setters.containsKey(headerIndex)) return false;
                    if (getters.containsKey(headerIndex)) return false;

                    //Если нет, то добавляем методы в хеш-карты
                    setters.put(headerIndex, setter);
                    getters.put(headerIndex, getter);
                    //Добавляем хеш-карты методов в глобальные хеш-карты по ключу класса
                    settersMap.put(clazz, setters);
                    gettersMap.put(clazz, getters);
                }
                catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * Метод загрузки объекта из данных строки из таблицы БД.
     * @param line скрока с данными
     * @param clazz класс объекта типа T
     * @param <T> (класс объекта напрямую не указан, мы можем отправлять сюда такой объект, который будет указан ранее)
     * @return объект класса T
     */
    public <T> T loadObject(String line, Class<T> clazz) {
        T instance = null;
        try {
            //Получаем конструктор класса T
            Constructor<T> constructor = clazz.getConstructor();
            //Создаём новый объект этого класса
            instance = constructor.newInstance();
            //Разбиваем значения строки с данными на массив данных по символу-разделителю
            String[] values = line.split(DataBaseTable.fieldSeparator);
            //Получаем сеттеры (методы получения значений)
            Map<Integer, Method> integerMethodMap = settersMap.get(clazz);

            //Идём в цикле по сеттерам
            for(Map.Entry<Integer, Method> entry : integerMethodMap.entrySet()) {
                //Получаем индекс сеттера
                int key = entry.getKey();
                String value;
                //Получаем значение из массива данных по индексу сеттера
                if (key < values.length) value = values[key];
                else value = "";

                System.out.println(entry);

                //Вызываем сеттеры и передаём им полученное значение из массива данных (то есть заполняем объект данными)
                switch (entry.getValue().getParameterTypes()[0].getTypeName()) {
                    case "int": entry.getValue().invoke(instance, Integer.parseInt(0 + value)); break;
                    case "long": entry.getValue().invoke(instance, Long.parseLong(0 + value)); break;
                    default: entry.getValue().invoke(instance, value); break;
                }

            }

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //Возвращаем заполненный (загруженный) объект
        return instance;
    }

    /**
     * Метод сохранения объекта в строку с данными для записи в таблицу БД
     * @param object объект, который нужно записать
     * @return строка с данными для записи с таблицу БД
     */
    public String saveObject(DBObject object) {
        try {
            //Создаём буфер для данных
            StringBuilder sb = new StringBuilder();
            //Получаем геттеры объекта из глобальной карты геттеров
            Map<Integer, Method> integerMethodMap = gettersMap.get(object.getClass());

            //Идём по геттерам объекта
            for(Map.Entry<Integer, Method> entry : integerMethodMap.entrySet()) {
                //Записываем данные из объекта в буфер и добавляем символ-разделитель
                sb.append(entry.getValue().invoke(object)).append(DataBaseTable.fieldSeparator);
            }
            //Убираем лишний символ-разделитель в конце
            sb.setLength(sb.length()-1);

            //Получаем готовую строку с данными из буфера и возвращаем её
            return sb.toString();

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
