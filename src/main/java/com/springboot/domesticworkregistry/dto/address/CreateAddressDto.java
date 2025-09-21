package com.springboot.domesticworkregistry.dto.address;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressDto {

    @NotNull(message = "is required")
    private String street;

    @NotNull(message = "is required")
    private String number;

    @NotNull(message = "is required")
    private String city;

    @NotNull(message = "is required")
    private String postalCode;
    
    @NotNull(message = "is required")
    private String country;
}
