package com.training.moneybags.repository;

import com.training.moneybags.model.KycDocument;
import com.training.moneybags.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KycDocumentRepository extends JpaRepository<KycDocument, Long> {
    List<KycDocument> findByCustomerId(String customerId);
}
