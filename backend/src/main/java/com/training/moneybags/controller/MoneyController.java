package com.training.moneybags.controller;

import com.training.moneybags.dtos.UserCreateRequest;
import com.training.moneybags.dtos.UserResponse;
import com.training.moneybags.model.Customer;
import com.training.moneybags.model.KycDocument;
import com.training.moneybags.model.VerificationRequest;
import com.training.moneybags.service.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200", "http://localhost:8000" })
@RequestMapping("/customers")
public class MoneyController {

    @Autowired
    private MoneyService moneyservice;
    @Autowired
    private WebClient userWebClient;

    //---------------------CUSTOMER------------------

    @PreAuthorize("hasRole('TELLER')")
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        // Check if CIF already exists
        if (moneyservice.getCustomerByCif(customer.getCifNumber()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Customer with CIF number already exists: " + customer.getCifNumber());
        }

        UserResponse userResponse = userWebClient.post()
                .uri("register")
                .bodyValue(new UserCreateRequest(
                    customer.getFirstName().toLowerCase() + "." + customer.getLastName().toLowerCase(),
                    "test",
                    List.of("CUSTOMER")
                )
        )
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();

        customer.setUserId(userResponse.id());
        Customer saved = moneyservice.saveCustomer(customer);
        return ResponseEntity
                .created(URI.create("/customers/" + saved.getCustomerId()))
                .body(saved);
    }

    @PreAuthorize("hasRole('TELLER')")
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId) {
        Customer customer = moneyservice.findById(customerId);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAnyRole('OPS_ADMIN','TELLER')")
    @GetMapping
    public List<Customer> getAllCustomers() {
        return moneyservice.findAllCustomers();
    }

    @PreAuthorize("hasAnyRole('OPS_ADMIN','TELLER')")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String customerId) {
        Customer customer = moneyservice.findById(customerId);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer not found with ID: " + customerId);
        }

        moneyservice.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('OPS_ADMIN','CUSTOMER','TELLER')")
    @GetMapping("/cif/{cifNumber}")
    public ResponseEntity<Customer> getCustomerByCif(@PathVariable String cifNumber) {
        Customer customer = moneyservice.getCustomerByCif(cifNumber);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    //-----------------KYC-----------------

    @PreAuthorize("hasAnyRole('CUSTOMER','TELLER')")
    @PostMapping("/cif/{cifNumber}/kyc")
    public ResponseEntity<KycDocument> uploadKycDocument(
            @PathVariable String cifNumber,
            @RequestBody KycDocument kycDocument) {

        Customer customer = moneyservice.getCustomerByCif(cifNumber);

        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        List<KycDocument> doc = moneyservice.findKycByCifNumber(cifNumber);
        if (!doc.isEmpty()) {
            return ResponseEntity.status(409).build();
        }

        kycDocument.setCustomerId(customer.getCustomerId());
        kycDocument.setVerificationStatus("Pending");
        KycDocument savedDoc = moneyservice.saveKycDocument(kycDocument);
        savedDoc.setCifNumber(customer.getCifNumber());
        return ResponseEntity.ok(savedDoc);
    }

//    @PreAuthorize("hasRole('OPS_ADMIN')")
//    @GetMapping("/{customerId}/kyc")
//    public ResponseEntity<List<KycDocument>> getKycDocuments(@PathVariable String customerId) {
//        Customer customer = moneyservice.findById(customerId);
//        if (customer == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(moneyservice.findKycByCustomerId(customerId));
//    }

    @PreAuthorize("hasAnyRole('CUSTOMER','TELLER')")
    @GetMapping("/cif/{cifNumber}/kyc")
    public ResponseEntity<List<KycDocument>> getKycDocumentsByCif(@PathVariable String cifNumber) {
        Customer customer = moneyservice.getCustomerByCif(cifNumber);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        List<KycDocument> docs = moneyservice.findKycByCustomerId(customer.getCustomerId());
        docs.forEach(doc -> doc.setCifNumber(customer.getCifNumber()));
        return ResponseEntity.ok(docs);
    }

    @PreAuthorize("hasRole('OPS_ADMIN')")
    @PutMapping("/kyc/{kycId}/verify")
    public ResponseEntity<?> verifyKycDocument(@PathVariable Long kycId,
                                               @RequestBody VerificationRequest request) {
        KycDocument doc = moneyservice.findById(kycId);
        if (doc == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("KYC document not found with ID: " + kycId);
        }

        if ("Verified".equalsIgnoreCase(request.getStatus())) {
            doc.setVerificationStatus("Verified");
            doc.setVerifiedAt(LocalDateTime.now());
        } else if ("Rejected".equalsIgnoreCase(request.getStatus())) {
            doc.setVerificationStatus("Rejected");
            doc.setVerifiedAt(null);
        }

        moneyservice.save(doc);
        return ResponseEntity.ok(doc);
    }
}
