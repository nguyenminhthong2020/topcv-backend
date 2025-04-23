package com.example.Job.security.oauth2;

import com.example.Job.constant.RoleEnum;
import com.example.Job.entity.Account;
import com.example.Job.entity.User;
import com.example.Job.security.JwtUtil;
import com.example.Job.security.oauth2.user.OAuth2UserInfo;
import com.example.Job.security.oauth2.user.OAuth2UserInfoFactory;
import com.example.Job.service.IAccountService;
import com.example.Job.service.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {


    private final IAccountService accountService;
    private final IUserService userService;
    private final JwtUtil jwtUtil;
    @Value("${app.oauth2.authorizedRedirectUris}")
    private String successRedirectUri;

    public OAuth2LoginSuccessHandler(IAccountService accountService, IUserService userService, JwtUtil jwtUtil) {
        this.accountService = accountService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User = token.getPrincipal();
        // Extract provider details
        String provider = token.getAuthorizedClientRegistrationId();

        String providerId;
        String email;
        String name;

        // Different providers have different attribute structures, refractor using Factory
//        if(provider.equals("google")){
//            providerId = oAuth2User.getAttribute("sub");
//            email = oAuth2User.getAttribute("email");
//            name = oAuth2User.getAttribute("name");
//        }else if (provider.equals("facebook")) {
//            providerId = oAuth2User.getAttribute("id");
//            email = oAuth2User.getAttribute("email");
//            name = oAuth2User.getAttribute("name");
//        } else {
//            throw new OAuth2AuthenticationException("Unsupported provider");
//        }

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());

        providerId = oAuth2UserInfo.getId();
        email = oAuth2UserInfo.getEmail();
        name = oAuth2UserInfo.getName();

        Account account = this.processUserAccount(email, name, provider, providerId);

        // Redirect to frontend manually
//        response.sendRedirect(successRedirectUri);
        String accessToken = jwtUtil.createAccessToken(account);


        String redirectUrl = UriComponentsBuilder.fromUriString(successRedirectUri)
                .queryParam("token", accessToken)
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);

    }


    private Account processUserAccount(String email, String name, String provider, String providerId) {
        // Check if user exists
        Account account = accountService.getAccountByEmail(email);

        if (account == null) {
            // Create new user account
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setProvider(provider);
            newUser.setProviderId(providerId);
            newUser.setRole(RoleEnum.USER);


            newUser.setPassword(UUID.randomUUID().toString());

            Account user = userService.createUser(newUser);
            return user;
        } else {
            // Update provider information if needed
            Account user = account;
            if (account.getProvider() == null || !account.getProvider().equals(provider)) {
                account.setProvider(provider);
                account.setProviderId(providerId);
                user = accountService.updateAccount(account);

            }

            return user;
        }
    }
}
