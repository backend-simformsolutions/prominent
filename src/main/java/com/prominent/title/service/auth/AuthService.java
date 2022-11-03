package com.prominent.title.service.auth;

import com.prominent.title.dto.LoginResponseDto;
import com.prominent.title.dto.ResetPasswordDto;
import com.prominent.title.dto.response.EmptyJsonBody;
import com.prominent.title.dto.response.GenericResponse;
import com.prominent.title.dto.user.UserLoginDto;
import com.prominent.title.entity.user.LoginHistory;
import com.prominent.title.entity.user.User;
import com.prominent.title.exception.UserNotFoundException;
import com.prominent.title.repository.LoginHistoryRepository;
import com.prominent.title.repository.UserRepository;
import com.prominent.title.utility.EmailUtil;
import com.prominent.title.utility.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    private final LoginHistoryRepository loginHistoryRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
    private final AuthenticationManager authenticationManager;
    @Value("${MAX_FAILED_ATTEMPTS}")
    public int maxFailedAttempts;
    @Value("${LOCK_TIME_DURATION}")
    public long lockTimeDuration;
    @Value("${JWT_TOKEN_EXPIRATION}")
    public long jwtTokenExpiration;
    @Value("${PASSWORD_TOKEN_EXPIRATION}")
    public long passwordTokenExpiration;

    @Autowired
    public AuthService(LoginHistoryRepository loginHistoryRepository, UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, EmailUtil emailUtil, AuthenticationManager authenticationManager) {
        this.loginHistoryRepository = loginHistoryRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailUtil = emailUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * This method logouts the user.
     *
     * @param request request
     */
    public void logout(HttpServletRequest request) {
        log.info("Logging out...");
        String userName = jwtUtil.extractUsername(request.getHeader("Authorization").substring(7));
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UserNotFoundException(userName));
        loginHistoryRepository.updateLogoutTimeByUser(user, LocalDateTime.now());
        log.info("{} Logged out...", userName);
    }

    /**
     * This method increases fail attempt by 1 and update field in database
     *
     * @param user user
     */
    private void increaseFailedLogonAttempts(User user) {
        log.info("Increasing failed login attempts for user {}...", user.getUserName());
        int newFailAttempts = user.getFailedLogons() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getUserName());
        user.setFailedLogons(newFailAttempts);
        log.info("Increased failed login attempts for user {}...", user.getUserName());
    }

    /**
     * This method will lock the user and set lock time as current time in database field.
     *
     * @param user user
     */
    private void lockUser(User user) {
        log.info("Locking user {}...", user.getUserName());
        user.setAccountNonLocked(false);
        user.setLockTime(LocalDateTime.now());
        userRepository.save(user);
        log.info("User {} Locked...", user.getUserName());
    }

    /**
     * This method checks if user is now able to unlock the account or not.  If so then unlock the user. Dividing by 1000 for converting to seconds
     *
     * @param user user
     * @return {@link boolean}
     */
    private boolean unlockUserWhenTimeExpired(User user) {
        if (user.getLockTime().plusSeconds(lockTimeDuration / 1000).isBefore(LocalDateTime.now())) {
            log.info("Unlocking user {}...", user.getUserName());
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedLogons(0);
            userRepository.save(user);
            log.info("Unlocked user {}...", user.getUserName());
            return true;
        }
        return false;
    }

    /**
     * This method Increases total login attempts for user
     *
     * @param totalLogons totalLogons
     * @param user        user
     */
    private void increaseTotalLogons(int totalLogons, User user) {
        log.info("Increasing total login attempts for user {}...", user.getUserName());
        userRepository.increaseLogonAttempt(totalLogons + 1, user.getUserName());
        user.setTotalLogons(totalLogons + 1);
        log.info("Increased total login attempts for user {}...", user.getUserName());
    }

    /**
     * This method sets necessary user login information
     *
     * @param user user
     */
    private void setUserLoginInformation(User user) {
        log.info("Setting user login information...");
        if (user.getPreviousLogonDate() == null) {
            user.setPreviousLogonDate(LocalDateTime.now());
        } else {
            user.setPreviousLogonDate(user.getLogonDate());
        }
        user.setFailedLogons(0);
        user.setLogonDate(LocalDateTime.now());
        user.setActiveFrom(LocalDate.now());
        user.setActiveTo(LocalDate.now().plusDays(7));
        userRepository.save(user);
    }

    /**
     * This method authenticate username, password then generates the jwt token returns it.
     *
     * @param userLoginDto userLoginDto
     * @return {@link String}
     * @see String
     */
    private String setJwtAuthentication(UserLoginDto userLoginDto) {
        log.info("Setting jwt token for authentication...");
        User user = userRepository.findByUserName(userLoginDto.getUserName().toLowerCase()).orElseThrow(() -> new UserNotFoundException(userLoginDto.getUserName().toLowerCase()));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName().toLowerCase(), userLoginDto.getUserPassword()));
        setUserLoginInformation(user);
        return jwtUtil.generateToken(user.getUserName().toLowerCase(), jwtTokenExpiration);
    }

    /**
     * This method returns generic responses according to the message
     *
     * @param message      message
     * @param userLoginDto userLoginDto
     * @return {@link GenericResponse}
     * @see GenericResponse
     */
    public GenericResponse getApiResponse(String message, UserLoginDto userLoginDto) {
        GenericResponse genericResponse;
        LoginHistory loginHistory;
        User user = userRepository.findByUserName(userLoginDto.getUserName().toLowerCase()).orElseThrow(() -> new UserNotFoundException(userLoginDto.getUserName().toLowerCase()));
        loginHistory = setLoginHistoryInformation(user, message);
        if (message.contains("Success")) {
            List<String> roles = new ArrayList<>();
            user.getUserRoles().forEach(role -> roles.add(role.getRole().getRoleCode()));
            LoginResponseDto loginResponseDto = new LoginResponseDto(user, setJwtAuthentication(userLoginDto), roles);
            genericResponse = new GenericResponse(true, message, loginResponseDto, HttpStatus.OK.value());
        } else {
            loginHistory.setLoginResultName("FAILURE");
            genericResponse = new GenericResponse(false, message, new EmptyJsonBody(), HttpStatus.UNAUTHORIZED.value());
        }
        loginHistoryRepository.save(loginHistory);
        return genericResponse;
    }

    /**
     * This method sets information related to log in history of user like ip, host name, login time
     *
     * @param user    user
     * @param message message
     * @return {@link LoginHistory}
     * @see LoginHistory
     */
    private LoginHistory setLoginHistoryInformation(User user, String message) {
        log.info("Storing Login History Of User - {}", user.getUserName());
        LoginHistory loginHistory = new LoginHistory();
        InetAddress inetAddress;
        loginHistory.setUser(user);
        try {
            inetAddress = InetAddress.getLocalHost();
            loginHistory.setServerName(InetAddress.getLocalHost().getHostName());
            loginHistory.setLoginIpAddress(inetAddress.getHostAddress());
        } catch (UnknownHostException e) {
            log.info("UnknownHostException Is Thrown During Getting User's IP Address");
        }
        loginHistory.setLastLoginTime(LocalDateTime.now());
        loginHistory.setErrorMsg(message);
        loginHistory.setLoginResultName("SUCCESS");
        return loginHistory;
    }

    /**
     * This method checks the password if password is wrong then it will increase login attempt or lock if it reaches to max.
     *
     * @param isPasswordSame isPasswordSame
     * @param userLoginDto   userLoginDto
     * @return {@link String}
     * @see String
     */
    public String verifyUserLogonAttempts(boolean isPasswordSame, UserLoginDto userLoginDto) {
        log.info("Verifying login attempt for user {}...", userLoginDto.getUserName());
        String message;
        User user = userRepository.findByUserName(userLoginDto.getUserName().toLowerCase()).orElseThrow(() -> new UserNotFoundException(userLoginDto.getUserName().toLowerCase()));
        if (isPasswordSame) {
            message = "User Logged In Successfully";
        } else {
            if (user.getFailedLogons() < maxFailedAttempts) {
                if (user.getFailedLogons() + 1 == 3) {
                    log.info("Invalid username or password, One attempt remaining for user {}...", user.getUserName().toLowerCase());
                    message = "Invalid Username or Password. One Attempt Remaining";
                } else {
                    log.info("Invalid username or password for user {}...", user.getUserName().toLowerCase());
                    message = "Invalid Username or Password";
                }
            } else {
                lockUser(user);
                message = "Your account has been locked due to 3 failed attempts. It will be unlocked after 1 hour.";
            }
            increaseFailedLogonAttempts(user);
        }
        return message;
    }


    /**
     * This method checks if password is correct and if user is locked or not
     * And if conditions are match then used will be logged in successfully.
     *
     * @param userLoginDto userLoginDto
     * @return {@link GenericResponse}
     * @see GenericResponse
     */
    public GenericResponse verifyCredentials(UserLoginDto userLoginDto) {
        log.info("Verifying login credentials for user {}...", userLoginDto.getUserName().toLowerCase());
        Optional<User> optionalUser = userRepository.findByUserName(userLoginDto.getUserName().toLowerCase());
        String message;
        GenericResponse genericResponse;
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean isPasswordSame = passwordEncoder.matches(userLoginDto.getUserPassword(), user.getUserPassword());
            increaseTotalLogons(user.getTotalLogons(), user);
            if (user.isAccountNonLocked()) {
                message = verifyUserLogonAttempts(isPasswordSame, userLoginDto);
            } else {
                if (unlockUserWhenTimeExpired(user) && isPasswordSame) {
                    log.info("User {} logged in successfully...", user.getUserName().toLowerCase());
                    message = "User Logged In Successfully";
                } else {
                    increaseFailedLogonAttempts(user);
                    log.info("Account is still locked for user {}...", user.getUserName().toLowerCase());
                    message = "Your Account Is Still Locked. Please Try Again After Sometime";
                }
            }
        } else {
            log.error("User {} not found...", userLoginDto.getUserName().toLowerCase());
            message = "Invalid Username or Password";
        }
        genericResponse = getApiResponse(message, userLoginDto);
        return genericResponse;
    }

    /**
     * This method generates Jwt token for password reset functionality.
     *
     * @param email email
     * @return {@link GenericResponse}
     * @throws MessagingException javax.mail. messaging exception
     * @throws IOException        java.io. i o exception
     * @see GenericResponse
     */
    public GenericResponse createPasswordResetTokenForUser(String email) throws MessagingException, IOException {
        boolean exists = userRepository.existsByUserName(email);
        if (exists) {
            log.info("Creating password reset token for {}...", email);
            String token = jwtUtil.generateToken(email, passwordTokenExpiration);
            userRepository.insertResetPasswordToken(token, email);
            String resetPasswordLink = "http://prominent-title-dev.simform.solutions/auth/reset-password/" + token;
            emailUtil.constructResetTokenEmail(email, resetPasswordLink);
            return new GenericResponse(true, "We have sent a reset password link to your email. Please check.", email, HttpStatus.OK.value());
        } else {
            throw new UserNotFoundException(email);
        }
    }

    /**
     * This method verifies token using expiry date
     *
     * @param token token
     * @return {@link boolean}
     */
    private boolean verifyToken(String token) {
        log.info("Verifying reset password Token...");
        Optional<User> optionalUser = userRepository.findByForgotPasswordAccessKey(token);
        return optionalUser.isPresent() && !jwtUtil.isTokenExpired(token);
    }

    /**
     * This method resets user password after verifying the token.
     *
     * @param resetPasswordDto   resetPasswordDto
     * @param resetPasswordToken resetPasswordToken
     * @return {@link boolean}
     */
    public boolean resetPassword(ResetPasswordDto resetPasswordDto, String resetPasswordToken) {

        boolean isValid = verifyToken(resetPasswordToken);
        Optional<User> optionalUser = userRepository.findByForgotPasswordAccessKey(resetPasswordDto.getToken());
        if (isValid && optionalUser.isPresent() && resetPasswordDto.getPassword().equals(resetPasswordDto.getRepeatPassword())) {
            log.info("Running reset Password for user {}...", jwtUtil.extractUsername(resetPasswordDto.getToken()));
            User user = optionalUser.get();
            user.setForgotPasswordAccessKey(null);
            user.setUserPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
            user.setPasswordChangedDate(LocalDate.now());
            user.setUserStatus("ACTIVE");
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
}
