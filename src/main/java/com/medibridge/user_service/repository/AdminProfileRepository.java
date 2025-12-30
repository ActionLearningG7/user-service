package com.medibridge.user_service.repository;

import com.medibridge.user_service.entity.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for AdminProfile.
 */
@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile, UUID> {
    Optional<AdminProfile> findByUserId(UUID userId);

    List<AdminProfile> findByDepartment(String department);

    List<AdminProfile> findByOrganizationCode(String organizationCode);

    List<AdminProfile> findByAccessLevelGreaterThanEqual(Integer accessLevel);

    @Query("SELECT ap FROM AdminProfile ap WHERE ap.user.username = :username")
    Optional<AdminProfile> findByUsername(@Param("username") String username);

    List<AdminProfile> findByAccountLockedFalse();
}
