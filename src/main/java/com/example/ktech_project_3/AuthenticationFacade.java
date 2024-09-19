package com.example.ktech_project_3;

import com.example.ktech_project_3.user.entity.UserEntity;
import com.example.ktech_project_3.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFacade {
//    public Authentication getAuth() {
//        return SecurityContextHolder.getContext().getAuthentication();
//    }
    private final UserRepository repository;

    public String findUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "User not authenticated"
            );
        }
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        return username;
    }

    public UserEntity findUser (){
        return repository.findByUsername(findUsername()).orElseThrow();
    }
}
