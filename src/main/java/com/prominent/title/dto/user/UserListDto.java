package com.prominent.title.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserListDto {

    private int userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String role;
    private String contactNumber;
    private String address;
    private String city;
    private String state;
    private String zipCode;
}
