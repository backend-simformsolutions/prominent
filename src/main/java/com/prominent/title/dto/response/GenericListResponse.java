package com.prominent.title.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericListResponse {
    private boolean success;
    private String message;
    private Object data;
    private int code;
    private int totalPages;
    private long totalElements;
}
