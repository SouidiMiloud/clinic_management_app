package com.example.clinic_manager.security.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {

    private String username;
    private String password;
}