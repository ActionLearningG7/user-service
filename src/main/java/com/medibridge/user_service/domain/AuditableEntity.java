package com.medibridge.user_service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base entity for all domain entities in MediBridge.
 * Provides common auditing fields and soft delete support.
 * Implements DDD principles with version control for optimistic locking.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class AuditableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Entity creation timestamp
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    /**
     * Entity last update timestamp
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    /**
     * User ID who created this entity
     */
    @Column(name = "created_by")
    protected UUID createdBy;

    /**
     * User ID who last updated this entity
     */
    @Column(name = "updated_by")
    protected UUID updatedBy;

    /**
     * Soft delete flag
     */
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    protected Boolean isDeleted = false;

    /**
     * Soft delete timestamp
     */
    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    /**
     * Optimistic locking version
     */
    @Version
    @Column(name = "version")
    @Builder.Default
    protected Long version = 0L;

    /**
     * Soft delete this entity
     */
    @PreUpdate
    public void softDelete() {
        if (this.isDeleted && this.deletedAt == null) {
            this.deletedAt = LocalDateTime.now();
        }
    }
}

