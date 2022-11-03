package com.prominent.title.entity.resource;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.prominent.title.entity.user.Organization;
import com.prominent.title.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeneralAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer addressId;

    private String address;

    private String state;

    private String city;

    private String zipCode;

    @Column(nullable = false)
    private int createUserId;
    private int updateUserId;
    @Column(nullable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    @Column(nullable = false, length = 100)
    private String internalKey;
    @Column(nullable = false, length = 10)
    private String concurrencyKey;

    @Column(columnDefinition = "boolean default false")
    private boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = User.class)
    @JoinColumn(referencedColumnName = "userId", name = "userId")
    @ToString.Exclude
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "organizationId", name = "organizationId")
    @ToString.Exclude
    private Organization organization;

}
