package com.ddr.penerimaandocument.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.EnumType;

@Entity
@Table(name = "document")
@Data
public class Document {
    // set sequential
    @Id
    private String documentId;


    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "company_name")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "vendor_name")
    private Vendor vendor;

    private String description;
    private String receivedBy;
    private String contactName;
    private String contactNumber;
    private String documentPath;
    private String createdBy;
    private String updatedBy;
    private String invoiceRef;


    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private DocumentType type;
}