package ru.db.objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.annotations.Column;
import ru.utils.enums.EnumStatus;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class Skill extends DBObject {

    @Column(name = "id") private long id;
    @Column(name = "status") private EnumStatus status = EnumStatus.NOT_STATED;
    @Column(name = "displayName") private String displayName;

    public void setStatus(String name) {
        status = EnumStatus.getEnum(name);
    }

    public String getStatus() {
        return status.getUnlocalizedName();
    }

    public static String getHeader() {
        return getFields(Skill.class);
    }
}
