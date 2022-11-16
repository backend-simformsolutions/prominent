package com.prominent.title.service.organization;


import com.prominent.title.converter.EntityConverter;
import com.prominent.title.dto.organization.*;
import com.prominent.title.dto.resource.GeneralAddressDto;
import com.prominent.title.dto.response.GenericFilterDto;
import com.prominent.title.dto.response.GenericListResponse;
import com.prominent.title.dto.response.GenericPageableDto;
import com.prominent.title.dto.user.UserIdAndNameDto;
import com.prominent.title.entity.resource.GeneralAddress;
import com.prominent.title.entity.user.Organization;
import com.prominent.title.entity.user.PaymentInformation;
import com.prominent.title.exception.AddressNotFoundException;
import com.prominent.title.exception.InvalidColumnNameException;
import com.prominent.title.exception.OrganizationNotFoundException;
import com.prominent.title.repository.OrganizationRepository;
import com.prominent.title.utility.EntitySpecifications;
import com.prominent.title.utility.RecordCreationUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final EntityConverter entityConverter;

    private final RecordCreationUtility recordCreationUtility;

    public OrganizationService(OrganizationRepository organizationRepository, EntityConverter entityConverter, RecordCreationUtility recordCreationUtility) {
        this.organizationRepository = organizationRepository;
        this.entityConverter = entityConverter;
        this.recordCreationUtility = recordCreationUtility;
    }

    /**
     * This method find all the organization with pageable.
     *
     * @param genericPageableDto genericPageableDto
     * @return {@link GenericListResponse}
     * @see GenericListResponse
     */
    public GenericListResponse findAllOrganizationPageable(GenericPageableDto genericPageableDto, GenericFilterDto genericFilterDto) {
        log.info("Converting Organization To OrganizationListDto ... ");
        int size = genericPageableDto.getSize();
        int page = genericPageableDto.getPage();
        String order = genericPageableDto.getOrder();
        String columnName = genericPageableDto.getColumnName();
        String searchTerm = genericFilterDto.getSearchTerm();
        if (columnName.equalsIgnoreCase("address") || columnName.equalsIgnoreCase("zipCode") || columnName.equalsIgnoreCase("city") || columnName.equalsIgnoreCase("state"))
            columnName = "addressList." + columnName;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.valueOf(order.toUpperCase()), columnName);
        Page<Organization> organizationPage;
        try {
            Specification<Organization> spec =
                    Specification.where(EntitySpecifications.searchOrganizationName(searchTerm));

            organizationPage = organizationRepository.findAll(spec, pageable);
        } catch (InvalidDataAccessApiUsageException exception) {
            log.info("InvalidDataAccessApiUsageException For Column {} ...", columnName);
            throw new InvalidColumnNameException(columnName);
        }
        List<OrganizationListDto> organizationListDtoList = organizationPage.stream().map(
                orders -> {
                    Optional<GeneralAddress> optionalGeneralAddress = orders.getAddressList().stream().filter(GeneralAddress::isDefault).findFirst();
                    if (optionalGeneralAddress.isPresent())
                        return entityConverter.organizationToListDto(orders, optionalGeneralAddress.get());
                    return entityConverter.organizationToListDto(orders);
                }).collect(Collectors.toList());

        return new GenericListResponse(true, organizationPage.isEmpty() ? "No Record Found" : "Record Fetched Successfully", organizationListDtoList, HttpStatus.OK.value(), organizationPage.getTotalPages(), organizationPage.getTotalElements());
    }

    /**
     * This method handles exception if organization with same name already exists and if not then
     * it sets organization's general information using utility and then saves organization information.
     *
     * @param organizationEntryDto organizationEntryDto
     * @return {@link OrganizationEntryDto}
     * @see OrganizationEntryDto
     */
    public OrganizationEntryDto add(OrganizationEntryDto organizationEntryDto) {
        OrganizationDto organizationDto = organizationEntryDto.getOrganizationDto();
        PaymentInformationDto paymentInformationDto = organizationEntryDto.getPaymentInformationDto();
        log.info("Checking If Organization With Name - {} Already Exists ...", organizationEntryDto.getOrganizationDto().getOrganizationName());
        if (organizationRepository.existsByOrganizationNameIgnoreCase(organizationDto.getOrganizationName())) {
            log.info("Organization With Name - {} Already Exists ...", organizationDto.getOrganizationName());
            throw new DataIntegrityViolationException(organizationDto.getOrganizationName());
        }
        Organization organization = entityConverter.organizationDtoToOrganization(organizationDto);
        organization.setPaymentInformation(entityConverter.paymentInformationDtoToPaymentInformation(paymentInformationDto));
        BeanUtils.copyProperties(recordCreationUtility.putNewRecordInformation(), organization);
        GeneralAddress generalAddress = new GeneralAddress();
        BeanUtils.copyProperties(organizationDto, generalAddress);
        BeanUtils.copyProperties(recordCreationUtility.putNewRecordInformation(), generalAddress);
        generalAddress.setDefault(true);
        organization.addAddressInOrganization(generalAddress);
        organization.setPrimaryContactEmail(organizationEntryDto.getOrganizationDto().getPrimaryContactEmail().toLowerCase());
        log.info("Saving Organization Information With Name - {}", organizationDto.getOrganizationName());
        organizationRepository.save(organization);
        log.info("Saved Organization Information With Name - {}", organizationDto.getOrganizationName());
        organizationDto.setOrganizationId(organization.getOrganizationId());
        return organizationEntryDto;
    }

    /**
     * This method retrieves Organization Information using id else throws Exception
     *
     * @param id id
     * @return {@link OrganizationDtoWithBrokers}
     * @see OrganizationDtoWithBrokers
     */
    public OrganizationDtoWithBrokers getOrganization(int id) {

        Optional<Organization> optionalOrganization = organizationRepository.findById(id);
        if (optionalOrganization.isPresent()) {
            PaymentInformation paymentInformation = optionalOrganization.get().getPaymentInformation();

            List<GeneralAddressDto> generalAddressDtoList = entityConverter.listOfGeneralAddressToGeneralAddressDto(optionalOrganization.get().getAddressList());
            generalAddressDtoList.sort((o1, o2) -> Boolean.compare(o1.isDefault(), o2.isDefault()));
            List<UserIdAndNameDto> idAndNameDtoList = optionalOrganization.get().getUserList().stream().map(entityConverter::userToUserIdAndNameDto).collect(Collectors.toList());

            OrganizationDto organizationDto = entityConverter.organizationToOrganizationDto(optionalOrganization.get());
            BeanUtils.copyProperties(generalAddressDtoList.stream().findFirst().orElseThrow(() -> new AddressNotFoundException(id + "")), organizationDto);
            return new OrganizationDtoWithBrokers(organizationDto, paymentInformation != null ? entityConverter.paymentInformationToPaymentInformationDto(paymentInformation) : null, idAndNameDtoList);
        } else {
            throw new OrganizationNotFoundException(id + "");
        }
    }

    /**
     * This method takes OrganizationDto finds and updates organization information else throws Exception
     *
     * @param organizationEntryDto organizationEntryDto
     * @return {@link OrganizationEntryDto}
     * @see OrganizationEntryDto
     */

    public OrganizationEntryDto editOrganizationDetails(OrganizationEntryDto organizationEntryDto) {
        log.info("Checking If Organization With Name - {} Already Exists ...", organizationEntryDto.getOrganizationDto().getOrganizationName());
        if (organizationRepository.existsByOrganizationNameIgnoreCase(organizationEntryDto.getOrganizationDto().getOrganizationName())) {
            Organization organization = organizationRepository.findByOrganizationNameIgnoreCase(organizationEntryDto.getOrganizationDto().getOrganizationName());
            PaymentInformation paymentInformation = organization.getPaymentInformation();
            organization = entityConverter.updateOrganizationDtoToOrganization(organizationEntryDto.getOrganizationDto(), organization);

            List<GeneralAddress> generalAddressList = organization.getAddressList();
            generalAddressList.sort((o1, o2) -> Boolean.compare(o1.isDefault(), o2.isDefault()));
            organization.addAddressInOrganization(entityConverter.updateGeneralAddressFromOrganizationDto(organizationEntryDto.getOrganizationDto(), generalAddressList.stream().findFirst().orElseThrow(() -> new AddressNotFoundException(organizationEntryDto.getOrganizationDto().getOrganizationId() + ""))));
            if (paymentInformation == null) {
                paymentInformation = entityConverter.paymentInformationDtoToPaymentInformation(organizationEntryDto.getPaymentInformationDto());
            } else
                paymentInformation = entityConverter.updatePaymentInformationFromPaymentInformationDto(organizationEntryDto.getPaymentInformationDto(), paymentInformation);
            organization.setPaymentInformation(paymentInformation);
            organizationEntryDto.getOrganizationDto().setOrganizationId(organization.getOrganizationId());
            BeanUtils.copyProperties(recordCreationUtility.updateRecordInformation(), organization);
            organization.setPrimaryContactEmail(organizationEntryDto.getOrganizationDto().getPrimaryContactEmail().toLowerCase());
            organizationRepository.save(organization);
            return organizationEntryDto;
        } else {
            throw new OrganizationNotFoundException(organizationEntryDto.getOrganizationDto().getOrganizationName());
        }
    }

    public Boolean deleteOrganization(int organizationId) {
        Optional<Organization> optionalOrganization = organizationRepository.findById(organizationId);
        if (optionalOrganization.isPresent()) {
            organizationRepository.delete(optionalOrganization.get());
            return true;
        } else {
            throw new OrganizationNotFoundException(organizationId + "");
        }
    }

    /**
     * This method search the organization by searchterm.
     *
     * @param searchTerm searchTerm
     * @return {@link List}
     * @see List
     * @see OrganizationSearchDto
     */
    public List<OrganizationSearchDto> searchOrganization(String searchTerm) {
        return organizationRepository.findByOrganizationNameLikeIgnoreCase(searchTerm.toLowerCase());
    }
}
