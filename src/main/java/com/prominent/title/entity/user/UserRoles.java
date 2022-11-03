package com.prominent.title.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_roles")
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userRoleId;
    @ManyToOne(targetEntity = User.class, cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "userId", name = "user_id")
    private User user;
    @ManyToOne(targetEntity = Role.class, cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "roleId", name = "role_id")
    private Role role;
    //    @Column(nullable = false)
    private LocalDate activeFrom;
    //    @Column(nullable = false)
    private LocalDate activeTo;
    //    @Column(nullable = false)
    private int createUserId;
    private int updateUserId;
    //    @Column(nullable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    //    @Column(nullable = false,length = 100)
    private int internalKey;
    //    @Column(nullable = false,length = 10)
    private int concurrencyKey;
}
