package com.rossypotential.todo_assignment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rossypotential.todo_assignment.config.JwtService;
import com.rossypotential.todo_assignment.dtos.request.AuthenticationRequest;
import com.rossypotential.todo_assignment.dtos.request.RegisterRequest;
import com.rossypotential.todo_assignment.dtos.response.ApiResponse;
import com.rossypotential.todo_assignment.dtos.response.AuthenticationResponse;
import com.rossypotential.todo_assignment.dtos.response.RegisterResponse;
import com.rossypotential.todo_assignment.dtos.response.UserResponse;
import com.rossypotential.todo_assignment.email.EmailDetails;
import com.rossypotential.todo_assignment.email.EmailService;
import com.rossypotential.todo_assignment.exception.InvalidEmailFormatException;
import com.rossypotential.todo_assignment.exception.InvalidLoginException;
import com.rossypotential.todo_assignment.model.AppUser;
import com.rossypotential.todo_assignment.repositories.UserRepository;
import com.rossypotential.todo_assignment.service.UserService;
import com.rossypotential.todo_assignment.utils.EmailTemplate;
import com.rossypotential.todo_assignment.utils.UserUtils;
import com.rossypotential.todo_assignment.utils.VerificationUtils;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.URI;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${baseUrl}")
    private String baseUrl;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final Set<String> validDomains = Set.of("com", "net", "org", "edu", "tech", "*");

    public ApiResponse<RegisterResponse> register(@Valid RegisterRequest registerRequest) throws MessagingException, JsonProcessingException {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ApiResponse.badRequest("An account with this email already exists.");
        }

        String domain = registerRequest.getEmail().substring(registerRequest.getEmail().lastIndexOf(".") + 1);
        if (!validDomains.contains(domain)) {
            return ApiResponse.badRequest("Invalid email domain.");
        }

        CompletableFuture<String> encodedPasswordFuture = CompletableFuture.supplyAsync(() ->
                passwordEncoder.encode(registerRequest.getPassword()));

        CompletableFuture<AppUser> newUserFuture = encodedPasswordFuture.thenApply(encodedPassword -> {
            AppUser newUser = AppUser.builder()
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .email(registerRequest.getEmail())
                    .password(encodedPassword)
                    .phoneNumber(registerRequest.getPhoneNumber())
                    .isEnabled(false)
                    .build();
            return userRepository.save(newUser);
        });

        AppUser newUser = newUserFuture.join();

        String verificationCode = VerificationUtils.generateVerificationCode();
        newUser.setVerificationCode(verificationCode);
        userRepository.save(newUser);
        log.info("Verification code for user: " + newUser.getEmail() + " is " + verificationCode);

        String link = EmailTemplate.getVerificationUrl(baseUrl, verificationCode);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(newUser.getEmail())
                .subject("ACCOUNT VERIFICATION")
                .messageBody(EmailTemplate.getEmailMessage(newUser.getFirstName(), baseUrl, verificationCode))
                .build();

        emailService.sendHtmlMessageToVerifyEmail(emailDetails, newUser.getFirstName(), link);
        log.info("Email sent successfully");

        RegisterResponse registerResponse = RegisterResponse.builder()
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .build();

        return ApiResponse.ok("Congratulations!!!",registerResponse);
    }


    @Override
    @Transactional
    public ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            AppUser user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String jwtToken = jwtService.generateToken(user);

            user.setToken(jwtToken);
            AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                    .firstName(user.getFirstName())
                    .email(user.getEmail())
                    .accessToken(user.getToken())
                    .build();


            return ApiResponse.ok("Welcome!!!",authenticationResponse);


        } catch (InvalidLoginException e) {
            log.error("Invalid login credentials: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String verifyEmail(String verificationCode) {

        if (verificationCode != null) {
            Optional<AppUser> user = userRepository.findByVerificationCode(verificationCode);

            if (user.isPresent()) {
                if (user.get().isEnabled()) {
                    return "User has been verified!";
                } else {
                    user.get().setIsEnabled(true);
                    user.get().setVerificationCode(null);
                    userRepository.save(user.get());
                    return "Email Verified";
                }

            } else {
                return "User does not exist";
            }
        }
        return "Invalid token or broken link";
    }

    @Override
    public ResponseEntity<ApiResponse<String>> resendEmailVerification(String email) {
        Optional<AppUser> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest("User with the provided email does not exist."));
        }

        AppUser user = optionalUser.get();
        if (user.isEnabled()) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest("User's email is already verified."));
        }

        String verificationCode = VerificationUtils.generateVerificationCode();
        user.setVerificationCode(verificationCode);
        userRepository.save(user);

        String link = EmailTemplate.getVerificationUrl(baseUrl, verificationCode);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("ACCOUNT VERIFICATION")
                .messageBody(EmailTemplate.getEmailMessage(user.getFirstName(), baseUrl, verificationCode))
                .build();

        try {
            emailService.sendHtmlMessageToVerifyEmail(emailDetails, user.getFirstName(), link);
            return ResponseEntity.ok(ApiResponse.ok("Verification email resent successfully.",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.badRequest("Failed to resend verification email. Please try again later."));
        }
    }


    public ResponseEntity<ApiResponse<UserResponse<?>>> editUser(Long id, String firstName, String lastName, String phoneNumber) {
        try {
            AppUser appUser = userRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("No User associated with ID: " + id));

            if (firstName != null) appUser.setFirstName(firstName);
            if (lastName != null) appUser.setLastName(lastName);
            if (phoneNumber != null) appUser.setPhoneNumber(phoneNumber);

            userRepository.save(appUser);

            UserResponse<?> userResponse = UserResponse.builder()
                    .firstName(appUser.getFirstName())
                    .lastName(appUser.getLastName())
                    .phoneNumber(appUser.getPhoneNumber())
                    .build();

            return ResponseEntity.ok(ApiResponse.ok("User updated!!!",userResponse));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.badRequest("An error occurred while updating the user"));
        }
    }


    @Override
    public UserResponse<?> viewUser(Long id) {
        try {
            Optional<AppUser> optionalUser = userRepository.findById(id);
            if (optionalUser.isEmpty()) {
                throw new UsernameNotFoundException("User not found");
            }

            AppUser viewUser = optionalUser.get();

            return UserResponse.builder()
                    .responseMessage(UserUtils.USER_DETAILS_MESSAGE)
                    .firstName(viewUser.getFirstName())
                    .lastName(viewUser.getLastName())
                    .phoneNumber(viewUser.getPhoneNumber())
                    .email(viewUser.getEmail())
                    .build();
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching the user details", e);
        }
    }

    public UserDetails loadUserByUsername(String username) {

        Optional<AppUser> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        AppUser user = optionalUser.get();

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }


    @Override
    public Page<UserResponse<?>> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));
        Page<AppUser> usersPage = userRepository.findAll(pageable);

        if (usersPage.isEmpty()) {
            return Page.empty();
        }

        return usersPage.map(user -> {
            log.info("Mapping User ID: {}, Email: {}", user.getId(), user.getEmail());
            return new UserResponse<>(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), null);
        });
    }

}