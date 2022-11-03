package com.prominent.title.dto.user;

import com.prominent.title.annotation.SignupConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SignupConstraint
public class UserSignupDto {

    @Schema(example = "john_doe@gmail.com")
    private String userName;
    @Schema(example = "8063725849")
    private String contactNumber;
    @Schema(allowableValues = {"Admin", "Broker", "User", "Lender", "Notary", "Title Abstractor", "Surveyor"})
    private String roleCode;
    @Schema(example = "************")
    private String userPassword;
    @Schema(example = "************")
    private String repeatPassword;
}
