package com.warehouse.demo.dto.employee;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
public class EmployeeRequest {
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @NotNull 
    @Positive 
    private Long employerOrganizationId;

    @NotNull
    @Positive 
    private Long positionId;

    @NotNull
    @Positive 
    private Long shiftId;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotBlank 
    @Size(min = 1, max = 50)
    private String documentId;

    @Size(min = 5, max = 100)
    private String residenceAddress;
    
    @Size(min = 2, max = 10)
    private String phoneNumber;
    
    @Positive 
    private Long workshopId;
    
    @Positive 
    private Long gateId;
}
