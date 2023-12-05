package com.ddr.penerimaandocument.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.UUID;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "document")
@Data
public class Document {

    private static int counter = 0;
    
    @GeneratedValue
    private UUID id;

    // set sequential
    @Id
    private String documentId;


    @CreationTimestamp
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private String description;
    private String receivedBy;
    private String contactName;
    private String contactNumber;
    private String iamgeUrl;


    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    @PrePersist
    protected void onCreate() {
        if (documentId == null || documentId.isEmpty()) {
            synchronized (Document.class) {
                counter++;
                documentId = "CID" + counter;
            }
        }
    }
}