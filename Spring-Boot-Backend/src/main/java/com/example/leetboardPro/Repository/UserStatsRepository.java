package com.example.leetboardPro.Repository;

import com.example.leetboardPro.Model.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStatsRepository extends JpaRepository<UserStats,Long> {
    Optional<UserStats> findByLeetcodeUsername(String username);

    Optional<UserStats> findByUserId(Long id);

    List<UserStats> findAllByOrderByHardCountDescMediumCountDescEasyCountDesc();
}
