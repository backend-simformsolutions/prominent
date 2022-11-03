package com.prominent.title.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenericFilterDto {
    @Nullable
    private LocalDate startDate;
    @Nullable
    private LocalDate endDate;
    @Nullable
    private String status;
    @Nullable
    private String searchTerm;

    public GenericFilterDto(@Nullable String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
