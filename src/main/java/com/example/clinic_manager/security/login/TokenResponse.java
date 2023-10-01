package com.example.clinic_manager.security.login;

import com.example.clinic_manager.user.ClinicUser;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class TokenResponse {

    private ClinicUser user;
    private String token;
}