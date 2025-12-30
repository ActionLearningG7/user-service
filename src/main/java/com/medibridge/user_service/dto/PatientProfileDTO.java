package com.medibridge.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for Patient Profile containing enterprise-level patient/user fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PatientProfileDTO extends UserProfileDTO {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String insuranceProvider;
    private String insurancePolicyNumber;
    private String insuranceGroupNumber;
    private Long insurancePolicyExpiryDate;
    private String bloodGroup;
    private Boolean medicalHistoryAccessGranted;
    private String medicalConditions;
    private String allergies;
    private String currentMedications;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
    private String secondaryEmergencyContactName;
    private String secondaryEmergencyContactPhone;
    private String secondaryEmergencyContactRelation;
    private String preferredLanguage;
    private Boolean notificationsEnabled;
    private String communicationPreference;
    private Long lastConsultationDate;
    private String preferredDoctorIds;
    private Boolean gdprConsent;
    private Boolean termsAndConditionsAccepted;
    private Boolean privacyPolicyAccepted;
    private Long accountCreatedDate;
    private Long lastLoginDate;
    private Boolean twoFactorAuthenticationEnabled;
}
