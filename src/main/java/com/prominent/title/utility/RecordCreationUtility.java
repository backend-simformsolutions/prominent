package com.prominent.title.utility;

import com.prominent.title.dto.util.CreateRecordInformation;
import com.prominent.title.dto.util.UpdateRecordInformation;
import com.prominent.title.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.time.LocalDateTime;


/**
 * This Utility is used for creating/updating general object which holds information
 * Which is required for creating/updating any entry for any entity
 * It also gets usedId of currently logged-in user from SecurityContext
 */
@Configuration
public class RecordCreationUtility {
    private final UserRepository userRepository;

    public RecordCreationUtility(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * This method is used for creating general object CreateRecordInformation Class
     *
     * @return {@link CreateRecordInformation}
     * @see CreateRecordInformation
     */
    public CreateRecordInformation putNewRecordInformation() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null || principal.getName().equals("anonymousUser"))
            return new CreateRecordInformation(true, -1, LocalDateTime.now(), RandomString.make(30), RandomString.make(10));
        return new CreateRecordInformation(true, userRepository.findUserIdByUserName(principal.getName()), LocalDateTime.now(), RandomString.make(30), RandomString.make(10));
    }

    /**
     * This method is used for updating general object using UpdateRecordInformation Class
     *
     * @return {@link UpdateRecordInformation}
     * @see UpdateRecordInformation
     */
    public UpdateRecordInformation updateRecordInformation() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null)
            return new UpdateRecordInformation(-1, LocalDateTime.now());
        return new UpdateRecordInformation(userRepository.findUserIdByUserName(principal.getName()), LocalDateTime.now());
    }
}
