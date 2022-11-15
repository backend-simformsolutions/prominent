package com.prominent.title.dto.organization;

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
public class OrganizationDto {

    private int organizationId;

    @Schema(example = "Thurston Wyatt real Estate LLC.")
    @NotEmpty(message = "Organization Name Cannot Be Empty")
    private String organizationName;

    private String organizationTypeName = "Entity";

    @Schema(example = " New York")
    @NotEmpty(message = "Alice street")
    private String address;

    @Schema(example = "New york")
    @NotEmpty(message = "City Cannot Be Empty")
    private String city;

    @Schema(example = "New York")
    @NotEmpty(message = "State Cannot Be Empty")
    private String state;

    @Schema(example = "10001")
    @NotEmpty(message = "ZipCode Cannot Be Empty")
    private String zipCode;

    @Schema(example = "Jakob Vaccaro")
    @NotEmpty(message = "Managing Broker Name Cannot Be Empty")
    private String primaryContactName;

    @Schema(example = "2025550162")
    @Digits(integer = 15, fraction = 0, message = "Please Enter Numeric Values")
    private String primaryContactPhone;

    @Schema(example = "thru@gmail.com")
    @Email
    private String primaryContactEmail;

    @Schema(example = "30-2232116")
    @NotEmpty(message = "Managing Broker Name Cannot Be Empty")
    private String brokerageLicenseNumber;

    @Schema(example = "2")
    @NotEmpty(message = "Brokerage Admin Fee Amount Broker Name Cannot Be Empty")
    @Digits(integer = 20, fraction = 0, message = "Please Enter Numeric Values")
    private String brokerageAdminFeeAmount;

}