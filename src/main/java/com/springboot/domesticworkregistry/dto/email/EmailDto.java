package com.springboot.domesticworkregistry.dto.email;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {

    @NotEmpty
    private List<@Email String> to = new ArrayList<>();

    @NotBlank
    private String subject;

    @NotBlank
    private String text;
}
