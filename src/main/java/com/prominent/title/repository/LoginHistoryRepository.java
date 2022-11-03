package com.prominent.title.repository;

import com.prominent.title.entity.user.LoginHistory;
import com.prominent.title.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {

    @Query(value = "Update LoginHistory l SET l.lastLogoutTime = ?2 WHERE l.user = ?1 AND l.loginResultName = 'SUCCESS' AND l.lastLogoutTime IS NULL")
    @Transactional
    @Modifying
    void updateLogoutTimeByUser(@Param("user") User user, @Param("logoutTime") LocalDateTime logoutTime);
}
