package com.example.Job.security.oauth2;


import com.example.Job.constant.RoleEnum;
import com.example.Job.entity.Account;
import com.example.Job.entity.User;
import com.example.Job.security.oauth2.user.OAuth2UserInfo;
import com.example.Job.security.oauth2.user.OAuth2UserInfoFactory;
import com.example.Job.service.IAccountService;
import com.example.Job.service.IUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Don't need this class yet
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final IAccountService accountService;
    private final IUserService userService;

    public CustomOAuth2UserService(IAccountService accountService, IUserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        System.out.println("CustomOAuth2UserService.loadUser() called");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extract provider details
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId;
        String email;
        String name;

        // Different providers have different attribute structures
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

        // Store provider information in the OAuth2User to use later
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("registration_id", provider);

        processUserAccount(email, name, provider, providerId);

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, userNameAttributeName );


    }

    private void processUserAccount(String email, String name, String provider, String providerId) {
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

            userService.createUser(newUser);
        } else {
            // Update provider information if needed
            if (account.getProvider() == null || !account.getProvider().equals(provider)) {
                account.setProvider(provider);
                account.setProviderId(providerId);
                accountService.updateAccount(account);
            }
        }
    }
}
