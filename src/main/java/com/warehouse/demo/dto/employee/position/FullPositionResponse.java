package com.warehouse.demo.dto.employee.position;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullPositionResponse extends PositionResponse {
    private String codeName;
    private boolean isEnabled;

    public FullPositionResponse(long id, String name, String codeName, boolean isEnabled) {
        super(id, name);
        this.codeName = codeName;
        this.isEnabled = isEnabled;
    }
}
