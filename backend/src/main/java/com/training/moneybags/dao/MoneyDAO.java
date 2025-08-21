package com.training.moneybags.dao;

import com.training.moneybags.model.Customer;
import com.training.moneybags.model.KycDocument;

import java.util.List;
import java.util.Optional;

public interface MoneyDAO {
    Customer saveCustomer(Customer customer);
    Customer findById(String customerId);
    List<Customer> findAllCustomers();
    KycDocument saveKycDocument(KycDocument kycDocument);
    List<KycDocument> findKycByCustomerId(String customerId);
    void deleteCustomer(String customerId);
    Customer findByCifNumber(String cifNumber);
    List<KycDocument> findKycByCifNumber(String cifNumber);
    KycDocument findById(Long kycId);
    KycDocument save(KycDocument kycDocument);
}
