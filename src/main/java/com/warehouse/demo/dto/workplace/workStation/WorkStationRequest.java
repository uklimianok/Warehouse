package com.warehouse.demo.dto.workplace.workStation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class WorkStationRequest {
    @NotBlank 
    @Size(min = 1, max = 20)
    private String stationNumber;

    @NotBlank 
    @Size(min = 1, max = 5)
    private String controlNumber;

    @NotBlank 
    @Pattern(regexp = "[Aa]ctive|[Pp]assive")
    private String type;

    @NotNull 
    @Positive 
    private Long workshopId;
}
