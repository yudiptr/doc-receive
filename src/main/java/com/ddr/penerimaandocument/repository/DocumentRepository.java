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

    @Query("SELECT c.documentId FROM Document c WHERE c.createdAt = (SELECT MAX(c2.createdAt) FROM Document c2)")
    String findLastIn();


    @Query("Select d FROM Document d WHERE d.status = DRAFT AND d.company.companyId = :comId AND d.type = :type ")
    List<Document> findByCompanyDraft(@Param("comId") String companyId, @Param("type") DocumentType type);

    @Query("Select d FROM Document d WHERE d.status = SUBMITTED AND d.company.companyId = :comId AND d.type = :type")
    List<Document> findByCompanySubmitted(@Param("comId") String companyId, @Param("type") DocumentType type);


}
