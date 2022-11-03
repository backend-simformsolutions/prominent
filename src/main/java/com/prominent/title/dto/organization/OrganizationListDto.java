package com.prominent.title.dto.organization;

import lombok.Data;

@Data
public class OrganizationListDto {

    private int organizationId;
    private String organizationName;
    private String city;
    private String state;
    private String address;
    private String zipCode;
    private String primaryContactName;
    private String primaryContactPhone;
    private String primaryContactEmail;

}
