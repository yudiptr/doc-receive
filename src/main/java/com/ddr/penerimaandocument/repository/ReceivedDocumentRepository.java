package com.ddr.penerimaandocument.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ddr.penerimaandocument.model.ReceivedDocument;

import java.util.List;


@Repository
public interface ReceivedDocumentRepository extends JpaRepository<ReceivedDocument,String> {

    @Query("SELECT c.receivedDocumentId FROM ReceivedDocument c WHERE c.createdAt = (SELECT MAX(c2.createdAt) FROM ReceivedDocument c2)")
    String findLastIn();


    @Query("SELECT C FROM ReceivedDocument C WHERE C.isClosed = false")
    List<ReceivedDocument> findByIsClosed();
}
