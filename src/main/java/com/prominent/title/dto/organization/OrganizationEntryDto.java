package com.prominent.title.dto.organization;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationEntryDto {
    @NotNull
    @Valid
    private OrganizationDto organizationDto;

    @NotNull
    @Valid
    private PaymentInformationDto paymentInformationDto;
}
