package ru.controllers;

import javafx.scene.control.TextField;

import java.util.*;

public class Validator {

    //Набор уникальных разрешённых в логине почты символов
    private static Set<Character> emailLegalChars = initEmailLegalCharset();

    /**
     * Метод очистки тексового поля для ввода Email от запрещённых символов.
     * Вызывается каждый раз при вводе символа в поле ввода.
     * @param tf текстовое поля для ввода логина email
     */
    public static void cleanUpEmail(TextField tf) {
        //Создаём буфер для обработки символов из текстового поля
        StringBuilder sb = new StringBuilder();
        //Флаг-переменная, нужно ли смещать каретку ввода символов влево после очистки строки
        boolean needToMoveCaret = false;
        char ch;
        //Получаем текст из текстового поля
        String s = tf.getText();
        //Проходим по всем символам в строке
        for (int i = 0; i < s.length(); i++) {
            //Получаем i-тый символ строки
            ch = s.charAt(i);
            //Если символ разрешённый, то делаем дополнительные проверки и записываем его в буфер
            if (emailLegalChars.contains(ch)) {
                //В логине почты первый символ не может быть точкой. Проверяем это тут и записываем символы в буфер
                if (i == 0) {
                    if (ch != '.') sb.append(ch);
                } else sb.append(ch);
            } else needToMoveCaret = true;
        }
        //Вызываем метод записи в поле ввода очищенных символов из буфера
        replaceTFText(tf, sb.toString(), needToMoveCaret);
    }

    /**
     * Метод валидации Email.
     * @param email логин почты
     * @return проверенная строка с логином почты
     * @throws Exception ошибка в случае некорректного email
     */
    public static String validateEmail(String email) throws Exception {
        //Дели строку на несколько строк по символу @
        String[] data = email.split("@");
        //Если строка поделилась не на две части (в строке на один символ @), то выкидываем ошибку
        if (data.length != 2) throw new Exception("Введён некорректный email.");
        //Если всё в порядке, то в цикле бежим по полученным двум частям строки
        for (String s: data) {
            //Бежим по символам части строки
            for (int i = 0; i < s.length(); i++) {
                //Если символа нет в наборе разрешённых символов, то выкидываем ошибку
                if (!emailLegalChars.contains(s.charAt(i))) throw new Exception("Email содержит запрещённые символы.");
            }
        }
        //Возвращаем объединённую из двух частей строку, приведя все символы в нижний регистр
        return data[0].toLowerCase() + "@" + data[1].toLowerCase();
    }

    /**
     * Метод очистки тексового поля для ввода номера телефона от запрещённых символов.
     * Вызывается каждый раз при вводе символа в поле ввода.
     * @param tf текстовое поля для ввода номера телефона
     */
    public static void cleanUpPhoneNumber(TextField tf) {
        //Создаём буффер для обработки символов из текстового поля
        StringBuilder sb = new StringBuilder();
        //Флаг-переменная, нужно ли смещать каретку ввода символов влево после очистки строки
        boolean needToMoveCaret = false;
        char ch;
        //Получаем текст из текстового поля
        String s = tf.getText();
        //Проходим по всем символам в строке
        for (int i = 0; i < s.length(); i++) {
            //Получаем i-тый символ строки
            ch = s.charAt(i);
            //Если под номером 0 символ '+' или это цифра (48-57 это код цифр) и индекс символа меньше 12,
            //то записываем его в буфер
            if ((ch == '+' && i == 0 || ch > 47 && ch < 58) && i < 12) sb.append(ch);
            //Иначе это запрещённый символ, мы его не записываем и каретку нужно перенести на 1 символ влево
            else needToMoveCaret = true;
        }
        //Если в буфере нет символов, то добавляем туда символ '+'
        if (sb.length() == 0) sb.append('+');
        //Если в буфере нет символа и первый символ не '+', то вставляем в буфер перед первым символом символ '+'
        else if (sb.charAt(0) != '+') {
            s = sb.toString();
            sb.setLength(0);
            sb.append('+').append(s);
        }
        //Вызываем метод записи в поле ввода очищенных символов из буфера
        replaceTFText(tf, sb.toString(), needToMoveCaret);
    }

    /**
     * Метод замены текста в текстовом поле с установкой каретки ввода в нужное место
     * @param tf текстовое поле, в которое нужно записать строку
     * @param text строка, которую нужно записать в текстовое поле
     * @param needToMoveCaret нужно ли сдвигать картерку ввода на один символ влево
     */
    private static void replaceTFText(TextField tf, String text, boolean needToMoveCaret) {
        //Получаем текущую позицию каретки
        int i = tf.getCaretPosition();
        //Записываем очищенный текст в поле ввода
        tf.setText(text);
        //Если надо сместить каретку, то смещаем на 1 символ влево
        if (needToMoveCaret) tf.positionCaret(i-1);
        //Иначе оставляем каретку на старом месте
        else tf.positionCaret(i);
    }

    /**
     * Метод валидации номера телефона
     * @param phoneNumber строка с номером телефона
     * @return проверенная строка с номером телефона
     */
    public static String validatePhoneNumber(String phoneNumber) {
        return phoneNumber;
    }

    /**
     * Валидация пароля (сравнение первого и второго ввода пароля)
     * @param password первый ввод пароля
     * @param password2 второй ввод пароля
     * @return пароль, если он совпал
     * @throws Exception ошибка если пароль не совпал
     */
    public static String validatePassword(String password, String password2) throws Exception {
        if (!password.equals(password2)) throw new Exception("Пароль не совпадает.");
        return password;
    }

    /**
     * Метод инициализации (заполнения) набора разрешённых для логина почты символов
     * @return набор разрешённых символов
     */
    private static Set<Character> initEmailLegalCharset() {
        Set<Character> set = new HashSet<>();
        for (int i = 48; i <= 57; i++) set.add((char)i); //Символы 0-9
        for (int i = 65; i <= 90; i++) set.add((char)i); //Символы A-Z
        for (int i = 97; i <= 122; i++) set.add((char)i); //Символы a-z
        set.add('@');
        set.add('.');
        return set;
    }
}
