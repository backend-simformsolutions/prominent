package com.prominent.title.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "userRoles")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int roleId;
    @Column(nullable = false, length = 50)
    private String roleCode;
    @Column(nullable = false, length = 50)
    private String roleName;
    @Column(nullable = false)
    private LocalDate activeFrom;
    @Column(nullable = false)
    private LocalDate activeTo;
    @Column(nullable = false)
    private int createUserId;
    private int updateUserId;
    @Column(nullable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    @Column(nullable = false, length = 50)
    private String internalKey;
    @Column(nullable = false, length = 10)
    private String concurrencyKey;
    private int displayOrder;

    @JsonIgnore
    @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE)
    private Set<UserRoles> userRoles;
}
