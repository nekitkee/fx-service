package com.mintos.fxservice.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientNumber;

    private String name;

    @OneToMany(mappedBy = "client")
    private List<Account> accounts;
}

