package com.medibridge.user_service.repository;

import com.medibridge.user_service.entity.Role;
import com.medibridge.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByIsActiveTrue();

    List<User> findByRoleAndIsActiveTrue(Role role);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = true")
    List<User> findActiveUsersByRole(@Param("role") Role role);

    @Query("SELECT u FROM User u WHERE u.email = :email OR u.username = :username")
    Optional<User> findByEmailOrUsername(@Param("email") String email, @Param("username") String username);

    // ==================== PATIENT QUERIES ====================

    @Query("SELECT u FROM User u WHERE u.role = 'PATIENT' AND u.isDeleted = false")
    List<User> findAllPatients();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'PATIENT' AND u.isDeleted = false")
    long countPatients();

    // ==================== DOCTOR QUERIES ====================

    @Query("SELECT u FROM User u WHERE u.role = 'DOCTOR' AND u.isDeleted = false")
    List<User> findAllDoctors();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'DOCTOR' AND u.isDeleted = false")
    long countDoctors();

    // ==================== STATISTICS QUERIES ====================

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true AND u.isDeleted = false")
    long countActiveUsers();
}


