package com.example.ktech_project_3.user;

import com.example.ktech_project_3.user.entity.UserEntity;
import com.example.ktech_project_3.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.info("loadUserByUsername in UserService by me!");
        Optional<UserEntity> optionalUser =
                userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);

        return CustomerUserDetails.fromEntity(optionalUser.get());
    }
    public boolean userExists (String username){
        return userRepository.existsByUsername(username);
    }
}
