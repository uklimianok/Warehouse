package com.warehouse.demo.dto.employee;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private long organizationId;
    private long positionId;
    private long shiftId;
    private LocalDate birthDate;
    private String documentId;
    private String residenceAddress;
    private String phoneNumber;
}
