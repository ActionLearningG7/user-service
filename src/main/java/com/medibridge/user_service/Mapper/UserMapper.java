package com.medibridge.user_service.Mapper;

import com.medibridge.user_service.dto.response.UserProfileDTO;
import com.medibridge.user_service.dto.response.UserResponseDTO;
import com.medibridge.user_service.entity.User;
import com.medibridge.user_service.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for converting between User/UserProfile entities and DTOs.
 * Provides consistent and efficient mapping with type safety.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    /**
     * Map User entity to UserResponseDTO manually
     */
    default UserResponseDTO userToUserResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO dto = UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole() != null ? user.getRole() : null)
                .isActive(user.getIsActive())
                .isLocked(user.getIsLocked())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        if (user.getProfile() != null) {
            dto.setProfile(userProfileToUserProfileDTO(user.getProfile()));
        }

        return dto;
    }

    /**
     * Map UserProfile entity to UserProfileDTO
     */
    default UserProfileDTO userProfileToUserProfileDTO(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }

        return UserProfileDTO.builder()
                .id(userProfile.getId())
                .userId(userProfile.getUser() != null ? userProfile.getUser().getId() : null)
                .createdAt(userProfile.getCreatedAt())
                .updatedAt(userProfile.getUpdatedAt())
                .build();
    }

    /**
     * Map User to UserResponseDTO with profile
     */
    default UserResponseDTO userWithProfileToDTO(User user) {
        return userToUserResponseDTO(user);
    }
}

