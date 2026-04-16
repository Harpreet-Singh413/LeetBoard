package com.example.leetboardPro.Mapper;

import com.example.leetboardPro.DTO.UserResponseDTO;
import com.example.leetboardPro.DTO.UsersRequestDTO;
import com.example.leetboardPro.Model.Users;

public class UsersMapper {
    public static Users toEntity(UsersRequestDTO dto){
        Users user = new Users();
        user.setUsername(dto.getUsername());
        user.setLeetUsername(dto.getLeetUsername());
        user.setCourse(dto.getCourse());
        user.setSemester(dto.getSemester());
        return user;
    }

    public static UserResponseDTO toDto(Users user){
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setLeetUsername(user.getLeetUsername());
        dto.setCourse(user.getCourse());
        dto.setSemester(user.getSemester());
        return dto;
    }
}
