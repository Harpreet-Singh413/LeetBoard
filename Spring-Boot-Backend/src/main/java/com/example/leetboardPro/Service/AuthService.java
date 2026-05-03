package com.example.leetboardPro.Service;

import com.example.leetboardPro.Client.LeetCodeClient;
import com.example.leetboardPro.DTO.LeetCodeRawData;
import com.example.leetboardPro.DTO.OnboardingRequestDTO;
import com.example.leetboardPro.Model.Users;
import com.example.leetboardPro.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final LeetCodeClient leetCodeClient;
    private final LeetCodeService leetCodeService;

    public void completeOnboarding(Users user, OnboardingRequestDTO onboardingRequestDTO) {
        String leetUsername = onboardingRequestDTO.getLeetUsername();
        // 1. Validate leetUsername exists on LeetCode
        LeetCodeRawData data = leetCodeClient.fetchUserData(leetUsername);
        if (data == null) {
            throw new RuntimeException("LeetCode username not found");
        }

        if (data.getData() == null) {
            throw new RuntimeException("LeetCode API returned null data");
        }
        if (data.getData().getMatchedUser() == null) {
            throw new RuntimeException("LeetCode username '" + leetUsername + "' not found");
        }

        // 2. Check not already taken by another user
        if (userRepo.existsByLeetUsername(leetUsername)) {
            throw new RuntimeException("LeetCode username already registered");
        }

        // 3. Save and mark onboarded
        user.setLeetUsername(leetUsername);
        user.setCourse(onboardingRequestDTO.getCourse());
        user.setSemester(onboardingRequestDTO.getSemester());
        user.setOnboarded(true);
        userRepo.save(user);

        // 4. Initial stats sync
        leetCodeService.syncAndSaveUserStats(user);
    }
}
