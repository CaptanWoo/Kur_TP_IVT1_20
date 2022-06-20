package ru.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Utils {

    public static String arrayToString(String[] data) {
        StringBuilder sb = new StringBuilder();

        for (String d: data) {
            if (d != null) sb.append(d);
            sb.append(';');
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public static boolean hasAnyInArray(long[] array, Set<Long> longSet) {
        if (longSet.size() == 0) return true;
        for (long l: array) {
            if (longSet.contains(l)) return true;
        }
        return false;
    }

    public static boolean hasAllInArray(long[] array, Set<Long> longSet) {
        if (longSet.size() == 0) return true;
        for (long l: longSet) {
            if (!hasInArray(array, l)) return false;
        }
        return true;
    }

    public static boolean hasInArray(long[] array, long value) {
        for (long l: array) {
            if (l == value) return true;
        }
        return false;
    }

    /**
     * Метод, экранирующий символы \ и \n в получаемом из TextArea объектов многострочном тексте
     * Это нужно для того, чтобы в таблице БД не добавлялись ненужные переносы строк данных и
     * таблица не ломалась
     * @param str текст с символами \ и \n из TextArea
     * @return текст с экранированными символами \ и \n
     */
    public static String getFixedDescription(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\\') sb.append("\\\\");
            else if (str.charAt(i) == '\n') sb.append("\\n");
            else sb.append(str.charAt(i));
        }
        return sb.toString();
    }

    /**
     * Метод, снимающий экранирование символов \ и \n, заменяя их изначальными вариантами
     * @param str строка с экранированными символами
     * @return массив строк без экранирования для TextArea
     */
    public static List<String> getNotFixedDescription(String str) {
        if (str == null) return new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        int i = 0;

        char ch;
        while (i < str.length()-1) {
            ch = str.charAt(i);
            if (ch == '\\') {
                ch = str.charAt(i+1);
                if (ch == 'n') {
                    if (sb.length() > 0) {
                        list.add(sb + "\n");
                        sb.setLength(0);
                    } else {
                        list.add("\n");
                    }
                    i++;
                } else if (ch == '\\') {
                    sb.append('\\');
                    i++;
                }
            } else sb.append(ch);
            i++;
        }
        if (i == str.length() - 1) sb.append(str.charAt(i));
        if (sb.length() > 0) list.add(sb.toString());

        return list;
    }

    public static boolean hasId(long[] ids, long id) {
        for (long i: ids) if (id == i) return true;
        return false;
    }

    public static long[] addLongToArray(long[] ids, long id) {
        if (ids.length == 1 && ids[0] == 0) {
            ids[0] = id;
            return ids;
        }
        if (hasId(ids, id)) return ids;
        long[] temp = new long[ids.length+1];
        System.arraycopy(ids, 0, temp, 0, ids.length);
        temp[ids.length] = id;
        return temp;
    }

    public static long[] removeLongFromArray(long[] ids, long id) {
        if (!hasId(ids, id)) return ids;
        if (ids.length == 1) {
            ids[0] = 0;
            return ids;
        }
        long[] temp = new long[ids.length-1];
        int j = 0;
        for (long l : ids) {
            if (l != id) {
                temp[j] = l;
                j++;
            }
        }

        return temp;
    }

}
