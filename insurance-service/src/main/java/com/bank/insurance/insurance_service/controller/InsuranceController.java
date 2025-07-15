package com.bank.insurance.insurance_service.controller;

import com.bank.insurance.insurance_service.dto.AvailableInsuranceDto;
import com.bank.insurance.insurance_service.dto.UserInsuranceDto;
import com.bank.insurance.insurance_service.dto.UserInsuranceResponseDto;
import com.bank.insurance.insurance_service.service.InsuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;

    @GetMapping("/test")
    public ResponseEntity<String> testCard() {
        return ResponseEntity.ok(" Insurance Microservice is working!");
    }

    @PostMapping("/issue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AvailableInsuranceDto> issueInsurance(@Valid @RequestBody AvailableInsuranceDto dto){
        AvailableInsuranceDto response = insuranceService.issueInsurance(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<AvailableInsuranceDto>> getAllAvailablePlans() {
        List<AvailableInsuranceDto> plans = insuranceService.getAllAvailableInsurancePlans();
        return ResponseEntity.ok(plans);
    }

    @PostMapping("/purchase")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> purchaseInsurance(@Valid @RequestBody UserInsuranceDto dto) {
        String msg = insuranceService.purchaseInsurance(dto);
        return ResponseEntity.ok(msg);
    }
    @GetMapping("/my-insurances")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserInsuranceResponseDto>> getMyInsurances(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return ResponseEntity.ok(insuranceService.getUserInsurances(userId));
    }


}
