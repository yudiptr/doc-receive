package com.ddr.penerimaandocument.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ddr.penerimaandocument.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
