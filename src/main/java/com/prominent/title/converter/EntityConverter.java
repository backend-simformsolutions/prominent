package com.prominent.title.converter;

import com.prominent.title.dto.organization.OrganizationDto;
import com.prominent.title.dto.organization.OrganizationListDto;
import com.prominent.title.dto.organization.PaymentInformationDto;
import com.prominent.title.dto.resource.GeneralAddressDto;
import com.prominent.title.dto.user.*;
import com.prominent.title.entity.resource.GeneralAddress;
import com.prominent.title.entity.user.Organization;
import com.prominent.title.entity.user.PaymentInformation;
import com.prominent.title.entity.user.User;
import com.prominent.title.projection.UserListDtoProjection;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityConverter {

    @Mapping(target = "userId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserDetailsDtoToUser(UserDetailsDto userDetailsDto, @MappingTarget User user);

    User userEntryDtoToUser(UserEntryDto userEntryDto);

    User userSignupDtoToUser(UserSignupDto userSignupDto);

    UserDetailsDto userToUserDetailsDto(User user);

    Organization organizationDtoToOrganization(OrganizationDto organizationDto);

    OrganizationDto organizationToOrganizationDto(Organization organization);

    @Mapping(target = "organizationId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Organization updateOrganizationDtoToOrganization(OrganizationDto organizationDto, @MappingTarget Organization organization);

    OrganizationListDto organizationToListDto(Organization organization);

    OrganizationListDto organizationToListDto(Organization organization, GeneralAddress generalAddress);

    PaymentInformation paymentInformationDtoToPaymentInformation(PaymentInformationDto paymentInformationDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PaymentInformation updatePaymentInformationFromPaymentInformationDto(PaymentInformationDto paymentInformationDto, @MappingTarget PaymentInformation paymentInformation);

    PaymentInformationDto paymentInformationToPaymentInformationDto(PaymentInformation paymentInformation);

    UserListDto convertUserListProjectionToDto(UserListDtoProjection userListDtoProjection);

    @Mapping(target = "phoneNumber", source = "contactNumber")
    UserIdAndNameDto userToUserIdAndNameDto(User user);

    /**
     * This mapstruct methods are used for transforming address field to general address
     */
    GeneralAddressDto generalAddressToGeneralAddressDto(GeneralAddress generalAddress);

    List<GeneralAddressDto> listOfGeneralAddressToGeneralAddressDto(List<GeneralAddress> addressList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GeneralAddress updateGeneralAddressFromUserDetailsDto(UserDetailsDto userDetailsDto, @MappingTarget GeneralAddress generalAddress);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GeneralAddress updateGeneralAddressFromOrganizationDto(OrganizationDto organizationDto, @MappingTarget GeneralAddress generalAddress);

}
