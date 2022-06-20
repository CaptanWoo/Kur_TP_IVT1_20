package ru.utils.objects;

import ru.db.DataBaseTable;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Data
public class Work {

    private LocalDate startDate = LocalDate.MIN;
    private LocalDate endDate = LocalDate.MIN;
    private String position;
    private String company;
    private String description;

    public Work() {

    }

    public Work(String text) {
        load(text);
    }

    public Work(LocalDate startDate, LocalDate endDate, String position, String company, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.company = company;
        this.description = description;
    }

    public Period getPeriod() {
        return Period.between(startDate, endDate);
    }

    public void load(String text) {
        String[] data = text.split(DataBaseTable.textArraySeparator);
        if (data.length != 5) {
            String[] temp = new String[5];
            System.arraycopy(data, 0, temp, 0, data.length);
            for (int i = 0; i < temp.length; i++) if (temp[i] == null) temp[i] = "";
            data = temp;
        }
        if (!data[0].equals("")) startDate = LocalDate.parse(data[0]);
        if (!data[1].equals("")) endDate = LocalDate.parse(data[1]);
        position = data[2];
        company = data[3];
        description = data[4];
    }

    public String save() {
        return startDate.toString() + DataBaseTable.textArraySeparator +
                endDate.toString() + DataBaseTable.textArraySeparator +
                position + DataBaseTable.textArraySeparator +
                company + DataBaseTable.textArraySeparator +
                description;
    }

    public void clear() {
        startDate = LocalDate.MIN;
        endDate = LocalDate.MIN;
        this.position = "";
        this.company = "";
        this.description = "";
    }

}
