package com.ddr.penerimaandocument.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name= "_user")
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    
    @Column(unique = true)
    
    private String username;
    private String password;

    
    private String role;
}
