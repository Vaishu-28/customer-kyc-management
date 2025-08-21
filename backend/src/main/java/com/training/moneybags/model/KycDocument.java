package com.training.moneybags.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kyc_documents")
public class KycDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="kyc_id")
    private Long id;

    @Column(name = "document_type")
    private String documentType;


    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "verification_status")
    private String verificationStatus = "Pending";


    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    //@ManyToOne
    @Column(name = "customer_id")
    private String customerId;

    @Transient
    private String cifNumber;

    public String getCifNumber() {
        return cifNumber;
    }

    public void setCifNumber(String cifNumber) {
        this.cifNumber = cifNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    // Getters and setters
}
