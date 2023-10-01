package com.example.clinic_manager.security.login;

import com.example.clinic_manager.security.JwtUtil;
import com.example.clinic_manager.user.ClinicUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/authentication")
@AllArgsConstructor
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credentials credentials){

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(), credentials.getPassword()
                    )
            );
            ClinicUser user = (ClinicUser) authentication.getPrincipal();
            return ResponseEntity.ok().body(new TokenResponse(user, jwtUtil.generateToken(user)));
        }
        catch(BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}