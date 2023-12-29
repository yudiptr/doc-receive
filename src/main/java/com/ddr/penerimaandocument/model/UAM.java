package com.ddr.penerimaandocument.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name= "uam")
public class UAM {

    @Id
    @GeneratedValue
    private Integer id;
    
    private String uamName;
}
