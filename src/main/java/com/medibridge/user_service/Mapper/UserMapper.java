package com.medibridge.user_service.mapper;

import com.medibridge.user_service.dto.response.UserResponseDTO;
import com.medibridge.user_service.dto.response.UserProfileDTO;
import com.medibridge.user_service.entity.Role;
import com.medibridge.user_service.entity.User;
import com.medibridge.user_service.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for converting between User/UserProfile entities and DTOs.
 * Provides consistent and efficient mapping with type safety.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    /**
     * Map User entity to UserResponseDTO
     * Converts Role enum to String for API response
     */
    @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)")
    UserResponseDTO userToUserResponseDTO(User user);

    /**
     * Map UserProfile entity to UserProfileDTO
     */
    UserProfileDTO userProfileToUserProfileDTO(UserProfile userProfile);

    /**
     * Map User to UserResponseDTO with profile
     */
    default UserResponseDTO userWithProfileToDTO(User user) {
        UserResponseDTO dto = userToUserResponseDTO(user);
        if (user.getProfile() != null) {
            dto.setProfile(userProfileToUserProfileDTO(user.getProfile()));
        }
        return dto;
    }

    /**
     * Convert Role enum to String
     */
    default String roleToString(Role role) {
        return role != null ? role.name() : null;
    }
}

