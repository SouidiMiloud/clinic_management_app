package com.example.clinic_manager.user;


import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClinicUserDetailsService implements UserDetailsService {

    private ClinicUserRepo clinicUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<ClinicUser> user = clinicUserRepo.findByUsername(username);
        return user.orElseThrow(()->new UsernameNotFoundException(username + " not found"));
    }
}