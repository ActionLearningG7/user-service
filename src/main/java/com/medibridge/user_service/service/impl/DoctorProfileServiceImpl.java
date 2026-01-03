package com.medibridge.user_service.service.impl;

import com.medibridge.user_service.dto.DoctorProfileDTO;
import com.medibridge.user_service.dto.DoctorRegisterRequest;
import com.medibridge.user_service.entity.AccountStatus;
import com.medibridge.user_service.entity.DoctorProfile;
import com.medibridge.user_service.entity.User;
import com.medibridge.user_service.repository.DoctorProfileRepository;
import com.medibridge.user_service.service.DoctorProfileService;
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
public class DoctorProfileServiceImpl implements DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;

    @Override
    public DoctorProfile createDoctorProfile(User user, DoctorRegisterRequest request) {
        log.info("Creating doctor profile for user: {}", user.getUsername());

        if (request.getLicenseNumber() == null || request.getLicenseNumber().isEmpty()) {
            throw new IllegalArgumentException("License number is required for doctor registration");
        }

        DoctorProfile doctorProfile = DoctorProfile.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .licenseNumber(request.getLicenseNumber())
                .licenseIssuingBody(request.getLicenseIssuingBody())
                .licenseExpiryDate(request.getLicenseExpiryDate())
                .specialization(request.getSpecialization())
                .medicalRegistration(request.getMedicalRegistration())
                .npiNumber(request.getNpiNumber())
                .department(request.getDepartment())
                .qualifications(request.getQualifications())
                .clinicAddress(request.getClinicAddress())
                .clinicPhoneNumber(request.getClinicPhoneNumber())
                .consultationHours(request.getConsultationHours())
                .maxPatientsPerDay(request.getMaxPatientsPerDay())
                .consultationDurationMinutes(request.getConsultationDurationMinutes() != null ? request.getConsultationDurationMinutes() : 30)
                .consultationFee(request.getConsultationFee())
                .boardCertifications(request.getBoardCertifications())
                .yearsOfExperience(request.getYearsOfExperience())
                .languages(request.getLanguages())
                .isAcceptingNewPatients(request.getIsAcceptingNewPatients() != null ? request.getIsAcceptingNewPatients() : true)
                .officeLocation(request.getOfficeLocation())
                .affiliatedHospitals(request.getAffiliatedHospitals())
                .status(AccountStatus.PENDING_VERIFICATION)
                .verificationStatus(false)
                .totalConsultations(0)
                .averageRating(0.0)
                .build();

        return doctorProfileRepository.save(doctorProfile);
    }

    @Override
    public Optional<DoctorProfile> getDoctorProfileByUserId(UUID userId) {
        return doctorProfileRepository.findByUserId(userId);
    }

    @Override
    public Optional<DoctorProfile> getDoctorProfileByUsername(String username) {
        return doctorProfileRepository.findByUsername(username);
    }

    @Override
    public Optional<DoctorProfile> getDoctorByLicenseNumber(String licenseNumber) {
        return doctorProfileRepository.findByLicenseNumber(licenseNumber);
    }

    @Override
    public Optional<DoctorProfile> getDoctorByNpiNumber(String npiNumber) {
        return doctorProfileRepository.findByNpiNumber(npiNumber);
    }

    @Override
    public DoctorProfile updateDoctorProfile(UUID userId, DoctorProfileDTO dto) {
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user: " + userId));

        if (dto.getFirstName() != null) doctorProfile.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) doctorProfile.setLastName(dto.getLastName());
        if (dto.getSpecialization() != null) doctorProfile.setSpecialization(dto.getSpecialization());
        if (dto.getDepartment() != null) doctorProfile.setDepartment(dto.getDepartment());
        if (dto.getQualifications() != null) doctorProfile.setQualifications(dto.getQualifications());
        if (dto.getClinicAddress() != null) doctorProfile.setClinicAddress(dto.getClinicAddress());
        if (dto.getConsultationFee() != null) doctorProfile.setConsultationFee(dto.getConsultationFee());
        if (dto.getMaxPatientsPerDay() != null) doctorProfile.setMaxPatientsPerDay(dto.getMaxPatientsPerDay());
        if (dto.getIsAcceptingNewPatients() != null) doctorProfile.setIsAcceptingNewPatients(dto.getIsAcceptingNewPatients());
        if (dto.getOfficeLocation() != null) doctorProfile.setOfficeLocation(dto.getOfficeLocation());
        if (dto.getLanguages() != null) doctorProfile.setLanguages(dto.getLanguages());
        if (dto.getStatus() != null) doctorProfile.setStatus(dto.getStatus());

        log.info("Updated doctor profile for user: {}", userId);
        return doctorProfileRepository.save(doctorProfile);
    }

    @Override
    public List<DoctorProfile> getDoctorsBySpecialization(String specialization) {
        return doctorProfileRepository.findBySpecialization(specialization);
    }

    @Override
    public List<DoctorProfile> getDoctorsByDepartment(String department) {
        return doctorProfileRepository.findByDepartment(department);
    }

    @Override
    public List<DoctorProfile> getVerifiedDoctors() {
        return doctorProfileRepository.findByVerificationStatusTrue();
    }

    @Override
    public List<DoctorProfile> getVerifiedDoctorsBySpecialization(String specialization) {
        return doctorProfileRepository.findVerifiedDoctorsBySpecialization(specialization);
    }

    @Override
    public List<DoctorProfile> getVerifiedDoctorsByDepartmentSortedByRating(String department) {
        return doctorProfileRepository.findVerifiedDoctorsByDepartmentSortedByRating(department);
    }

    @Override
    public List<DoctorProfile> getDoctorsAcceptingNewPatients() {
        return doctorProfileRepository.findByIsAcceptingNewPatientsTrue();
    }

    @Override
    public void verifyDoctor(UUID userId, String verifiedBy) {
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user: " + userId));
        doctorProfile.setVerificationStatus(true);
        doctorProfile.setVerificationDate(System.currentTimeMillis());
        doctorProfile.setVerifiedBy(verifiedBy);
        doctorProfile.setStatus(AccountStatus.ACTIVE);
        doctorProfileRepository.save(doctorProfile);
        log.info("Doctor verified: {} by {}", userId, verifiedBy);
    }

    @Override
    public void rejectDoctorVerification(UUID userId) {
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user: " + userId));
        doctorProfile.setVerificationStatus(false);
        doctorProfile.setStatus(AccountStatus.INACTIVE);
        doctorProfileRepository.save(doctorProfile);
        log.info("Doctor verification rejected: {}", userId);
    }

    @Override
    public void updateAvailabilitySchedule(UUID userId, String scheduleJson) {
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user: " + userId));
        doctorProfile.setAvailabilitySchedule(scheduleJson);
        doctorProfile.setAvailabilityScheduleActive(true);
        doctorProfileRepository.save(doctorProfile);
        log.info("Updated availability schedule for doctor: {}", userId);
    }

    @Override
    public void updateConsultationFee(UUID userId, Double fee) {
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user: " + userId));
        doctorProfile.setConsultationFee(fee);
        doctorProfileRepository.save(doctorProfile);
    }

    @Override
    public void updateAverageRating(UUID userId, Double rating) {
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user: " + userId));
        doctorProfile.setAverageRating(rating);
        doctorProfileRepository.save(doctorProfile);
    }

    @Override
    public void incrementConsultationCount(UUID userId) {
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user: " + userId));
        int count = doctorProfile.getTotalConsultations() != null ? doctorProfile.getTotalConsultations() : 0;
        doctorProfile.setTotalConsultations(count + 1);
        doctorProfileRepository.save(doctorProfile);
    }

    @Override
    public DoctorProfileDTO toDTO(DoctorProfile profile) {
        if (profile == null) return null;

        return DoctorProfileDTO.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .status(profile.getStatus())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .licenseNumber(profile.getLicenseNumber())
                .licenseIssuingBody(profile.getLicenseIssuingBody())
                .licenseExpiryDate(profile.getLicenseExpiryDate())
                .specialization(profile.getSpecialization())
                .medicalRegistration(profile.getMedicalRegistration())
                .npiNumber(profile.getNpiNumber())
                .department(profile.getDepartment())
                .qualifications(profile.getQualifications())
                .clinicAddress(profile.getClinicAddress())
                .clinicPhoneNumber(profile.getClinicPhoneNumber())
                .consultationHours(profile.getConsultationHours())
                .availabilityScheduleActive(profile.getAvailabilityScheduleActive())
                .availabilitySchedule(profile.getAvailabilitySchedule())
                .maxPatientsPerDay(profile.getMaxPatientsPerDay())
                .consultationDurationMinutes(profile.getConsultationDurationMinutes())
                .consultationFee(profile.getConsultationFee())
                .boardCertifications(profile.getBoardCertifications())
                .yearsOfExperience(profile.getYearsOfExperience())
                .languages(profile.getLanguages())
                .verificationStatus(profile.getVerificationStatus())
                .verificationDate(profile.getVerificationDate())
                .verifiedBy(profile.getVerifiedBy())
                .isAcceptingNewPatients(profile.getIsAcceptingNewPatients())
                .officeLocation(profile.getOfficeLocation())
                .affiliatedHospitals(profile.getAffiliatedHospitals())
                .averageRating(profile.getAverageRating())
                .totalConsultations(profile.getTotalConsultations())
                .build();
    }
}

