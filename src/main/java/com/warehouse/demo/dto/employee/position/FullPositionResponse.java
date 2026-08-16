package com.warehouse.demo.dto.employee.position;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FullPositionResponse extends PositionResponse {
    private String codeName;
    private boolean hasDatabaseAccess;

    public FullPositionResponse(long id, String name, String codeName, boolean hasDatabaseAccess) {
        super(id, name);
        this.codeName = codeName;
        this.hasDatabaseAccess = hasDatabaseAccess;
    }
}
