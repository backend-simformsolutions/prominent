package com.prominent.title.service;

import com.prominent.title.converter.EntityConverter;
import com.prominent.title.dto.organization.*;
import com.prominent.title.dto.resource.GeneralAddressDto;
import com.prominent.title.dto.util.CreateRecordInformation;
import com.prominent.title.dto.util.UpdateRecordInformation;
import com.prominent.title.entity.resource.GeneralAddress;
import com.prominent.title.entity.user.Organization;
import com.prominent.title.entity.user.PaymentInformation;
import com.prominent.title.entity.user.User;
import com.prominent.title.exception.OrganizationNotFoundException;
import com.prominent.title.repository.OrganizationRepository;
import com.prominent.title.service.organization.OrganizationService;
import com.prominent.title.utility.RecordCreationUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationService organizationService;

    @Mock
    private EntityConverter entityConverter;

    @Mock
    private RecordCreationUtility recordCreationUtility;
    private Organization organization;

    private OrganizationDto organizationDto;
    private PaymentInformationDto paymentInformationDto;
    private OrganizationEntryDto organizationEntryDto;
    private User user;

    private List<GeneralAddress> addressList;

    private GeneralAddress generalAddress1;

    private List<OrganizationSearchDto> organizationSearchDtos;

    @BeforeEach
    public void setup() {
        organization = new Organization();
        organization.setOrganizationId(1);
        organization.setOrganizationName("Test");
        organization.setPrimaryContactEmail("dipak@gmail.com");
        organization.setOrganizationTypeName("TestType");
        organization.setActive(true);
        organization.setPrimaryContactName("Dipak");
        organization.setBrokerageAdminFeeAmount("10");

        PaymentInformation paymentInformation = new PaymentInformation();
        paymentInformation.setPaymentMethod("Cash");
        paymentInformation.setAddress("A1 block");
        paymentInformation.setCity("Prantij");
        paymentInformation.setBankAccountNumber("34214687439274932");
        paymentInformation.setRecipientName("Test2");
        organization.setPaymentInformation(paymentInformation);
        GeneralAddress generalAddress = new GeneralAddress();
        generalAddress.setDefault(true);
        organization.addAddressInOrganization(generalAddress);

        List<User> userList = new ArrayList<>();
        user = new User();
        user.setUserName("Dipak");
        user.setUserPassword("Test@123456");
        user.setContactNumber("914455098988");
        user.setFirstName("Dipak");
        user.setLastName("Patel");
        user.setActive(true);
        userList.add(user);
        organization.setUserList(userList);

        addressList = new ArrayList<>();
        generalAddress1 = new GeneralAddress();
        generalAddress1.setAddress("Add line1");
        generalAddress1.setAddressId(1);
        generalAddress1.setState("Guj");
        generalAddress1.setDefault(true);
        addressList.add(generalAddress1);

        organizationSearchDtos = new ArrayList<>();
        organizationSearchDtos.add(new OrganizationSearchDto(1, "Thurston Wyatt real Estate LLC.", "Dhaval"));

        organizationDto = new OrganizationDto(1, "Thurston Wyatt real Estate LLC.", "TestType", "New York", "New York", "New York", "1111", "Jcob", "2025550162", "jcob@gmail.com", "30-2232116", "2");
        paymentInformationDto = new PaymentInformationDto("Cash", "John Patel", "2472 Thunder Road", "Anchorage", "Anchorage", "99501", "123456789101112", "1020304050");
        organizationEntryDto = new OrganizationEntryDto(organizationDto, paymentInformationDto);

    }

    @Test
    void add_organization_test() {

        when(organizationRepository.existsByOrganizationNameIgnoreCase(any())).thenReturn(false);
        when(organizationRepository.save(any())).thenReturn(organization);
        when(entityConverter.organizationDtoToOrganization(organizationDto)).thenReturn(organization);
        when(recordCreationUtility.putNewRecordInformation()).thenReturn(new CreateRecordInformation(true, 1, LocalDateTime.now(), "fdfsdf", "errer"));
        OrganizationEntryDto savedOrganizationEntryDto = organizationService.add(organizationEntryDto);
        assertThat(savedOrganizationEntryDto).isNotNull();
    }

    @Test
    void get_organization_test() {

        List<GeneralAddressDto> addressListDto = new ArrayList<>();
        addressListDto.add(new GeneralAddressDto("Add line1", "Prantij", "Gujarat", "383120", true));

        when(organizationRepository.findById(anyInt())).thenReturn(Optional.ofNullable(organization));
        when(entityConverter.listOfGeneralAddressToGeneralAddressDto(any())).thenReturn(addressListDto);
        when(entityConverter.organizationToOrganizationDto(any())).thenReturn(organizationDto);

        OrganizationDtoWithBrokers organizationDtoWithBrokers = organizationService.getOrganization(1);

        assertThat(organizationDtoWithBrokers).isNotNull();
    }

    @Test
    void get_organization_organization_not_found_test() {

        when(organizationRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(OrganizationNotFoundException.class, () -> {
            organizationService.getOrganization(2);
        });

        assertTrue(exception.getMessage().contains("2"));
    }

    @Test
    void edit_organization_test() {

        when(organizationRepository.existsByOrganizationNameIgnoreCase(any())).thenReturn(true);
        when(organizationRepository.findByOrganizationNameIgnoreCase(any())).thenReturn(organization);
        when(entityConverter.updateOrganizationDtoToOrganization(organizationEntryDto.getOrganizationDto(), organization)).thenReturn(organization);
        when(entityConverter.updateGeneralAddressFromOrganizationDto(any(), any())).thenReturn(generalAddress1);
        when(entityConverter.updatePaymentInformationFromPaymentInformationDto(any(), any())).thenReturn(organization.getPaymentInformation());
        when(recordCreationUtility.updateRecordInformation()).thenReturn(new UpdateRecordInformation(-1, LocalDateTime.now()));

        OrganizationEntryDto savedOrganizationEntryDto = organizationService.editOrganizationDetails(organizationEntryDto);

        assertThat(savedOrganizationEntryDto).isNotNull();
    }

    @Test
    void edit_organization_organization_not_found_test() {

        when(organizationRepository.existsByOrganizationNameIgnoreCase(any())).thenReturn(false);

        Exception exception = assertThrows(OrganizationNotFoundException.class, () -> {
            organizationService.editOrganizationDetails(organizationEntryDto);
        });

        assertTrue(exception.getMessage().contains(organizationDto.getOrganizationName()));
    }

    @Test
    void delete_organization_test() {

        when(organizationRepository.findById(anyInt())).thenReturn(Optional.ofNullable(organization));

        Boolean isOrganizationDeleted = organizationService.deleteOrganization(organizationDto.getOrganizationId());

        assertTrue(isOrganizationDeleted);
    }

    @Test
    void delete_organization_organization_not_found_test() {

        when(organizationRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(OrganizationNotFoundException.class, () -> {
            organizationService.deleteOrganization(1);
        });

        assertTrue(exception.getMessage().contains(String.valueOf(organizationDto.getOrganizationId())));
    }

    @Test
    void search_organization_test() {

        when(organizationRepository.findByOrganizationNameLikeIgnoreCase(any())).thenReturn(organizationSearchDtos);

        List<OrganizationSearchDto> fetchedOrganizationList = organizationService.searchOrganization(organizationDto.getOrganizationName());
        System.out.println("size:" + fetchedOrganizationList.size());
        assertThat(fetchedOrganizationList).isNotEmpty();
    }

}

