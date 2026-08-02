package com.warehouse.demo.dto.employee.position;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FullPositionResponse extends PositionResponse {
    private String codeName;

    public FullPositionResponse(long id, String name, String codeName) {
        super(id, name);
        this.codeName = codeName;
    }
}
