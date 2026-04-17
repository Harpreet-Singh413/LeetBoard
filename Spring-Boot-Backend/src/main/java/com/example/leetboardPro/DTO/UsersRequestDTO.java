package com.example.leetboardPro.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersRequestDTO {
    private String username;
    private String leetUsername;
    private String course;
    private Integer semester;
}
