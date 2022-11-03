package com.prominent.title.repository;

import com.prominent.title.entity.user.PaymentInformation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentInformationRepository extends JpaRepository<PaymentInformation, Integer> {

}
