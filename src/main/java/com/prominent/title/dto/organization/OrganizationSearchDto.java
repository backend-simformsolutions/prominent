package com.prominent.title.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrganizationSearchDto {
    private int organizationId;
    private String organizationName;
    private String managingBroker;
}
