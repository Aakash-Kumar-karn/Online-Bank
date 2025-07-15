package com.bank.insurance.insurance_service.repository;


import com.bank.insurance.insurance_service.model.AvailableInsurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableInsuranceRepository extends JpaRepository<AvailableInsurance, Long> {
}