package com.prominent.title.dto;

import com.prominent.title.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private User user;
    private String token;
    private List<String> role;
}
