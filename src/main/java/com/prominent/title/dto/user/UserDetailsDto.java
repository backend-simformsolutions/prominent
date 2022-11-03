package com.prominent.title.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {

    @Schema(example = "1")
    private int userId;
    @Schema(example = "theodore_white@gmail.com")
    private String userName;
    @Schema(example = "Theodore")
    private String firstName;
    @Schema(example = "White")
    private String lastName;
    @Schema(hidden = true)
    private String userType;
    @Schema(example = "4312 Ashford Drive")
    private String address;
    @Schema(example = "Washington")
    private String city;
    @Schema(example = "Virginia")
    private String state;
    @Schema(allowableValues = {"Admin", "Broker", "User", "Lender", "Notary", "Title Abstractor", "Surveyor"})
    private String roleCode;
    @Schema(example = "martin_herrera@gmail.com")
    private String zipCode;
    @Schema(example = "8128311625")
    private String contactNumber;
}