package com.ddr.penerimaandocument.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ddr.penerimaandocument.model.Uam_Role_Join;

public interface UAM_JoinRepository extends JpaRepository<Uam_Role_Join, Integer> {
    

    @Query("Select u FROM Uam_Role_Join u WHERE u.role.id = :role AND u.uam.id = :permiss")
    List<Uam_Role_Join> findExactRole(@Param("role") Integer role, @Param("permiss") Integer permiss);

    @Query("SELECT U.uam.uamName FROM Uam_Role_Join U WHERE U.role.id = :role")
    List<String> findMenuByRole(@Param("role") Integer role);
}
