package com.prominent.title.service;

import com.prominent.title.converter.EntityConverter;
import com.prominent.title.dto.resource.GeneralAddressDto;
import com.prominent.title.dto.user.UserDetailsDto;
import com.prominent.title.dto.user.UserSignupDto;
import com.prominent.title.dto.util.CreateRecordInformation;
import com.prominent.title.entity.resource.GeneralAddress;
import com.prominent.title.entity.user.*;
import com.prominent.title.exception.UserNotFoundException;
import com.prominent.title.repository.OrganizationRepository;
import com.prominent.title.repository.RoleRepository;
import com.prominent.title.repository.UserRepository;
import com.prominent.title.repository.UserRolesRepository;
import com.prominent.title.service.user.UserService;
import com.prominent.title.utility.RecordCreationUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private EntityConverter entityConverter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RecordCreationUtility recordCreationUtility;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRolesRepository userRolesRepository;
    @InjectMocks
    private UserService userService;
    private Organization organization;
    private User user;

    private Role role;

    private UserRoles userRoles;

    private UserDetailsDto userDetailsDto;
    private UserSignupDto userSignupDto;
    private List<GeneralAddressDto> addressListDto;

    @BeforeEach
    public void setup() {
        userDetailsDto = new UserDetailsDto(1, "Dipak", "Dhaval", "Patel", "null", "null", "null", "null", "null", "null", "null");
        userSignupDto = new UserSignupDto("dipak@gmail.com", "9963725849", "User", "Test@1231111", "Test@1231111");
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

        user = new User();
        user.setUserName("Dipak");
        user.setUserPassword("Test@123456");
        user.setContactNumber("914455098988");
        user.setFirstName("Dipak");
        user.setLastName("Patel");
        user.setActive(true);
        List<GeneralAddress> addressList = new ArrayList<>();
        GeneralAddress generalAddress1 = new GeneralAddress();
        generalAddress1.setAddress("Add line1");
        generalAddress1.setAddressId(1);
        generalAddress1.setState("Guj");
        addressList.add(generalAddress1);
        user.setAddressList(addressList);

        role = new Role();
        role.setRoleId(1);
        role.setRoleCode("User");
        role.setCreateDate(LocalDateTime.now());

        userRoles = new UserRoles();
        userRoles.setRole(role);
        userRoles.setUser(user);
        addressListDto = new ArrayList<>();
        addressListDto.add(new GeneralAddressDto("Add line1", "Prantij", "Gujarat", "383120", true));

    }

    @Test
    void user_sign_up_test() {
        when(userRepository.existsByUserName(any())).thenReturn(false);
        when(organizationRepository.findByOrganizationNameIgnoreCase(any())).thenReturn(organization);
        when(passwordEncoder.encode(userSignupDto.getUserPassword())).thenReturn("xasafdsfre");
        when(entityConverter.userSignupDtoToUser(userSignupDto)).thenReturn(user);
        when(roleRepository.findByRoleCode(userSignupDto.getRoleCode())).thenReturn(role);
        when(recordCreationUtility.putNewRecordInformation()).thenReturn(new CreateRecordInformation(true, 1, LocalDateTime.now(), "fdfsdf", "errer"));
        when(userRolesRepository.save(userRoles)).thenReturn(userRoles);
        when(organizationRepository.save(organization)).thenReturn(organization);

        UserSignupDto savedUserSignupDto = userService.addUser(userSignupDto, "General User");
        assertThat(savedUserSignupDto).isNotNull();
    }

    @Test
    void edit_user_details_test() {
        when(userRepository.findByUserName(any())).thenReturn(Optional.ofNullable(user));
        GeneralAddress GeneralAddress = new GeneralAddress();
        when(entityConverter.updateGeneralAddressFromUserDetailsDto(any(), any())).thenReturn(GeneralAddress);
        when(entityConverter.updateUserDetailsDtoToUser(any(), any())).thenReturn(user);

        UserDetailsDto savedUserDetailsDto = userService.editUserDetails(userDetailsDto);
        assertThat(savedUserDetailsDto.getFirstName().equals("Dhaval"));
    }

    @Test
    void edit_user_not_found_test() {
        when(userRepository.findByUserName(any())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.editUserDetails(userDetailsDto);
        });

        assertTrue(exception.getMessage().contains(userDetailsDto.getUserName().toLowerCase()));
    }
}
