package com.prominent.title.dto.resource;

import com.prominent.title.entity.resource.GeneralAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link GeneralAddress} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeneralAddressDto implements Serializable {

    private String address;
    private String state;
    private String city;
    private String zipCode;
    private boolean isDefault;
}