package com.example.leetboardPro.Service;


import com.example.leetboardPro.Client.LeetCodeClient;
import com.example.leetboardPro.DTO.LeetCodeRawData;
import com.example.leetboardPro.DTO.UserResponseDTO;
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
    public UserStatsDTO syncAndSaveUserStats(String username) {
        // 1. Fetch from LeetCode
        LeetCodeRawData rawResponse = leetCodeClient.fetchUserData(username);

        if (rawResponse == null || rawResponse.getData().getMatchedUser() == null) {
            throw new RuntimeException("User '" + username + "' not found on LeetCode.");
        }

        Users user = userRepo.findByLeetUsername(username);
        if(user == null){
            throw new RuntimeException("User not exists or not registered");
        }

        // 2. Prepare Entity (Update existing or Create new)
        UserStats stats = repository.findByLeetcodeUsername(username).orElse(new UserStats());
        stats.setUser(user);
        stats.setLeetcodeUsername(username);
        stats.setLastSync(LocalDateTime.now());

        // 3. Extract difficulty counts from the list
        rawResponse.getData().getMatchedUser().getSubmitStats().getAcSubmissionNum()
                .forEach(stat -> {
                    switch (stat.getDifficulty()) {
                        case "Easy" -> stats.setEasyCount(stat.getCount());
                        case "Medium" -> stats.setMediumCount(stat.getCount());
                        case "Hard" -> stats.setHardCount(stat.getCount());
                    }
                });

        // 4. Save to Database
        UserStats savedEntity = repository.save(stats);

        // 5. Return sanitized Response DTO
        return new UserStatsDTO(
                savedEntity.getLeetcodeUsername(),
                savedEntity.getEasyCount(),
                savedEntity.getMediumCount(),
                savedEntity.getHardCount(),
                savedEntity.getLastSync()
        );
    }
}
