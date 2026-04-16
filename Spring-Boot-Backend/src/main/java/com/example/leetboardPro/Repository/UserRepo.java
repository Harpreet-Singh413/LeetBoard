package com.example.leetboardPro.Repository;

import com.example.leetboardPro.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<Users,Long> {
    boolean existsByLeetUsername(String leetUsername);

    Users findByLeetUsername(String username);
}
