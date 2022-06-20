package ru.utils.objects;

import ru.db.DataBaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Recommendation {

    private String fio;
    private String phoneNumber;
    private int year;
    private String company;
    private String position;
    private String description;

    public Recommendation(String text) {
        load(text);
    }

    public Recommendation(String fio, String phoneNumber, int year, String company, String position, String description) {
        this.fio = fio;
        this.phoneNumber = phoneNumber;
        this.year = year;
        this.company = company;
        this.position = position;
        this.description = description;
    }

    public void load(String text) {
        String[] data = text.split(DataBaseTable.textArraySeparator);
        if (data.length != 6) {
            String[] temp = new String[6];
            System.arraycopy(data, 0, temp, 0, data.length);
            for (int i = 0; i < temp.length; i++) if (temp[i] == null) temp[i] = "";
            data = temp;
        }
        fio = data[0];
        phoneNumber = data[1];
        year = Integer.parseInt("0" + data[2]);
        company = data[3];
        position = data[4];
        description = data[5];
    }

    public String save() {
        return fio + DataBaseTable.textArraySeparator +
                phoneNumber + DataBaseTable.textArraySeparator +
                year + DataBaseTable.textArraySeparator +
                company + DataBaseTable.textArraySeparator +
                position + DataBaseTable.textArraySeparator +
                description;
    }

    public void clear() {
        fio = "";
        phoneNumber = "";
        year = 0;
        company = "";
        position = "";
        description = "";
    }

}
