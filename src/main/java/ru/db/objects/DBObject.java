package ru.db.objects;

import ru.annotations.Column;
import ru.db.DataBaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@NoArgsConstructor
@Data
public class DBObject {

    public static String getFields(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        for (Field field: clazz.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                sb.append(column.name()).append(DataBaseTable.fieldSeparator);
            }
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

}
