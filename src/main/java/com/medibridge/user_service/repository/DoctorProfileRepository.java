package com.medibridge.user_service.repository;

import com.medibridge.user_service.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for DoctorProfile.
 */
@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, UUID> {
    Optional<DoctorProfile> findByUserId(UUID userId);

    Optional<DoctorProfile> findByLicenseNumber(String licenseNumber);

    Optional<DoctorProfile> findByNpiNumber(String npiNumber);

    List<DoctorProfile> findBySpecialization(String specialization);

    List<DoctorProfile> findByDepartment(String department);

    List<DoctorProfile> findByVerificationStatusTrue();

    List<DoctorProfile> findByIsAcceptingNewPatientsTrue();

    @Query("SELECT dp FROM DoctorProfile dp WHERE dp.user.username = :username")
    Optional<DoctorProfile> findByUsername(@Param("username") String username);

    @Query("SELECT dp FROM DoctorProfile dp WHERE dp.specialization = :specialization AND dp.verificationStatus = true")
    List<DoctorProfile> findVerifiedDoctorsBySpecialization(@Param("specialization") String specialization);

    @Query("SELECT dp FROM DoctorProfile dp WHERE dp.department = :department AND dp.verificationStatus = true ORDER BY dp.averageRating DESC")
    List<DoctorProfile> findVerifiedDoctorsByDepartmentSortedByRating(@Param("department") String department);
}

