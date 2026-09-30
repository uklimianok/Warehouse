package com.warehouse.demo.dto.employee.position;

import java.util.List;
import java.util.Map;

import com.warehouse.demo.entity.employee.Position;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullPositionResponse extends PositionResponse {
    private String codeName;
    private boolean isEnabled;
    private List<Position> inheritedPositions;
    private boolean isRemovable;
    private Map<String, String> controllerAccessFlags;

    public FullPositionResponse(long id, String name, String codeName, boolean isEnabled, List<Position> inheritedPositions, boolean isRemovable, Map<String, String> controllerAccessFlags) {
        super(id, name);
        this.codeName = codeName;
        this.isEnabled = isEnabled;
        this.inheritedPositions = inheritedPositions;
        this.isRemovable = isRemovable;
        this.controllerAccessFlags = controllerAccessFlags;
    }
}
