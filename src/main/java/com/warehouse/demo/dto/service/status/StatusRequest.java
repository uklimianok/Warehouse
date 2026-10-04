package com.warehouse.demo.dto.service.status;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusRequest {
    @NotNull 
    @Size(min = 1, max = 50)
    private String name;

    @NotNull 
    @Size(min = 1, max = 50)
    private String type;
}
