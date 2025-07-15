package com.bank.insurance.insurance_service.repository;

import com.bank.insurance.insurance_service.model.UserInsurance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInsuranceRepository extends JpaRepository<UserInsurance, Long> {
    List<UserInsurance> findByUserId(Long userId);
}