package com.project.habit_tracker.Controller;

import com.project.habit_tracker.Dto.LoginRequestDto;
import com.project.habit_tracker.Dto.LoginResponseDto;
import com.project.habit_tracker.Dto.RegisterRequestDto;
import com.project.habit_tracker.Dto.UserResponseDto;
import com.project.habit_tracker.Model.User;
import com.project.habit_tracker.Security.JwtService;
import com.project.habit_tracker.Service.AuditLogService;
import com.project.habit_tracker.Service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    private final AuditLogService auditLogService;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.cookie-name:habit_tracker_jwt}")
    private String cookieName;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    @Value("${app.jwt.cookie-secure:false}")
    private boolean cookieSecure;

    public AuthController(ApplicationEventPublisher applicationEventPublisher, AuditLogService auditLogService, UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.auditLogService = auditLogService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        User user = userService.getOptionalUserByUsername(request.getUsername()).orElse(null);
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtService.generateToken(user);
        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .sameSite("Strict")
                .maxAge(jwtExpirationMs / 1000)
                .build();
        logger.info("User " + user.getUsername() + " logged in successfully, token: " + token);

        LoginResponseDto response = new LoginResponseDto(user.getName(), user.getUsername(), user.getRole());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequestDto request, HttpServletRequest httpRequest) {
        try {
            User registeredUser = userService.registerUser(
                    request.getUsername(),
                    request.getName(),
                    request.getEmail(),
                    request.getPassword(),
                    httpRequest.getRemoteAddr()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(userToDto(registeredUser));
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .sameSite("Strict")
                .maxAge(0)
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponseDto> me(jakarta.servlet.http.HttpServletRequest request) {
        String token = getTokenFromCookie(request);
        if (token == null || !jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = jwtService.extractAllClaims(token);
        LoginResponseDto response = new LoginResponseDto(
                claims.get("name", String.class),
                claims.get("username", String.class),
                claims.get("role", String.class)
        );
        return ResponseEntity.ok(response);
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for ( Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
//
//    private String resolveClientIp(HttpServletRequest request) {
//        String xForwardedFor = request.getHeader("X-Forwarded-For");
//        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
//            return xForwardedFor.split(",")[0].trim();
//        }
//
//        String xRealIp = request.getHeader("X-Real-IP");
//        if (xRealIp != null && !xRealIp.isBlank()) {
//            return xRealIp.trim();
//        }
//
//        return request.getRemoteAddr();
//    }

    private UserResponseDto userToDto(User user) {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        return userDto;
    }
}
