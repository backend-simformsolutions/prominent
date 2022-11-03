package com.prominent.title.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PaymentInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentInformationId;
    private String paymentMethod;
    private String recipientName;
    private String address;
    private String city;
    private String county;
    private String state;
    private String zipCode;
    private String bankAccountNumber;
    private String routingNumber;
    private String fedexTrackingId;
}