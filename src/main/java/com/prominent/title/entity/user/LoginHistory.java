package com.prominent.title.entity.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "login_histories")
@EqualsAndHashCode(exclude = "user")
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int loginHistoryId;
    @Column(length = 50)
    private String loginIpAddress;
    private int sessionId;
    private LocalDateTime lastLoginTime;
    private LocalDateTime lastLogoutTime;
    private String errorMsg;
    @Column(length = 50)
    private String loginResultName;
    private String serverName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}
