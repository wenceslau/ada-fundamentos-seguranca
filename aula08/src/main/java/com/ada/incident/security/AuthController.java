package com.ada.incident.security;

import com.ada.incident.user.UserCreateRequest;
import com.ada.incident.user.UserResponse;
import com.ada.incident.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class AuthController {

    public record TokenRequest(String username, String password) {
    }

    public record TokenResponse(String token) {
    }

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtEncoder jwtEncoder;

    public AuthController(UserService userService, AuthenticationManager authManager, JwtEncoder jwtEncoder) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {

        var user = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserResponse(user.getId(), user.getName(), user.getUsername()));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody TokenRequest request) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("ada@ada.com.br")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(auth.getName())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return ResponseEntity.ok(new TokenResponse(token));
    }

}
