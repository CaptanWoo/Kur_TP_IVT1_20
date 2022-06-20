package ru.utils.objects;

import lombok.Getter;
import lombok.Setter;
import ru.db.DataBaseTable;

@Setter
@Getter
public class ContactInfo {

    private String fio;
    private String email;
    private String phoneNumber;
    private String description;

    public ContactInfo() {
        this.fio = "";
        this.email = "";
        this.phoneNumber = "";
        this.description = "";
    }

    public ContactInfo(String data) {
        load(data);
    }

    public ContactInfo(String fio, String email, String phoneNumber, String description) {
        this.fio = fio;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }

    public void load(String text) {
        String[] data = text.split(DataBaseTable.textArraySeparator);
        if (data.length != 4) {
            String[] temp = new String[4];
            System.arraycopy(data, 0, temp, 0, data.length);
            for (int i = 0; i < temp.length; i++) if (temp[i] == null) temp[i] = "";
            data = temp;
        }
        fio = data[0];
        email = data[1];
        phoneNumber = data[2];
        description = data[3];
    }

    public String save() {
        return fio + DataBaseTable.textArraySeparator +
                email + DataBaseTable.textArraySeparator +
                phoneNumber + DataBaseTable.textArraySeparator +
                description;
    }

    public void clear() {
        fio = "";
        email = "";
        phoneNumber = "";
        description = "";
    }
}
