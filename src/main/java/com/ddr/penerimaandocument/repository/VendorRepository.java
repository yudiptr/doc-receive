package com.ddr.penerimaandocument.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ddr.penerimaandocument.model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor,String> {
    
    @Query("SELECT c.vendorId FROM Vendor c WHERE c.joinDate = (SELECT MAX(c2.joinDate) FROM Vendor c2)")
    String findLastInserted();
}
