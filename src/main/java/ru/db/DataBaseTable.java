package ru.db;

import ru.managers.FileManager;
import ru.utils.enums.EnumDataPaths;
import ru.utils.interfaces.ICheckable;
import ru.utils.interfaces.IExecutable;
import ru.utils.interfaces.IProcessable;
import ru.utils.interfaces.IReturnString;

import java.io.*;
import java.util.*;

public class DataBaseTable {

    /**Хеш-сеты для возможности получить отображаемое имя записи по id или id по отображаемому имени.
    * Это нужно для того, чтобы можно было очень быстро получать значения из баз данных с малым числом записей.
    * К примеру, языков, гражданств и навыков. Их нужно получать очень часто, поэтому каждый раз полностью
    * проверять файл таблицы не имеет смысла, так как это требует много времени и ресурсов
    **/
    protected final Map<Long, String> idToNameMap = new HashMap<>();
    protected final Map<String, Long> nameToIdMap = new HashMap<>();

    public static final String fieldSeparator = ";"; //Символ-разделитель столбцов в таблице БД
    public static final String numberArraySeparator = ","; //Символ-разделитель массива чисел
    public static final String objectArraySeparator = "♦"; //alt+4 //Символ-разделитель массива объектов
    public static final String textArraySeparator = "█"; //alt+219 //Символ-разделитель текста
    private final EnumDataPaths bdPath; //Путь до файла с таблицей БД

    public DataBaseTable(EnumDataPaths bdPath, String header) {
        this.bdPath = bdPath;
        init(header);
    }

    public void init(String header) {
        //Если файла с таблицей БД не существует
        if (!FileManager.isFileExists(bdPath.getPath())) {
            //Создаем файл и закидываем в него заголовок от класса с данными, который должен храниться в таблице
            appendRecord(data -> header);
        }
    }

    /**
     * Метод получения отображаемых_имён объектов из хеш-сета.
     * Обычно используется для того, чтобы отобразить пользователю список вариантов (языков, гражданств и т.д.)
     * @return коллекция (грубо говоря список) отображаемых имён
     */
    public List<String> getHashNames() {
        List<String> list = new ArrayList<>(nameToIdMap.keySet());
        Collections.sort(list);
        return list;
    }

    /**
     * Метод сборки id из хеш-сета в строку данных для таблицы БД с использованием буфера.
     * @param idHashSet хеш-сет id
     * @param sb буфер
     * @return строка данных
     */
    protected String assembleIds(Set<Long> idHashSet, StringBuilder sb) {
        sb.setLength(0);
        for (long lid: idHashSet) sb.append(lid).append(DataBaseTable.numberArraySeparator);
        idHashSet.clear();
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    /**
     * Метод получения отображаемого имени объекта в бд по его id
     * @param id объекта
     * @return отображаемое имя объекта
     */
    public String getNameById(long id) {
        return idToNameMap.get(id);
    }

    public long getIdByName(String name) {
        return nameToIdMap.getOrDefault(name, 0L);
    }

    public Set<Long> getIds() {
        return idToNameMap.keySet();
    }

    /**
     * Метод, который автоматически перестраивает таблицу базы данных, если в ней не хватает каких-либо столбцов
     * с данными или если они стоят в неправильном порядке.
     *
     * Если кратко, то алгоритм работает следующим образом:
     * 1. Разделяет столбцы из строки newFields по символу разделителю столбцов;
     * 2. Получает столбцы из текущей таблицы и делит их по символу разделителю столбцов;
     * 3. Находит совпадающие столбцы и создает ссылки (к примеру, 5 новый столбец это 3 старый).
     *    Если совпадающий столбец не найден, то значение ссылки равно 0 и это значит, что этот столбец нужно создать;
     * 4. Вызывает метод rewrite, которому отправляет следующие инструкции:
     *    4.1. Создай буфер для символов
     *    4.2. Замени первую строку старого заголовка новым заголовком
     *    4.3. Цикл по строкам файла:
     *       4.3.1. Прочитай строку.
     *       4.3.2. Раздели её по символу-разделителю столбцов таблицы в массив старых значений;
     *       4.3.3. Создай новый массив для значений строки по столбцам таблицы;
     *       4.3.4. Переноси из массива старых значений данный в массив новых значений по значению ссылки из пункта 3;
     *       4.3.5. Запиши значения из массива новых значений в буфер символов, добавляя разделители столбцов;
     *       4.3.6. Запиши данные из буфера в файл;
     *       4.3.7. Очисти буфер;
     *    4.4. Сообщи о том, что таблица была перестроена
     *
     * @param newFields строка с примером того, какие столбцы и в каком порядке должны находиться в таблице
     */
    public void rebase(String newFields) {
        String oldFields = readHeader();
        if (oldFields == null) {
            appendRecord(data -> newFields);
            return;
        } else if (oldFields.equals(newFields)) return;

        System.out.println("DataBase " + bdPath.getPath() + " need to be rebased");
        String[] oldArr = oldFields.split(fieldSeparator);
        String[] newArr = newFields.split(fieldSeparator);

        int[] links = new int[oldArr.length];

        //Создание ссылок между старыми столбцами и новыми
        for (int o = 0; o < oldArr.length; o++) {
            for (int n = 0; n < newArr.length; n++) {
                if (oldArr[o].equals(newArr[n])) {
                    links[o] = n + 1;
                    break;
                }
            }
        }

        rewrite(((reader, writer) -> {
            String line;
            StringBuilder sb = new StringBuilder();
            reader.readLine();
            writer.write(newFields + '\n');
            while ((line = reader.readLine()) != null) {
                String[] input = line.split(DataBaseTable.fieldSeparator);
                String[] output = new String[newArr.length];

                //Перенос данных из массива старых данных в массив новых данных по ссылкам
                for (int i = 0; i < input.length; i++) {
                    if (links[i] > 0) output[links[i] - 1] = input[i];
                }
                //Запись данных из массива новых данных в буфер
                for (String s : output) {
                    if (s != null) sb.append(s);
                    sb.append(DataBaseTable.fieldSeparator);
                }
                //Записываем данные из буфера в файл
                sb.setLength(sb.length()-1);
                writer.write(sb.toString() + '\n');
                sb.setLength(0);
            }
            System.out.println("DataBase " + bdPath.getPath() + " rebased");
        }));

    }

    /**
     * Метод получения заголовка таблицы из файла таблицы
     * @return заголовок таблицы
     */
    public String readHeader() {
        //Открываем файл
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(bdPath.getPath())))) {
            return reader.readLine();
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла БД " + bdPath.getPath());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Метод добавления записи в строку. Получает на вход лямбда-выражение, в котором хранятся нужные инструкции для записи.
     * @param function лямдба-выражение с инструкциями
     */
    public void appendRecord(IReturnString function) {
        rewrite((reader, writer) -> {
            String line, data = null;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                writer.write(line + '\n');
                if (isHeader) isHeader = false;
                else data = line;
            }
            writer.write(function.getString(data) + '\n');
        });
    }

    /**
     * Метод для выполнения действий, логика которых записана в получаемом лямбда-выражении, для всех строк таблицы
     * @param iExecutable лямбда-выражение с инструкциями
     */
    public void readRecord(IExecutable iExecutable) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(bdPath.getPath())))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) iExecutable.execute(line);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла БД " + bdPath.getPath());
            e.printStackTrace();
        }
    }

    /**
     * Метод получения определённой строки из файла таблицы, которая выбирается по инструкциям из переданного лямбда-выражения.
     * @param iCheckable лямбда-выражение с инструкциями о том, какую запись нужно достать
     * @return запись
     */
    public String getRecord(ICheckable iCheckable) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(bdPath.getPath())))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (iCheckable.check(line)) {
                    reader.close();
                    return line;
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла БД " + bdPath.getPath());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Метод для проверки данных в таблице. Логика проверки данных находится в получаемом лямбда-выражении.
     * @param iCheckable лямбда-выражение с проверкой данных
     * @return возвращает true, если условия лямбда-выражения выполнились и false, если нет
     */
    public boolean checkRecord(ICheckable iCheckable) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(bdPath.getPath())))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (iCheckable.check(line)) {
                    reader.close();
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла БД " + bdPath.getPath());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Метод замены записи в файле таблицы. Получает на вход лямбда-выражение с инструкциями, которые нужно выполнить
     * для каждой строки (внутри выражения находится цикл прохода по срокам данных, проверки данных, замена данных и т.д.)
     * @param processable лямдба-выражение с инструкциями и логикой замены данных
     */
    public void rewrite(IProcessable processable) {
        //Если файл таблицы не существует, то его надо создать
        FileManager.createFileIfNotExists(bdPath.getPath());

        //Открываем старый файл таблицы
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(bdPath.getPath())))) {
            //Создаём временный файл таблицы (в Java нельзя одновременно считывать и записывать данный в одном файле как в С++)
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(bdPath.getPath() + ".temp"))) {

                //Выполняем лямбда-выражение с инструкциями
                processable.execute(reader, writer);

                //Закрываем старый и временный файл после окончания работы с ними
                reader.close();
                writer.close();

                File file = new File(bdPath.getPath());
                if (file.delete()) { //Удаляем старый файл
                    file = new File(bdPath.getPath() + ".temp");
                    File newFile = new File(bdPath.getPath());
                    //Переименовываем временный файл на имя старого файла (основное имя таблицы)
                    if (!file.renameTo(newFile)) System.err.println("Произошла ошибка при переименовании файла БД " + bdPath.getPath() + ".temp");
                } else System.err.println("Произошла ошибка при удалении файла БД" + bdPath.getPath());

            } catch (IOException e) {
                System.err.println("Ошибка при создании или закрытии файлов БД " + bdPath.getPath());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла БД " + bdPath.getPath());
            e.printStackTrace();
        }

    }
}
