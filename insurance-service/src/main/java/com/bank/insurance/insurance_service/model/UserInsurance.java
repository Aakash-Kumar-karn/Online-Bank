package com.bank.insurance.insurance_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_insurances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInsurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id")
    private AvailableInsurance insurance;


    private LocalDate purchaseDate;
    private LocalDate expiryDate;
}
