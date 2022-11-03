package com.prominent.title.dto.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateRecordInformation {
    private boolean isActive;
    private int createUserId;
    private LocalDateTime createDate;
    private String internalKey;
    private String concurrencyKey;
}
