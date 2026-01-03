package com.medibridge.user_service.repository;

import com.medibridge.user_service.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for PatientProfile.
 */
@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, UUID> {
    Optional<PatientProfile> findByUserId(UUID userId);

    Optional<PatientProfile> findByInsurancePolicyNumber(String policyNumber);

    List<PatientProfile> findByBloodGroup(String bloodGroup);

    List<PatientProfile> findByMedicalHistoryAccessGrantedTrue();

    List<PatientProfile> findByTwoFactorAuthenticationEnabledTrue();

    @Query("SELECT pp FROM PatientProfile pp WHERE pp.user.username = :username")
    Optional<PatientProfile> findByUsername(@Param("username") String username);

    @Query("SELECT pp FROM PatientProfile pp WHERE pp.user.email = :email")
    Optional<PatientProfile> findByEmail(@Param("email") String email);

    @Query("SELECT pp FROM PatientProfile pp WHERE pp.medicalConditions LIKE %:condition%")
    List<PatientProfile> findPatientsByMedicalCondition(@Param("condition") String condition);

    @Query("SELECT pp FROM PatientProfile pp WHERE pp.gdprConsent = true")
    List<PatientProfile> findPatientWithGdprConsent();
}

