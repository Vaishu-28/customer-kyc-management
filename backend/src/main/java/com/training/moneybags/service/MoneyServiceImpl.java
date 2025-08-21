package com.training.moneybags.service;

import com.training.moneybags.dao.MoneyDAO;
import com.training.moneybags.model.Customer;
import com.training.moneybags.model.KycDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoneyServiceImpl implements MoneyService {

    @Autowired
    private MoneyDAO moneyDAO;

    @Override
    public Customer saveCustomer(Customer customer) {
        return moneyDAO.saveCustomer(customer);
    }

    @Override
    public Customer findById(String customerId) {
        Customer customer = moneyDAO.findById(customerId);
        return customer;
    }

    @Override
    public KycDocument saveKycDocument(KycDocument kycDocument) {
        return moneyDAO.saveKycDocument(kycDocument);
    }

    @Override
    public List<KycDocument> findKycByCustomerId(String customerId) {
        return moneyDAO.findKycByCustomerId(customerId);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return moneyDAO.findAllCustomers();
    }

    @Override
    public void deleteCustomer(String customerId) {
        moneyDAO.deleteCustomer(customerId);
    }

    @Override
    public Customer getCustomerByCif(String cifNumber) {
        Customer customer = moneyDAO.findByCifNumber(cifNumber);
        return (customer != null) ? customer : null;
    }

    @Override
    public List<KycDocument> findKycByCifNumber(String cifNumber) {
        return moneyDAO.findKycByCifNumber(cifNumber);
    }

    @Override
    public KycDocument findById(Long kycId) {
        return moneyDAO.findById(kycId);
    }

    @Override
    public KycDocument save(KycDocument doc) {
        return moneyDAO.save(doc);
    }
}
