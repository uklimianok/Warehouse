package com.warehouse.demo.dto.employee.organization;

import jakarta.validation.constraints.Email;
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
public class OrganizationRequest {
    @NotBlank 
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank 
    @Size(min = 3, max = 30)
    private String organizationNumber;

    @NotNull
    @Positive 
    private long organizationTypeId;

    @Size(min = 5, max = 100)
    private String address;

    @Size(min = 3, max = 10)
    private String phoneNumber;

    @Email 
    private String email;
    
    @Size(min = 3, max = 50)
    private String url;
}
