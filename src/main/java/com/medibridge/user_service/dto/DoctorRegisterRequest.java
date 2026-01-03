package com.medibridge.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Doctor registration request with doctor-specific profile fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DoctorRegisterRequest extends RegisterRequest {
    private String firstName;
    private String lastName;
    private String licenseNumber; // Required
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
    private Integer maxPatientsPerDay;
    private Integer consultationDurationMinutes;
    private Double consultationFee;
    private String boardCertifications;
    private Long yearsOfExperience;
    private String languages;
    @lombok.Builder.Default
    private Boolean isAcceptingNewPatients = true;
    private String officeLocation;
    private String affiliatedHospitals;
}
