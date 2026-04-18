package com.example.leetboardPro.Service;

import com.example.leetboardPro.DTO.UsersRequestDTO;
import com.example.leetboardPro.Model.Users;
import com.example.leetboardPro.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import static com.example.leetboardPro.Mapper.UsersMapper.toUserEntity;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LeetCodeService leetCodeService;

    @Transactional
    public String registerUser(@RequestBody UsersRequestDTO dto){
        boolean ifExistsAlready = userRepo.existsByLeetUsername(dto.getLeetUsername());
        if(ifExistsAlready){
            throw new RuntimeException("User already exists");
        }
        else {
            Users user = toUserEntity(dto);
            userRepo.save(user);
            leetCodeService.syncAndSaveUserStats(user.getLeetUsername());
        }
        return "User Registered";
    }
}
