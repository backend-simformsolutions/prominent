package com.prominent.title.dto;


import com.prominent.title.annotation.ResetPasswordConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ResetPasswordConstraint
public class ResetPasswordDto {
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJJc3N1ZXIifQ.HLkw6rgYSwcv0sE69OKiNQFvHoo-6VqlxC5nKuMmftg")
    String token;
    @Schema(example = "************")
    private String password;
    @Schema(example = "************")
    private String repeatPassword;
}
