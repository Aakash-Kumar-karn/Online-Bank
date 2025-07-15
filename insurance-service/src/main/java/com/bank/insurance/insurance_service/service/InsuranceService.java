package com.bank.insurance.insurance_service.service;

import com.bank.insurance.insurance_service.dto.AvailableInsuranceDto;
import com.bank.insurance.insurance_service.dto.UserInsuranceDto;
import com.bank.insurance.insurance_service.dto.UserInsuranceResponseDto;

import java.util.List;

public interface InsuranceService {
    AvailableInsuranceDto issueInsurance(AvailableInsuranceDto dto); // Admin creates new plan

    List<AvailableInsuranceDto> getAllAvailableInsurancePlans();

    String purchaseInsurance(UserInsuranceDto dto);

    List<UserInsuranceResponseDto> getUserInsurances(Long userId);


}