package com.prominent.title.dto.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UpdateRecordInformation {
    private int updateUserId;
    private LocalDateTime updateDate;
}
