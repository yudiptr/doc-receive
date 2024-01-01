package com.ddr.penerimaandocument.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name= "uam_role_join")
public class Uam_Role_Join {

    @Id
    @GeneratedValue
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission")
    private UAM uam;
}
