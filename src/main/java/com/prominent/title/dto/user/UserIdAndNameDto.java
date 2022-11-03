package com.prominent.title.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdAndNameDto {

    @Schema(example = "10")
    private int userId;
    @Schema(example = "John")
    private String firstName;
    @Schema(example = "Doe")
    private String lastName;
    @Schema(example = "john_doe@gmail.com")
    private String userName;
    @Schema(example = "8605588552")
    private String phoneNumber;
}
