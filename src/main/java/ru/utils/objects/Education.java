package ru.utils.objects;

import ru.db.DataBaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.utils.enums.EnumEducationLevel;

@NoArgsConstructor
@Data
public class Education {

    private EnumEducationLevel level = EnumEducationLevel.NOT_STATED;
    private String institute;
    private String profession;
    private int year;
    private String description;

    public Education(String text) {
        load(text);
    }

    public Education(EnumEducationLevel level, String institute, String profession, int year, String description) {
        this.level = level;
        this.institute = institute;
        this.profession = profession;
        this.year = year;
        this.description = description;
    }

    public void load(String text) {
        String[] data = text.split(DataBaseTable.textArraySeparator);
        if (data.length != 5) {
            String[] temp = new String[5];
            System.arraycopy(data, 0, temp, 0, data.length);
            for (int i = 0; i < temp.length; i++)if (temp[i] == null) temp[i] = "";
            data = temp;
        }
        level = EnumEducationLevel.getEnum(data[0]);
        institute = data[1];
        profession = data[2];
        year = Integer.parseInt("0" + data[3]);
        description = data[4];
    }

    public String save() {
        return level.getUnlocalizedName() + DataBaseTable.textArraySeparator +
                institute + DataBaseTable.textArraySeparator +
                profession + DataBaseTable.textArraySeparator +
                year + DataBaseTable.textArraySeparator +
                description;
    }

    public void clear() {
        level = EnumEducationLevel.NOT_STATED;
        institute = "";
        profession = "";
        year = 0;
        description = "";
    }

}
