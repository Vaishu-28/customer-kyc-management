package com.training.moneybags.service;

import com.training.moneybags.model.Customer;
import com.training.moneybags.model.KycDocument;

import java.util.List;

public interface MoneyService {
    Customer saveCustomer(Customer customer);
    Customer findById(String customerId);
    KycDocument saveKycDocument(KycDocument kycDocument);
    List<KycDocument> findKycByCustomerId(String customerId);
    List<Customer> findAllCustomers();
    void deleteCustomer(String customerId);
    Customer getCustomerByCif(String cifNumber);
    List<KycDocument> findKycByCifNumber(String cifNumber);
    KycDocument findById(Long kycId);
    KycDocument save(KycDocument kycDocument);
}