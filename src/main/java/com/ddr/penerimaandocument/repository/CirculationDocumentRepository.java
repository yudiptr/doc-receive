package com.ddr.penerimaandocument.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ddr.penerimaandocument.model.CirculationDocument;
import java.util.List;


@Repository
public interface CirculationDocumentRepository extends JpaRepository<CirculationDocument,String> {

    @Query("SELECT c.circulationDocId FROM CirculationDocument c WHERE c.createdAt = (SELECT MAX(c2.createdAt) FROM CirculationDocument c2)")
    String findLastIn();


    @Query("SELECT C FROM CirculationDocument C WHERE C.isClosed = false")
    List<CirculationDocument> findByIsClosed();
}
