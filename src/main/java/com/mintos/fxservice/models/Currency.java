package com.mintos.fxservice.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;
}
