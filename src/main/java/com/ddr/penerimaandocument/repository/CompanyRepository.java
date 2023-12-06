package com.ddr.penerimaandocument.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ddr.penerimaandocument.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company,String> {
    

    @Query("SELECT c.companyId FROM Company c WHERE c.joinDate = (SELECT MAX(c2.joinDate) FROM Company c2)")
    String findLastInserted();
}
