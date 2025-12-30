package com.medibridge.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Patient registration request with patient-specific profile fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PatientRegisterRequest extends RegisterRequest {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
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
    private String medicalConditions;
    private String allergies;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
    private String secondaryEmergencyContactName;
    private String secondaryEmergencyContactPhone;
    private String secondaryEmergencyContactRelation;
    @lombok.Builder.Default
    private String preferredLanguage = "en";
    @lombok.Builder.Default
    private String communicationPreference = "EMAIL";
    @lombok.Builder.Default
    private Boolean gdprConsent = false;
    @lombok.Builder.Default
    private Boolean termsAndConditionsAccepted = false;
    @lombok.Builder.Default
    private Boolean privacyPolicyAccepted = false;
}
