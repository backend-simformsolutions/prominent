package com.prominent.title.service.user;

import com.prominent.title.entity.user.User;
import com.prominent.title.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


/**
 * This class contains loadUserByUsername method for security purposes.
 */
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method return UserDetails object using user's username
     *
     * @param username username
     * @return {@link UserDetails}
     * @see UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            Optional<User> user = userRepository.findByUserName(username);
            if (user.isPresent())
                return new org.springframework.security.core.userdetails.User(user.get().getUserName(), user.get().getUserPassword(), new ArrayList<>());
            else
                throw new UsernameNotFoundException("Username not found");
        } catch (UsernameNotFoundException e) {
            log.debug("handling UserNotFoundException...");
            throw new UsernameNotFoundException("Username not found");
        }
    }

}
