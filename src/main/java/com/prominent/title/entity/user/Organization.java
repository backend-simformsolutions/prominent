package com.prominent.title.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prominent.title.entity.resource.GeneralAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Organization {
    @JsonIgnore
    @OneToMany(targetEntity = User.class, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "organization")
    List<User> userList;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int organizationId;
    @Column(nullable = false, length = 100)
    private String organizationTypeName;
    @Column(nullable = false, unique = true)
    private String organizationName;

    private String countryId;

    @Column(nullable = false)
    private String primaryContactName;
    private String primaryContactPhone;
    @Column(nullable = false)
    private String primaryContactEmail;
    @Column(length = 50)
    private String brokerageLicenseNumber;
    private String brokerageAdminFeeAmount;
    private int logoDocumentId;
    @Column(nullable = false)
    private boolean isActive;
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
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(referencedColumnName = "paymentInformationId", name = "paymentInformationId")
    private PaymentInformation paymentInformation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = CascadeType.ALL)
    private List<GeneralAddress> addressList;

    public void addUserInOrganization(User user) {
        if (userList.isEmpty()) {
            this.primaryContactName = user.getFirstName() + " " + user.getLastName();
            this.primaryContactEmail = user.getUserName().toLowerCase();
            this.primaryContactPhone = user.getContactNumber();
        }
        this.userList.add(user);
        user.setOrganization(this);
    }

    public void addAddressInOrganization(GeneralAddress generalAddress) {
        if (addressList == null) {
            this.addressList = new ArrayList<>();
        }
        this.addressList.add(generalAddress);
        generalAddress.setOrganization(this);
    }
}
