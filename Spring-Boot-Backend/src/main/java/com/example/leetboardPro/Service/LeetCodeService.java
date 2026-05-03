package com.example.leetboardPro.Service;


import com.example.leetboardPro.Client.LeetCodeClient;
import com.example.leetboardPro.DTO.LeetCodeRawData;
import com.example.leetboardPro.DTO.UserStatsDTO;
import com.example.leetboardPro.Model.UserStats;
import com.example.leetboardPro.Model.Users;
import com.example.leetboardPro.Repository.UserRepo;
import com.example.leetboardPro.Repository.UserStatsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LeetCodeService {

    private final LeetCodeClient leetCodeClient;
    private final UserStatsRepository repository;
    private final UserRepo userRepo;

    public LeetCodeService(LeetCodeClient leetCodeClient, UserStatsRepository repository, UserRepo userRepo) {
        this.leetCodeClient = leetCodeClient;
        this.repository = repository;
        this.userRepo = userRepo;
    }

    @Transactional
    public UserStatsDTO syncAndSaveUserStats(Users user) {
        LeetCodeRawData rawResponse = leetCodeClient.fetchUserData(user.getLeetUsername());

        if (rawResponse == null || rawResponse.getData().getMatchedUser() == null) {
            throw new RuntimeException("User not found on LeetCode.");
        }

        // Re-fetch to get a managed entity ← this is the fix
        Users managedUser = userRepo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found in DB"));

        UserStats stats = repository.findByLeetcodeUsername(user.getLeetUsername())
                .orElse(new UserStats());

        stats.setUser(managedUser); // ← use managed entity, not detached one
        stats.setLeetcodeUsername(user.getLeetUsername());
        stats.setLastSync(LocalDateTime.now());

        rawResponse.getData().getMatchedUser().getSubmitStats().getAcSubmissionNum()
                .forEach(stat -> {
                    switch (stat.getDifficulty()) {
                        case "Easy" -> stats.setEasyCount(stat.getCount());
                        case "Medium" -> stats.setMediumCount(stat.getCount());
                        case "Hard" -> stats.setHardCount(stat.getCount());
                    }
                });

        UserStats saved = repository.save(stats);
        return new UserStatsDTO(
                saved.getLeetcodeUsername(),
                saved.getUser(),
                saved.getEasyCount(),
                saved.getMediumCount(),
                saved.getHardCount(),
                saved.getLastSync()
        );
    }
}
