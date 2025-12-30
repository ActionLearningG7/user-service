package com.medibridge.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for Doctor Profile containing enterprise-level doctor/healthcare provider
 * fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DoctorProfileDTO extends UserProfileDTO {
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String licenseIssuingBody;
    private Long licenseExpiryDate;
    private String specialization;
    private String medicalRegistration;
    private String npiNumber;
    private String department;
    private String qualifications;
    private String clinicAddress;
    private String clinicPhoneNumber;
    private String consultationHours;
    private Boolean availabilityScheduleActive;
    private String availabilitySchedule;
    private Integer maxPatientsPerDay;
    private Integer consultationDurationMinutes;
    private Double consultationFee;
    private String boardCertifications;
    private Long yearsOfExperience;
    private String languages;
    private Boolean verificationStatus;
    private Long verificationDate;
    private String verifiedBy;
    private Boolean isAcceptingNewPatients;
    private String officeLocation;
    private String affiliatedHospitals;
    private Double averageRating;
    private Integer totalConsultations;
}
