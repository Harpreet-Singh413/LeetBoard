package com.example.leetboardPro.Controller;

import com.example.leetboardPro.DTO.OnboardingRequestDTO;
import com.example.leetboardPro.Model.Users;
import com.example.leetboardPro.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "localhost:3000")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/onboarding")
    public ResponseEntity<String> completeOnboarding(
            @RequestBody OnboardingRequestDTO dto,
            @AuthenticationPrincipal Users currentUser) {

        authService.completeOnboarding(currentUser, dto);
        return ResponseEntity.ok("Onboarding complete");
    }
}
