package com.ddr.penerimaandocument.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.UUID;
import com.ddr.penerimaandocument.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,UUID> {
    
    public List<UserRole> findAllByUsername(String username);
}
