package com.prominent.title.dto.resource;

import com.prominent.title.entity.resource.GeneralAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * A DTO for the {@link GeneralAddress} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GeneralAddressDtoWithId extends GeneralAddressDto {
    private int addressId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GeneralAddressDtoWithId that = (GeneralAddressDtoWithId) o;
        return addressId == that.addressId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), addressId);
    }
}