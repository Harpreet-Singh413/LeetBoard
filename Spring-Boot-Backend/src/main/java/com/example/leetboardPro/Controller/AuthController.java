package com.example.leetboardPro.Controller;

import com.example.leetboardPro.DTO.OnboardingRequestDTO;
import com.example.leetboardPro.Model.Users;
import com.example.leetboardPro.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/onboarding")
    public ResponseEntity<String> completeOnboarding(
            @RequestBody OnboardingRequestDTO dto,
            @AuthenticationPrincipal Users currentUser) {

        authService.completeOnboarding(currentUser, dto.getLeetUsername());
        return ResponseEntity.ok("Onboarding complete");
    }
}
