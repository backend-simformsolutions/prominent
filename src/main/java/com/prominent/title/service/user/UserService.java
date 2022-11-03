package com.prominent.title.service.user;

import com.prominent.title.converter.EntityConverter;
import com.prominent.title.dto.resource.GeneralAddressDto;
import com.prominent.title.dto.response.GenericListResponse;
import com.prominent.title.dto.response.GenericPageableDto;
import com.prominent.title.dto.user.UserDetailsDto;
import com.prominent.title.dto.user.UserEntryDto;
import com.prominent.title.dto.user.UserListDto;
import com.prominent.title.dto.user.UserSignupDto;
import com.prominent.title.entity.resource.GeneralAddress;
import com.prominent.title.entity.user.Organization;
import com.prominent.title.entity.user.Role;
import com.prominent.title.entity.user.User;
import com.prominent.title.entity.user.UserRoles;
import com.prominent.title.exception.*;
import com.prominent.title.projection.UserListDtoProjection;
import com.prominent.title.repository.OrganizationRepository;
import com.prominent.title.repository.RoleRepository;
import com.prominent.title.repository.UserRepository;
import com.prominent.title.repository.UserRolesRepository;
import com.prominent.title.utility.RecordCreationUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/*This Service has necessary methods related to task users will perform.*/

@Slf4j
@Service
public class UserService {

    private final OrganizationRepository organizationRepository;

    private final EntityConverter entityConverter;


    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;


    private final RoleRepository roleRepository;

    private final UserRolesRepository userRolesRepository;

    private final RecordCreationUtility recordCreationUtility;

    public UserService(OrganizationRepository organizationRepository, EntityConverter entityConverter, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRolesRepository userRolesRepository, RecordCreationUtility recordCreationUtility) {
        this.organizationRepository = organizationRepository;
        this.entityConverter = entityConverter;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRolesRepository = userRolesRepository;
        this.recordCreationUtility = recordCreationUtility;
    }

    /**
     * This method takes editUserDto as an input and updates existing user to the database
     *
     * @param userDetailsDto userDetailsDto
     * @return {@link UserDetailsDto}
     * @see UserDetailsDto
     */
    public UserDetailsDto editUserDetails(UserDetailsDto userDetailsDto) {
        Optional<User> existingUser = userRepository.findByUserName(userDetailsDto.getUserName().toLowerCase());
        if (existingUser.isPresent()) {
            User newUser = entityConverter.updateUserDetailsDtoToUser(userDetailsDto, existingUser.get());
            List<GeneralAddress> generalAddressList = newUser.getAddressList();
            generalAddressList.sort((o1, o2) -> Boolean.compare(o1.isDefault(), o2.isDefault()));
            newUser.addAddressToAddressList(entityConverter.updateGeneralAddressFromUserDetailsDto(userDetailsDto, generalAddressList.stream().findFirst().orElseThrow(() -> new AddressNotFoundException(existingUser.get().getUserId() + ""))));
            newUser.setUserName(userDetailsDto.getUserName().toLowerCase());
            userRepository.save(newUser);
            log.info("updated user {}...", userDetailsDto.getUserName().toLowerCase());
            return userDetailsDto;
        } else
            throw new UserNotFoundException(userDetailsDto.getUserName().toLowerCase());
    }

    /**
     * This method used to add user.
     *
     * @param userEntryDto userEntryDto
     * @return {@link UserEntryDto}
     * @see UserEntryDto
     */
    public UserEntryDto addUser(UserEntryDto userEntryDto) {
        User user = entityConverter.userEntryDtoToUser(userEntryDto);
        user.setUserName(userEntryDto.getUserName().toLowerCase());
        if (organizationRepository.existsByOrganizationNameIgnoreCase(userEntryDto.getOrganizationName())) {
            Organization organization = organizationRepository.findByOrganizationNameIgnoreCase(userEntryDto.getOrganizationName());
            GeneralAddress generalAddress = new GeneralAddress();
            BeanUtils.copyProperties(recordCreationUtility.putNewRecordInformation(), generalAddress);
            BeanUtils.copyProperties(userEntryDto, generalAddress);
            generalAddress.setDefault(true);  //first address will default
            user.addAddressToAddressList(generalAddress);
            if (userRepository.existsByUserName(userEntryDto.getUserName().toLowerCase()))
                throw new DataIntegrityViolationException("This Email ");

            UserRoles userRoles = new UserRoles();
            userRoles.setUser(user);
            Role role = roleRepository.findByRoleCode(userEntryDto.getRoleCode());
            if (role == null)
                throw new RoleNotFoundException("This role does not exist");
            userRoles.setRole(role);
            BeanUtils.copyProperties(recordCreationUtility.putNewRecordInformation(), userRoles);
            userRolesRepository.save(userRoles);

            BeanUtils.copyProperties(recordCreationUtility.putNewRecordInformation(), user);
            organization.addUserInOrganization(user);
            organizationRepository.save(organization);

            return userEntryDto;
        } else {
            throw new OrganizationNotFoundException(userEntryDto.getOrganizationName());
        }
    }

    /**
     * This method used to signup user.
     *
     * @param userSignupDto    userSignupDto
     * @param organizationName organizationName
     * @return {@link UserSignupDto}
     * @see UserSignupDto
     */
    public UserSignupDto addUser(UserSignupDto userSignupDto, String organizationName) {

        if (userRepository.existsByUserName(userSignupDto.getUserName().toLowerCase()))
            throw new DataIntegrityViolationException("This Email ");

        User user;
        user = entityConverter.userSignupDtoToUser(userSignupDto);
        user.setUserPassword(passwordEncoder.encode(userSignupDto.getUserPassword()));
        user.setUserName(userSignupDto.getUserName().toLowerCase());

        Organization organization = organizationRepository.findByOrganizationNameIgnoreCase(organizationName);
        Role role = roleRepository.findByRoleCode(userSignupDto.getRoleCode());

        if (role == null)
            throw new RoleNotFoundException("This role does not exist");
        UserRoles userRoles = new UserRoles();
        userRoles.setUser(user);
        userRoles.setRole(role);
        BeanUtils.copyProperties(recordCreationUtility.putNewRecordInformation(), userRoles);
        userRolesRepository.save(userRoles);

        BeanUtils.copyProperties(recordCreationUtility.putNewRecordInformation(), user);
        organization.getUserList().add(user);
        organizationRepository.save(organization);

        return userSignupDto;
    }

    /**
     * This method used to get user details.
     *
     * @param username username
     * @param userId   userId
     * @return {@link UserDetailsDto}
     * @see UserDetailsDto
     */
    public UserDetailsDto getUserDetails(String username, String userId) {
        Optional<User> optionalUser;
        if (Objects.equals(userId, "") || userId == null)
            optionalUser = userRepository.findByUserName(username);
        else
            optionalUser = userRepository.findById(Integer.parseInt(userId));
        if (optionalUser.isPresent()) {
            log.info("UserDetails Fetched for {}...", username);
            List<GeneralAddressDto> generalAddressDtoList = entityConverter.listOfGeneralAddressToGeneralAddressDto(optionalUser.get().getAddressList());
            generalAddressDtoList.sort((o1, o2) -> Boolean.compare(o1.isDefault(), o2.isDefault()));
            UserDetailsDto userDetailsDto = entityConverter.userToUserDetailsDto(optionalUser.get());
            BeanUtils.copyProperties(generalAddressDtoList.stream().findFirst().orElseThrow(() -> new AddressNotFoundException(optionalUser.get().getUserId() + "")), userDetailsDto);
            userDetailsDto.setRoleCode(optionalUser.get().getUserRoles().stream().map(UserRoles::getRole).map(Role::getRoleCode).reduce("", String::concat));
            return userDetailsDto;
        } else {
            throw new UserNotFoundException(username);
        }
    }

    /**
     * This method finds users and sorts them according to supplied properties
     *
     * @param genericPageableDto genericPageableDto
     * @return {@link GenericListResponse}
     * @see GenericListResponse
     */
    public GenericListResponse findAllUsers(GenericPageableDto genericPageableDto) {
        int size = genericPageableDto.getSize();
        int page = genericPageableDto.getPage();
        String order = genericPageableDto.getOrder();
        String columnName = genericPageableDto.getColumnName();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.valueOf(order.toUpperCase()), columnName);
        Page<UserListDtoProjection> userListDtoProjectionPage;
        try {
            userListDtoProjectionPage = userRepository.findAllUsersPaginated(pageable);
        } catch (InvalidDataAccessApiUsageException exception) {
            log.info("InvalidDataAccessApiUsageException For Column {} ...", columnName);
            throw new InvalidColumnNameException(columnName);
        }
        List<UserListDto> userListDtos = userListDtoProjectionPage.getContent().stream().map(entityConverter::convertUserListProjectionToDto).collect(Collectors.toList());
        return new GenericListResponse(true, "List fetched Successfully", userListDtos, HttpStatus.OK.value(), userListDtoProjectionPage.getTotalPages(), userListDtoProjectionPage.getTotalElements());
    }

}
