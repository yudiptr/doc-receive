package com.ddr.penerimaandocument.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ddr.penerimaandocument.model.DocumentType;
import java.util.List;
import com.ddr.penerimaandocument.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document,String> {

    List<Document> findByType(DocumentType type);

    @Query("SELECT c.documentId FROM Document c WHERE c.createdAt = (SELECT MAX(c2.createdAt) FROM Document c2) AND c.type = IN")
    String findLastIn();


    @Query("Select d FROM Document d WHERE d.status = DRAFT AND d.company.companyId = :comId")
    List<Document> findByCompanyDraft(@Param("comId") String companyId);

    @Query("Select d FROM Document d WHERE d.status = SUBMITTED AND d.company.companyId = :comId")
    List<Document> findByCompanySubmitted(@Param("comId") String companyId);


}
