package com.example.leetboardPro.Controller;

import com.example.leetboardPro.DTO.UserResponseDTO;
import com.example.leetboardPro.DTO.UsersRequestDTO;
import com.example.leetboardPro.Model.Users;
import com.example.leetboardPro.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UsersController {
    @Autowired
    private UserService service;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UsersRequestDTO dto){
        return new ResponseEntity<String>(service.registerUser(dto),HttpStatus.OK);
    }


}
