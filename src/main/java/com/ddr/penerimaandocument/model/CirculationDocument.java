package com.ddr.penerimaandocument.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "circulation_document")
@Data
public class CirculationDocument {

    // set sequential
    @Id
    private String circulationDocId;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedDate;

    @ManyToOne
    @JoinColumn(name = "company_to")
    private Company company;

    private String[] documentsId;

    private String receivedBy;
    private String refDescription;

    private boolean isClosed;
    private String createdBy;
    private String type;

}
