package com.ddr.penerimaandocument.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "company")
@Data
public class Company {

    // set sequential
    @Id
    private String companyId;
    
    private String companyName;
    private String companyAddress;

    @CreationTimestamp
    private Date joinDate;

    @UpdateTimestamp
    private Date updatedDate;

    private String contactNumber;
    private String contactName;

}
