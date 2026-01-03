package com.medibridge.user_service.Mapper;

import com.medibridge.user_service.dto.UserDTO;
import com.medibridge.user_service.entity.User;

public class UserDTOMapper {

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }
}
