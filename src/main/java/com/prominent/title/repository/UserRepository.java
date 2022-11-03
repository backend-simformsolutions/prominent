package com.prominent.title.repository;


import com.prominent.title.entity.user.User;
import com.prominent.title.projection.UserListDtoProjection;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByUserName(String userName);

    @Query(value = "UPDATE users SET failed_logons = ?1 WHERE user_name = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateFailedAttempts(@Param("failedLogons") int failedLogons, @Param("userName") String userName);

    @Query(value = "UPDATE users SET total_logons = ?1 WHERE user_name = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void increaseLogonAttempt(@Param("totalLogons") int totalLogons, @Param("userName") String userName);

    @Query(value = "UPDATE users SET forgot_password_access_key = ?1,user_status ='PASSWORD_RESET' WHERE user_name = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void insertResetPasswordToken(@Param("token") String token, @Param("email") String email);

    boolean existsByUserName(String userName);

    @Query(value = "SELECT u.userId FROM User u WHERE u.userName = ?1")
    int findUserIdByUserName(@Param("userName") String userName);

    Optional<User> findByForgotPasswordAccessKey(@Param("token") String token);

    @Query("select u.userId as userId,u.userName as userName,u.firstName as firstName,u.lastName as lastName,r.roleCode as role,u.contactNumber as contactNumber, al.address as address, al.city as city, al.state as state, al.zipCode as zipCode " +
            "from User u " +
            "inner join u.userRoles userRoles " +
            "join userRoles.role r " +
            "left join u.addressList al " +
            "where al.isDefault = true or al is null ")
    Page<UserListDtoProjection> findAllUsersPaginated(Pageable pageable);

}
