package com.warehouse.demo.dto.employee.position;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullPositionResponse extends PositionResponse {
    private String codeName;
    private boolean isEnabled;
    private List<PositionResponse> inheritedPositions;
    private boolean isRemovable;
    private Map<String, List<String>> controllerFlags;

    public FullPositionResponse(long id, String name, String codeName, boolean isEnabled, List<PositionResponse> inheritedPositions, boolean isRemovable, Map<String, List<String>> controllerFlags) {
        super(id, name);
        this.codeName = codeName;
        this.isEnabled = isEnabled;
        this.inheritedPositions = inheritedPositions;
        this.isRemovable = isRemovable;
        this.controllerFlags = controllerFlags;
    }
}
