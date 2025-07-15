package com.bank.insurance.insurance_service.service.Impl;

import com.bank.insurance.insurance_service.dto.AvailableInsuranceDto;
import com.bank.insurance.insurance_service.dto.NotificationRequestDto;
import com.bank.insurance.insurance_service.dto.UserInsuranceDto;
import com.bank.insurance.insurance_service.dto.UserInsuranceResponseDto;
import com.bank.insurance.insurance_service.exception.UserNotFoundException;
import com.bank.insurance.insurance_service.feign.NotificationClient;
import com.bank.insurance.insurance_service.feign.UserClient;
import com.bank.insurance.insurance_service.model.AvailableInsurance;
import com.bank.insurance.insurance_service.model.UserInsurance;
import com.bank.insurance.insurance_service.repository.AvailableInsuranceRepository;
import com.bank.insurance.insurance_service.repository.UserInsuranceRepository;
import com.bank.insurance.insurance_service.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

 public class InsuranceServiceImpl implements InsuranceService {
    private final AvailableInsuranceRepository availableInsuranceRepository;
    private final UserInsuranceRepository userInsuranceRepository;
    private final UserClient userClient;
    private final NotificationClient notificationClient;
    @Override
    public AvailableInsuranceDto issueInsurance(AvailableInsuranceDto dto) {
        AvailableInsurance availableInsurance = new AvailableInsurance();
        availableInsurance.setType(dto.getType());
        availableInsurance.setDescription(dto.getDescription());
        availableInsurance.setCoverageAmount(dto.getCoverageAmount());
        availableInsurance.setPremiumAmount(dto.getPremiumAmount());
        availableInsurance.setDurationInYears(dto.getDurationInYears());

        AvailableInsurance saved = availableInsuranceRepository.save(availableInsurance);

        return mapToDto(saved);
    }

    private AvailableInsuranceDto mapToDto(AvailableInsurance insurance) {
        return new AvailableInsuranceDto(
                insurance.getType(),
                insurance.getDescription(),
                insurance.getPremiumAmount(),
                insurance.getCoverageAmount(),
                insurance.getDurationInYears()
        );
    }

    @Override
    public List<AvailableInsuranceDto> getAllAvailableInsurancePlans() {

        List<AvailableInsurance> list = availableInsuranceRepository.findAll();
         return list.stream()
                 .map(this::mapToDto)
                 .toList();
    }

    @Override
    public String purchaseInsurance(UserInsuranceDto dto) {

        boolean userExists = userClient.checkUserExists(dto.getUserId());
        if (!userExists) {
            throw new UserNotFoundException("User not found with ID: " + dto.getUserId());
        }

        AvailableInsurance insurance = availableInsuranceRepository.findById(dto.getInsuranceId())
                .orElseThrow(() -> new UserNotFoundException("Insurance not found"));

        UserInsurance userInsurance = new UserInsurance();
        userInsurance.setUserId(dto.getUserId());
        userInsurance.setInsurance(insurance);
        userInsurance.setPurchaseDate(LocalDate.now());
        userInsurance.setExpiryDate(LocalDate.now().plusYears(insurance.getDurationInYears()));

        userInsuranceRepository.save(userInsurance);

        NotificationRequestDto notification = new NotificationRequestDto();
        notification.setUserId(dto.getUserId());
        notification.setType("email");
        notification.setTitle("Insurance Purchase Confirmation");
        notification.setMsg("You have successfully purchased the " + insurance.getType() + " insurance plan.");

        try {
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }


        return "Insurance purchased successfully";
    }
    @Override
    public List<UserInsuranceResponseDto> getUserInsurances(Long userId) {

        // Fetch all insurance records purchased by the user
        List<UserInsurance> userPolicies = userInsuranceRepository.findByUserId(userId);

        // Convert each UserInsurance entity to a flattened UserInsuranceResponseDto
        return userPolicies.stream().map(policy -> {

            AvailableInsurance insurance = policy.getInsurance(); // CORRECT

            UserInsuranceResponseDto dto = new UserInsuranceResponseDto();
            dto.setPlanName(insurance.getType().name());              // from AvailableInsurance entity
            dto.setDescription(insurance.getDescription());          // from AvailableInsurance entity
            dto.setAmount(insurance.getPremiumAmount());             // from AvailableInsurance entity
            dto.setDurationInYears(insurance.getDurationInYears());  // from AvailableInsurance entity
            dto.setPurchaseDate(policy.getPurchaseDate());           // from UserInsurance entity
            dto.setExpiryDate(policy.getExpiryDate());               // from UserInsurance entity

            return dto;

        }).collect(Collectors.toList());
    }




}