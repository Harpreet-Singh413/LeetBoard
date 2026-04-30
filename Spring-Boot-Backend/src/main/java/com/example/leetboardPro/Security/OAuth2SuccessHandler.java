package com.example.leetboardPro.Security;

import com.example.leetboardPro.Model.Users;
import com.example.leetboardPro.Repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Find or create user
        Users user = userRepo.findByEmail(email).orElseGet(() -> {
            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setOnboarded(false);
            return userRepo.save(newUser);
        });

        String token = jwtUtil.generateToken(user);

        // Redirect to frontend with JWT as query param
        // Frontend reads it once, stores in memory/localStorage
        String redirectUrl = user.isOnboarded()
                ? "http://localhost:3000/dashboard?token=" + token
                : "http://localhost:3000/onboarding?token=" + token;

//        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + token + "\", \"isOnboarded\":" + user.isOnboarded() + "}");
    }
}
