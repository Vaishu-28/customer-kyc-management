package com.training.moneybags.dao;

import com.training.moneybags.dao.MoneyDAO;
import com.training.moneybags.model.Customer;
import com.training.moneybags.model.KycDocument;
import com.training.moneybags.repository.CustomerRepository;
import com.training.moneybags.repository.KycDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class MoneyDAOImpl implements MoneyDAO {

    private final Random random = new Random();
    private final AtomicInteger counter = new AtomicInteger(1);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private KycDocumentRepository kycDocumentRepository;

    private String generateUniqueCif() {
        String cif = "";
        Customer exists;
        boolean result = true;

        while(result) {
            cif = String.format("%07d", random.nextInt(10_000_000)); // 7-digit number
            exists = customerRepository.findByCifNumber(cif);
            result = false;
        }
        return cif;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        customer.setCustomerId("CUST" + String.format("%03d", counter.getAndIncrement()));
        String cif = generateUniqueCif();
        customer.setCifNumber(cif);
        return customerRepository.save(customer);
    }

    @Override
    public Customer findById(String customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public KycDocument saveKycDocument(KycDocument kycDocument) {
        return kycDocumentRepository.save(kycDocument);
    }

    @Override
    public List<KycDocument> findKycByCustomerId(String customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        return (customer != null) ? kycDocumentRepository.findByCustomerId(customer.getCustomerId()) : List.of();
    }

    @Override
    public List<KycDocument> findKycByCifNumber(String cifNumber) {
        Customer customer = customerRepository.findByCifNumber(cifNumber);
        return (customer != null) ? kycDocumentRepository.findByCustomerId(customer.getCustomerId()) : List.of();
    }

    @Override
    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public Customer findByCifNumber(String cifNumber) {
        return customerRepository.findByCifNumber(cifNumber);
    }

    @Override
    public KycDocument findById(Long kycId) {
        return kycDocumentRepository.findById(kycId)
                .orElseThrow(() -> new RuntimeException("KYC Document not found with id: " + kycId));
    }


    @Override
    public KycDocument save(KycDocument kycDocument) {
        return kycDocumentRepository.save(kycDocument);
    }
}
