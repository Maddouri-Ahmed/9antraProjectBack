package tn.esprit.PFE.utils;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ForgotPasswordDto {
    @NotBlank
    private String email;
}
