package com.medibridge.user_service.service.impl;

import com.medibridge.user_service.dto.PatientProfileDTO;
import com.medibridge.user_service.dto.PatientRegisterRequest;
import com.medibridge.user_service.entity.AccountStatus;
import com.medibridge.user_service.entity.PatientProfile;
import com.medibridge.user_service.entity.User;
import com.medibridge.user_service.repository.PatientProfileRepository;
import com.medibridge.user_service.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientProfileServiceImpl implements PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;

    @Override
    public PatientProfile createPatientProfile(User user, PatientRegisterRequest request) {
        log.info("Creating patient profile for user: {}", user.getUsername());

        PatientProfile patientProfile = PatientProfile.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .country(request.getCountry())
                .insuranceProvider(request.getInsuranceProvider())
                .insurancePolicyNumber(request.getInsurancePolicyNumber())
                .insuranceGroupNumber(request.getInsuranceGroupNumber())
                .insurancePolicyExpiryDate(request.getInsurancePolicyExpiryDate())
                .bloodGroup(request.getBloodGroup())
                .medicalConditions(request.getMedicalConditions())
                .allergies(request.getAllergies())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .emergencyContactRelation(request.getEmergencyContactRelation())
                .secondaryEmergencyContactName(request.getSecondaryEmergencyContactName())
                .secondaryEmergencyContactPhone(request.getSecondaryEmergencyContactPhone())
                .secondaryEmergencyContactRelation(request.getSecondaryEmergencyContactRelation())
                .preferredLanguage(request.getPreferredLanguage() != null ? request.getPreferredLanguage() : "en")
                .notificationsEnabled(true)
                .communicationPreference(request.getCommunicationPreference() != null ? request.getCommunicationPreference() : "EMAIL")
                .gdprConsent(request.getGdprConsent() != null ? request.getGdprConsent() : false)
                .termsAndConditionsAccepted(request.getTermsAndConditionsAccepted() != null ? request.getTermsAndConditionsAccepted() : false)
                .privacyPolicyAccepted(request.getPrivacyPolicyAccepted() != null ? request.getPrivacyPolicyAccepted() : false)
                .accountCreatedDate(System.currentTimeMillis())
                .status(AccountStatus.PENDING_VERIFICATION)
                .twoFactorAuthenticationEnabled(false)
                .medicalHistoryAccessGranted(false)
                .createdAt(System.currentTimeMillis())
                .build();

        return patientProfileRepository.save(patientProfile);
    }

    @Override
    public Optional<PatientProfile> getPatientProfileByUserId(UUID userId) {
        return patientProfileRepository.findByUserId(userId);
    }

    @Override
    public Optional<PatientProfile> getPatientProfileByUsername(String username) {
        return patientProfileRepository.findByUsername(username);
    }

    @Override
    public Optional<PatientProfile> getPatientProfileByEmail(String email) {
        return patientProfileRepository.findByEmail(email);
    }

    @Override
    public Optional<PatientProfile> getPatientByInsurancePolicyNumber(String policyNumber) {
        return patientProfileRepository.findByInsurancePolicyNumber(policyNumber);
    }

    @Override
    public PatientProfile updatePatientProfile(UUID userId, PatientProfileDTO dto) {
        PatientProfile patientProfile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user: " + userId));

        if (dto.getFirstName() != null) patientProfile.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) patientProfile.setLastName(dto.getLastName());
        if (dto.getDateOfBirth() != null) patientProfile.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getGender() != null) patientProfile.setGender(dto.getGender());
        if (dto.getPhoneNumber() != null) patientProfile.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getAddress() != null) patientProfile.setAddress(dto.getAddress());
        if (dto.getCity() != null) patientProfile.setCity(dto.getCity());
        if (dto.getState() != null) patientProfile.setState(dto.getState());
        if (dto.getZipCode() != null) patientProfile.setZipCode(dto.getZipCode());
        if (dto.getCountry() != null) patientProfile.setCountry(dto.getCountry());
        if (dto.getInsuranceProvider() != null) patientProfile.setInsuranceProvider(dto.getInsuranceProvider());
        if (dto.getBloodGroup() != null) patientProfile.setBloodGroup(dto.getBloodGroup());
        if (dto.getMedicalConditions() != null) patientProfile.setMedicalConditions(dto.getMedicalConditions());
        if (dto.getAllergies() != null) patientProfile.setAllergies(dto.getAllergies());
        if (dto.getEmergencyContactName() != null) patientProfile.setEmergencyContactName(dto.getEmergencyContactName());
        if (dto.getEmergencyContactPhone() != null) patientProfile.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        if (dto.getPreferredLanguage() != null) patientProfile.setPreferredLanguage(dto.getPreferredLanguage());
        if (dto.getCommunicationPreference() != null) patientProfile.setCommunicationPreference(dto.getCommunicationPreference());
        if (dto.getStatus() != null) patientProfile.setStatus(dto.getStatus());

        log.info("Updated patient profile for user: {}", userId);
        return patientProfileRepository.save(patientProfile);
    }

    @Override
    public List<PatientProfile> getPatientsByBloodGroup(String bloodGroup) {
        return patientProfileRepository.findByBloodGroup(bloodGroup);
    }

    @Override
    public List<PatientProfile> getPatientsWithMedicalHistoryAccess() {
        return patientProfileRepository.findByMedicalHistoryAccessGrantedTrue();
    }

    @Override
    public List<PatientProfile> getPatientsWithTwoFactorAuth() {
        return patientProfileRepository.findByTwoFactorAuthenticationEnabledTrue();
    }

    @Override
    public List<PatientProfile> getPatientsByMedicalCondition(String condition) {
        return patientProfileRepository.findPatientsByMedicalCondition(condition);
    }

    @Override
    public List<PatientProfile> getPatientsWithGdprConsent() {
        return patientProfileRepository.findPatientWithGdprConsent();
    }

    @Override
    public void grantMedicalHistoryAccess(UUID userId) {
        PatientProfile patientProfile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user: " + userId));
        patientProfile.setMedicalHistoryAccessGranted(true);
        patientProfileRepository.save(patientProfile);
        log.info("Granted medical history access to patient: {}", userId);
    }

    @Override
    public void revokeMedicalHistoryAccess(UUID userId) {
        PatientProfile patientProfile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user: " + userId));
        patientProfile.setMedicalHistoryAccessGranted(false);
        patientProfileRepository.save(patientProfile);
        log.info("Revoked medical history access from patient: {}", userId);
    }

    @Override
    public void enableTwoFactorAuth(UUID userId) {
        PatientProfile patientProfile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user: " + userId));
        patientProfile.setTwoFactorAuthenticationEnabled(true);
        patientProfileRepository.save(patientProfile);
        log.info("Enabled 2FA for patient: {}", userId);
    }

    @Override
    public void disableTwoFactorAuth(UUID userId) {
        PatientProfile patientProfile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user: " + userId));
        patientProfile.setTwoFactorAuthenticationEnabled(false);
        patientProfileRepository.save(patientProfile);
        log.info("Disabled 2FA for patient: {}", userId);
    }

    @Override
    public void updateLastLoginTime(UUID userId) {
        PatientProfile patientProfile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user: " + userId));
        patientProfile.setLastLoginDate(System.currentTimeMillis());
        patientProfileRepository.save(patientProfile);
    }

    @Override
    public void updateLastConsultationDate(UUID userId) {
        PatientProfile patientProfile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user: " + userId));
        patientProfile.setLastConsultationDate(System.currentTimeMillis());
        patientProfileRepository.save(patientProfile);
    }

    @Override
    public void acceptGdprAndTerms(UUID userId) {
        PatientProfile patientProfile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user: " + userId));
        patientProfile.setGdprConsent(true);
        patientProfile.setTermsAndConditionsAccepted(true);
        patientProfile.setPrivacyPolicyAccepted(true);
        patientProfileRepository.save(patientProfile);
        log.info("Patient accepted GDPR and terms: {}", userId);
    }

    @Override
    public PatientProfileDTO toDTO(PatientProfile profile) {
        if (profile == null) return null;

        return PatientProfileDTO.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .status(profile.getStatus())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .phoneNumber(profile.getPhoneNumber())
                .address(profile.getAddress())
                .city(profile.getCity())
                .state(profile.getState())
                .zipCode(profile.getZipCode())
                .country(profile.getCountry())
                .insuranceProvider(profile.getInsuranceProvider())
                .insurancePolicyNumber(profile.getInsurancePolicyNumber())
                .insuranceGroupNumber(profile.getInsuranceGroupNumber())
                .insurancePolicyExpiryDate(profile.getInsurancePolicyExpiryDate())
                .bloodGroup(profile.getBloodGroup())
                .medicalHistoryAccessGranted(profile.getMedicalHistoryAccessGranted())
                .medicalConditions(profile.getMedicalConditions())
                .allergies(profile.getAllergies())
                .currentMedications(profile.getCurrentMedications())
                .emergencyContactName(profile.getEmergencyContactName())
                .emergencyContactPhone(profile.getEmergencyContactPhone())
                .emergencyContactRelation(profile.getEmergencyContactRelation())
                .secondaryEmergencyContactName(profile.getSecondaryEmergencyContactName())
                .secondaryEmergencyContactPhone(profile.getSecondaryEmergencyContactPhone())
                .secondaryEmergencyContactRelation(profile.getSecondaryEmergencyContactRelation())
                .preferredLanguage(profile.getPreferredLanguage())
                .notificationsEnabled(profile.getNotificationsEnabled())
                .communicationPreference(profile.getCommunicationPreference())
                .lastConsultationDate(profile.getLastConsultationDate())
                .preferredDoctorIds(profile.getPreferredDoctorIds())
                .gdprConsent(profile.getGdprConsent())
                .termsAndConditionsAccepted(profile.getTermsAndConditionsAccepted())
                .privacyPolicyAccepted(profile.getPrivacyPolicyAccepted())
                .accountCreatedDate(profile.getAccountCreatedDate())
                .lastLoginDate(profile.getLastLoginDate())
                .twoFactorAuthenticationEnabled(profile.getTwoFactorAuthenticationEnabled())
                .build();
    }
}

