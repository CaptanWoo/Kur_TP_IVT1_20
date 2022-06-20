package ru.dto.control;

import lombok.Getter;

@Getter
public class CommonObjectDTO {

    private final long id;
    private final String status, displayName;

    public CommonObjectDTO(long id, String status, String displayName) {
        this.id = id;
        this.status = status;
        this.displayName = displayName;
    }
}
