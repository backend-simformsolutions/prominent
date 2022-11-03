package com.prominent.title.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInformationDto {

    @Schema(allowableValues = {"Wire", "Cash"})
    @NotEmpty(message = "PaymentMethod Cannot Be Empty")
    private String paymentMethod;

    @Schema(example = "John Doe")
    @NotEmpty(message = "RecipientName Cannot Be Empty")
    @Size(max = 20)
    private String recipientName;

    @Schema(example = "2472 Thunder Road")
    @NotEmpty(message = "Address Cannot Be Empty")
    @Size(max = 100)
    private String address;

    @Schema(example = "Anchorage")
    @NotEmpty(message = "City Cannot Be Empty")
    private String city;

    @Schema(example = "Anchorage")
    @NotEmpty(message = "State Cannot Be Empty")
    private String state;

    @Schema(example = "99501")
    @NotEmpty(message = "Zip Cannot Be Empty")
    @Size(max = 10)
    private String zipCode;

    @Schema(example = "123456789101112")
    @NotEmpty(message = "Bank Account Number Cannot Be Empty")
    @Size(max = 15, min = 15)
    private String bankAccountNumber;

    @Schema(example = "1020304050")
    @NotEmpty(message = "Routing Number Cannot Be Empty")
    @Digits(integer = 20, fraction = 0, message = "Please Enter Numeric Values")
    private String routingNumber;
}
