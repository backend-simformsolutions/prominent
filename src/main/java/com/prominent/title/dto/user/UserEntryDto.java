package com.prominent.title.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntryDto {
    @Schema(example = "brandon_mcdaniel@gmail.com")
    private String userName;
    @Schema(example = "Brandon")
    private String firstName;
    @Schema(example = "Mcdaniel")
    private String lastName;
    @Schema(example = "1020301020")
    private String contactNumber;
    @Schema(example = "1970 Glen Street")
    private String address;
    @Schema(example = "Eddyville")
    private String city;
    @Schema(example = "Kentucky")
    private String state;
    @Schema(example = "42038")
    private String zipCode;
    @Schema(example = "General User")
    private String organizationName;
    @Schema(allowableValues = {"Admin", "Broker", "User", "Lender", "Notary", "Title Abstractor", "Surveyor"})
    private String roleCode;
}
