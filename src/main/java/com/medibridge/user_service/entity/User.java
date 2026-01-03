package com.medibridge.user_service.entity;

import com.medibridge.user_service.domain.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * User domain entity - Aggregate Root in DDD
 * Represents a user in the MediBridge system with authentication capabilities.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_username", columnList = "username", unique = true),
    @Index(name = "idx_email", columnList = "email", unique = true),
    @Index(name = "idx_is_active", columnList = "is_active"),
    @Index(name = "idx_is_deleted", columnList = "is_deleted")
})
public class User extends AuditableEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * Primary Key - UUID for distributed systems
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Unique username for login
     */
    @Column(unique = true, nullable = false, length = 100)
    private String username;

    /**
     * Hashed password (never store plain text)
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Unique email address
     */
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    /**
     * User role determining permissions
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    /**
     * Profile details (one-to-one relationship)
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;

    /**
     * Full name of the user
     */
    @Column(name = "full_name", length = 255)
    private String fullName;

    /**
     * Contact phone number
     */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    /**
     * Account active status
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Account lock status (for security)
     */
    @Column(name = "is_locked", nullable = false)
    @Builder.Default
    private Boolean isLocked = false;

    /**
     * Timestamp of last login
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * Failed login attempts counter
     */
    @Column(name = "failed_login_attempts", nullable = false)
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    /**
     * Account lock expiration
     */
    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked && isActive && (profile == null || profile.getStatus() != AccountStatus.SUSPENDED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive && !isDeleted;
    }

    /**
     * Lock the user account
     */
    public void lockAccount(int lockDurationMinutes) {
        this.isLocked = true;
        this.lockedUntil = LocalDateTime.now().plusMinutes(lockDurationMinutes);
    }

    /**
     * Unlock the user account
     */
    public void unlockAccount() {
        this.isLocked = false;
        this.lockedUntil = null;
        this.failedLoginAttempts = 0;
    }

    /**
     * Record a failed login attempt
     */
    public void recordFailedLoginAttempt() {
        this.failedLoginAttempts = (this.failedLoginAttempts == null ? 0 : this.failedLoginAttempts) + 1;
    }

    /**
     * Record a successful login
     */
    public void recordSuccessfulLogin() {
        this.lastLoginAt = LocalDateTime.now();
        this.failedLoginAttempts = 0;
        this.isLocked = false;
        this.lockedUntil = null;
    }
}
