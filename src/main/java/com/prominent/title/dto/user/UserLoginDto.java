package com.prominent.title.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    @Schema(example = "john_doe@gmail.com")
    private String userName;
    @Schema(example = "************")
    private String userPassword;
}
