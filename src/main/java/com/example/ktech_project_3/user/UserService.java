package com.example.ktech_project_3.user;

import com.example.ktech_project_3.jwt.JwtTokenUtils;
import com.example.ktech_project_3.jwt.dto.JwtRequestDto;
import com.example.ktech_project_3.jwt.dto.JwtResponseDto;
import com.example.ktech_project_3.user.dto.RegisterUserDto;
import com.example.ktech_project_3.user.dto.UserDto;
import com.example.ktech_project_3.user.dto.UserProfileDto;
import com.example.ktech_project_3.user.entity.UserEntity;
import com.example.ktech_project_3.user.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService  {
    private PasswordEncoder passwordEncoder;
    private JwtTokenUtils jwtTokenUtils;
    private UserRepository userRepository;
    private final CustomerUserDetailsService customService;

    public UserService(
            PasswordEncoder passwordEncoder,
            JwtTokenUtils jwtTokenUtils,
            UserRepository userRepository,
            CustomerUserDetailsService customService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtils = jwtTokenUtils;
        this.customService = customService;
        this.userRepository = userRepository;

        if (userRepository.findByUsername("admin").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setEmail("admin@example.com");
            admin.setPhoneNumber("123-456-7890");
            admin.setRole("ROLE_ADMIN,READ.REQUEST");
            userRepository.save(admin);
        }

    }


    public void registerUser (
            RegisterUserDto dto){
        if (customService.userExists(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username already exists");
        if (!dto.getPassword().equals(dto.getPasswordCheck()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Passwords do not match");
        UserEntity newUser = new UserEntity();
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setRole("ROLE_INACTIVE");
        userRepository.save(newUser);
    }

    public JwtResponseDto loginUser(
            JwtRequestDto requestDto
    ){
        if (requestDto.getUsername() == null || requestDto.getPassword() == null) {
            throw new IllegalArgumentException("Username and password must not be null.");
        }
        UserDetails userDetails;
        try {
            userDetails = customService.loadUserByUsername(requestDto.getUsername());
        } catch (UsernameNotFoundException ex) {
            throw new IllegalArgumentException("Username not found.");
        }
        if(!passwordEncoder.matches(
                requestDto.getPassword(), userDetails.getPassword()))
            throw new IllegalArgumentException( "Invalid password.");
        String jwt = jwtTokenUtils.generateToken(userDetails);
        JwtResponseDto responseDto = new JwtResponseDto();
        responseDto.setToken(jwt);
        return responseDto;
    }
    public UserDto profile(
            String username
    ){
        UserEntity user = userRepository.findByUsername(username).orElseThrow();

        return UserDto.fromEntity(user);
    }

    public UserDto updateProfile(
            String username,
            UserProfileDto profileDto
    ){
        Optional<UserEntity> optionalUser =
                userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Username not found");
        }
        UserEntity user = optionalUser.get();

        if(profileDto.getName() != null){
            user.setName(profileDto.getName());
        }
        if(profileDto.getAge() != null){
            user.setAge(Integer.valueOf(profileDto.getAge()));
        }
        if(profileDto.getEmail() != null){
            user.setEmail(profileDto.getEmail());
        }
        if(profileDto.getPhoneNumber() != null){
            user.setPhoneNumber(profileDto.getPhoneNumber());
        }
        userRepository.save(user);

        if(user.getName() != null && user.getAge() != null
                && user.getEmail() != null && user.getPhoneNumber() != null
                && Objects.equals(user.getRole(), "ROLE_INACTIVE"
        )){
            user.setRole("ROLE_USER,VIEW,ORDER,READ.REQUEST");
            userRepository.save(user);
        }

        return UserDto.fromEntity(user);
    }
    public UserDto updateImage(
            String username,
            MultipartFile image
    ){
        String timeString = String.valueOf(System.currentTimeMillis());
        Optional<UserEntity> optionalUser =
                userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            System.out.println("username not exists");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        String profileDir = "media/" + username + "/";
        try {
            Files.createDirectories(Path.of(profileDir));
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String originalFilename = image.getOriginalFilename();
        String[] filenameSplit = originalFilename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];

        String uploadPath = profileDir + timeString +"profile." + extension;
        try {
            image.transferTo(Path.of(uploadPath));
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String reqPath = "/static/"+ username + "/" + timeString + "profile." + extension;
        UserEntity target = optionalUser.get();

        target.setProfileImage(reqPath);

        return UserDto.fromEntity(userRepository.save(target));
    }
}



//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<UserEntity> optionalUser = repository.findByUsername(username);
//        if (optionalUser.isEmpty())
//            throw new UsernameNotFoundException(username);
//        return CustomerUserDetails.fromEntity(optionalUser.get());
//    }

//    public UserEntity createUser(String username, String password, String passCheck) {
//        if (userExist(username) || !password.equals(passCheck))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        UserEntity newUser = new UserEntity();
//        newUser.setUsername(username);
//        newUser.setPassword(passwordEncoder.encode(password));
//        newUser.setUserRole("ROLE_USER,READ");
////        repository.save(newUser);
//        return repository.save(newUser);
//    }
//
//    public UserEntity createUser(UserEntity user) {
//        if (userExist(username) || !password.equals(passCheck))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        UserEntity newUser = new UserEntity();
//        newUser.setUsername(user.getUsername());
//        newUser.setPassword(user.getPassword());
//        newUser.setUserRole("ROLE_USER,READ");
//        repository.save(newUser);
//        return repository.save(newUser);
//    }
//
//    public boolean userExist(String username) {
//        return repository.existsByUsername(username);
//    }

