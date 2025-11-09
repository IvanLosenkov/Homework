package org.LosenkovIvan.user_service.mapper;

import org.LosenkovIvan.userService.dto.UserDto;
import org.LosenkovIvan.userService.entity.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }
    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setCreatedAt(dto.getCreatedAt());

        return user;
    }
}