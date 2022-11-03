package com.prominent.title.validation;

import com.prominent.title.annotation.ResetPasswordConstraint;
import com.prominent.title.dto.ResetPasswordDto;
import com.prominent.title.entity.user.User;
import com.prominent.title.exception.PasswordContainsUsernameException;
import com.prominent.title.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * This Validator is used for custom annotation password
 * It validates the password by regex
 */
@Slf4j
public class ResetPasswordValidator implements ConstraintValidator<ResetPasswordConstraint, ResetPasswordDto> {

    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\\s).{12,20}$";

    private static final String PASSWORD_USERNAME_MESSAGE = "Your password should not contain Your Username";

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void initialize(ResetPasswordConstraint constraintAnnotation) {
        // No need for initialization
    }

    /**
     * This method validates password that does not contain username and has at least 1 Capital letter, 1 Small letter, 1 Number and 1 Special symbol.
     */
    @Override
    public boolean isValid(ResetPasswordDto resetPasswordDto, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Checking Password Validator ...");
        boolean isValid = false;
        Optional<User> optionalUser = userRepository.findByForgotPasswordAccessKey(resetPasswordDto.getToken());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String userName = user.getUserName();
            String userPassword = resetPasswordDto.getPassword();
            isValid = !userName.equals("") && userPassword.matches(PASSWORD_REGEX);
            if (!isValid && userPassword.toLowerCase().contains(userName.toLowerCase())) {
                throw new PasswordContainsUsernameException(PASSWORD_USERNAME_MESSAGE);
            }

            if(!isValid)
            {
                throw new PasswordContainsUsernameException("Password Should Contain At Least 1 Capital letter, 1 Symbol and 1 Digit");
            }
        }

        return isValid;
    }
}
