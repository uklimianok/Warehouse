package com.warehouse.demo.dto.employee.position;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PositionRequest {
    @NotBlank 
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    private Boolean enabled;

    @NotNull
    @Positive 
    private Long departmentId;

    @NotNull 
    private List<@NotNull @Positive Long> inheritedPositionsId;

    @NotNull 
    private Map<@NotBlank String, @NotNull List<String>> controllerFlags;
}
