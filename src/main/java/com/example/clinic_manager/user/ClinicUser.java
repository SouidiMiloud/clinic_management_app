package com.example.clinic_manager.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class ClinicUser implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String phone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private ClinicUserRole role;
    private String profileImagePath;
    private Integer appointmentsNotifNum;
    private Integer messagesNum;
    private Integer notificationsNum;


    public ClinicUser(String firstName, String lastName, String username, String phone, String password, ClinicUserRole role, String profileImagePath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.profileImagePath = profileImagePath;
        appointmentsNotifNum = 0;
        messagesNum = 0;
        notificationsNum = 0;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());

        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}