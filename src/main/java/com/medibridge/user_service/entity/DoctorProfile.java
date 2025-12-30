package com.medibridge.user_service.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Doctor user profile entity.
 * Contains enterprise-level doctor/healthcare provider specific fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@DiscriminatorValue("DOCTOR")
public class DoctorProfile extends UserProfile {

    private String firstName; // Doctor's first name

    private String lastName; // Doctor's last name

    private String licenseNumber; // Medical license number (required)

    private String licenseIssuingBody; // Issuing authority of the license

    private Long licenseExpiryDate; // License expiry timestamp

    private String specialization; // Medical specialization (e.g., Cardiology, Pediatrics)

    private String medicalRegistration; // Medical registration ID

    private String npiNumber; // National Provider Identifier (for US)

    private String department; // Hospital/Clinic department

    private String qualifications; // Educational qualifications (JSON or comma-separated)

    private String clinicAddress; // Primary clinic/hospital address

    private String clinicPhoneNumber; // Clinic contact number

    private String consultationHours; // JSON object with day-wise consultation hours

    @lombok.Builder.Default
    private Boolean availabilityScheduleActive = false; // Whether availability schedule is active

    private String availabilitySchedule; // JSON object with availability details

    private Integer maxPatientsPerDay; // Maximum patients per day

    @lombok.Builder.Default
    private Integer consultationDurationMinutes = 30; // Duration per consultation

    private Double consultationFee; // Consultation fee

    private String boardCertifications; // Board certifications (JSON or comma-separated)

    private Long yearsOfExperience; // Years in medical practice

    private String languages; // Languages spoken (comma-separated)

    @lombok.Builder.Default
    private Boolean verificationStatus = false; // Doctor verification status

    private Long verificationDate; // Verification timestamp

    private String verifiedBy; // Admin ID who verified the doctor

    @lombok.Builder.Default
    private Boolean isAcceptingNewPatients = true; // Accepting new patients

    private String officeLocation; // Office/clinic location

    private String affiliatedHospitals; // Affiliated hospitals (JSON or comma-separated)

    private Double averageRating; // Average patient rating

    @lombok.Builder.Default
    private Integer totalConsultations = 0; // Total number of consultations
}
