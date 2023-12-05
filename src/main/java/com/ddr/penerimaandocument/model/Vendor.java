package com.ddr.penerimaandocument.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.UUID;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "vendor")
@Data
public class Vendor {

    private static int counter = 0;

    @GeneratedValue
    private UUID id;

    // set sequential
    @Id
    private String vendorId;
    
    private String vendorName;
    private String vendorAddress;

    @CreationTimestamp
    private Date joinDate;

    private String contactNumber;
    private String contactName;

    @PrePersist
    protected void onCreate() {
        if (vendorId == null || vendorId.isEmpty()) {
            synchronized (Vendor.class) {
                counter++;
                vendorId = "VEID" + counter;
            }
        }
    }
}
