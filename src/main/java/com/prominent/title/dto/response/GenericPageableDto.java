package com.prominent.title.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericPageableDto {
    private String columnName;
    private int size;
    private int page;
    private String order;

    public GenericPageableDto(int size, int page) {
        this.size = size;
        this.page = page;
    }
}