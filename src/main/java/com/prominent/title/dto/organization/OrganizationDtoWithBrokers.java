package com.prominent.title.dto.organization;

import com.prominent.title.dto.user.UserIdAndNameDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDtoWithBrokers {

    private OrganizationDto organizationDto;

    private PaymentInformationDto paymentInformationDto;

    private List<UserIdAndNameDto> organizationBrokers;

}
