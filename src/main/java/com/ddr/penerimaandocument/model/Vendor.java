package com.ddr.penerimaandocument.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "vendor")
@Data
public class Vendor {
    // set sequential
    @Id
    private String vendorId;
    
    private String vendorName;
    private String vendorAddress;

    @CreationTimestamp
    private Date joinDate;

    private String contactNumber;
    private String contactName;
}
