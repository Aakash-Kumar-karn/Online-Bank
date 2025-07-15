package com.bank.insurance.insurance_service.model;

import com.bank.insurance.insurance_service.enums.InsuranceType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "available_insurances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableInsurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InsuranceType type;

    private String description;

    private Double premiumAmount;

    private Double coverageAmount;

    private Integer durationInYears;
}
