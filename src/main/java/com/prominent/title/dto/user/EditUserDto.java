package com.prominent.title.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDto {

    @Email
    @NotEmpty
    @Schema(example = "otis_osborne@gmail.com")
    private String userName;
    @Schema(example = "Otis")
    private String firstName;
    @Schema(example = "Osborne")
    private String lastName;
    @Schema(hidden = true)
    private String userType;
    @Schema(example = "4389 Hilltop Drive")
    private String address;
    @Schema(example = "Amarillo")
    private String city;
    @Schema(example = "Texas")
    private String state;
    @Schema(example = "79101")
    private String zipCode;
    @Schema(example = "8063725849")
    @Digits(integer = 15, fraction = 0, message = "Please Enter Numeric Values")
    private String contactNumber;
}
