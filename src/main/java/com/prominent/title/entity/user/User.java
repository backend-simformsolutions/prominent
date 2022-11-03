package com.prominent.title.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prominent.title.entity.resource.GeneralAddress;
import lombok.*;
import org.apache.commons.text.WordUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"userRoles", "loginHistories"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @JsonIgnore
    @OneToMany(targetEntity = LoginHistory.class, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "user")
    @ToString.Exclude
    List<LoginHistory> loginHistories;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    //@Column(nullable = false)
    @Column(unique = true)
    private String userName;
    //@Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;
    //@Column(nullable = false)
    private String firstName;
    //@Column(nullable = false)
    private String lastName;
    //@Column(nullable = false)
    @Column(unique = true)
    private String communicationEmail;
    //@Column(nullable = false,length = 50)
    private String userType;
    private String contactNumber;
    //@Column(length = 50)
    private String forgotPasswordAccessKey;
    //@Column(length = 50)
    private String forcePasswordChange;
    private boolean isActive = true;
    private int totalLogons;
    private LocalDateTime previousLogonDate;
    private int failedLogons;
    private LocalDateTime logonDate;
    private LocalDate passwordChangedDate;
    //@Column(length = 50)
    private String userStatus;
    //@Column(nullable = false)
    private LocalDate activeFrom;
    //@Column(nullable = false)
    private LocalDate activeTo;
    //@Column(nullable = false)
    private int createUserId;
    private int updateUserId;
    //@Column(nullable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    //@Column(nullable = false,length = 100)
    private String internalKey;
    //@Column(nullable = false,length = 10)
    private String concurrencyKey;
    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;
    @Column(name = "lock_time")
    private LocalDateTime lockTime;
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(referencedColumnName = "paymentInformationId", name = "paymentInformationId")
    private PaymentInformation paymentInformation;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(referencedColumnName = "userAttributeId", name = "userAttributeId")
    private UserAttribute userAttribute;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<UserRoles> userRoles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "organizationId", name = "organizationId")
    @ToString.Exclude
    private Organization organization;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, targetEntity = GeneralAddress.class)
    @ToString.Exclude
    @JsonManagedReference
    private List<GeneralAddress> addressList;

    public void addAddressToAddressList(GeneralAddress generalAddress) {
        if (addressList == null) {
            this.addressList = new ArrayList<>();
        }
        this.addressList.add(generalAddress);
        generalAddress.setUser(this);
    }

    public String getFirstName() {
        return WordUtils.capitalizeFully(firstName);
    }

    public void setFirstName(String firstName) {
        this.firstName = WordUtils.capitalizeFully(firstName);
    }

    public String getLastName() {
        return WordUtils.capitalizeFully(lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = WordUtils.capitalizeFully(lastName);
    }
}
