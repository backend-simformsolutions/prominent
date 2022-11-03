package com.prominent.title.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CityDto {
    @Schema(example = "Autauga County/City")
    @NotEmpty(message = "Please Enter City/County Name")
    String name;
}
