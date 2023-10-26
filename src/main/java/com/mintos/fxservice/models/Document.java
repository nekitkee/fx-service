package com.mintos.fxservice.models;

import com.mintos.fxservice.services.transfer.TransferType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
@Table
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentNumber;

    private LocalDateTime createdAt;

    private BigDecimal amount;

    private BigDecimal rate;

    private String ccy;

    private String sourceCcy;

    private String targetCcy;

    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    private BigDecimal sourceAmount;

    private BigDecimal targetAmount;

    @ManyToOne
    private Account sourceAccount;

    @ManyToOne
    private Account targetAccount;

}
