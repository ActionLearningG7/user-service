package com.medibridge.user_service.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Patient/User profile entity.
 * Contains enterprise-level patient/general user specific fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@DiscriminatorValue("USER")
public class PatientProfile extends UserProfile {

    private String firstName; // Patient's first name

    private String lastName; // Patient's last name

    private String dateOfBirth; // DOB in YYYY-MM-DD format

    private String gender; // MALE, FEMALE, OTHER

    private String phoneNumber; // Contact number

    private String address; // Residential address

    private String city; // City

    private String state; // State/Province

    private String zipCode; // Postal code

    private String country; // Country

    private String insuranceProvider; // Insurance provider name

    private String insurancePolicyNumber; // Insurance policy number

    private String insuranceGroupNumber; // Insurance group number

    private Long insurancePolicyExpiryDate; // Expiry timestamp

    private String bloodGroup; // Blood group (A+, B-, etc.)

    @lombok.Builder.Default
    private Boolean medicalHistoryAccessGranted = false; // Permission to share medical history

    private String medicalConditions; // Comma-separated or JSON array of conditions

    private String allergies; // Comma-separated or JSON array of allergies

    private String currentMedications; // JSON array of current medications

    private String emergencyContactName; // Primary emergency contact name

    private String emergencyContactPhone; // Primary emergency contact number

    private String emergencyContactRelation; // Relation to patient

    private String secondaryEmergencyContactName; // Secondary emergency contact

    private String secondaryEmergencyContactPhone; // Secondary contact number

    private String secondaryEmergencyContactRelation; // Relation to patient

    private String preferredLanguage; // Language preference (ISO 639-1)

    @lombok.Builder.Default
    private Boolean notificationsEnabled = true; // Push/Email notifications

    private String communicationPreference; // EMAIL, SMS, BOTH, NONE

    private Long lastConsultationDate; // Timestamp of last consultation

    private String preferredDoctorIds; // Comma-separated or JSON array of preferred doctor IDs

    @lombok.Builder.Default
    private Boolean gdprConsent = false; // GDPR compliance consent

    @lombok.Builder.Default
    private Boolean termsAndConditionsAccepted = false; // T&C acceptance

    @lombok.Builder.Default
    private Boolean privacyPolicyAccepted = false; // Privacy policy acceptance

    private Long accountCreatedDate; // Account creation timestamp

    private Long lastLoginDate; // Last login timestamp

    @lombok.Builder.Default
    private Boolean twoFactorAuthenticationEnabled = false; // 2FA status
}
