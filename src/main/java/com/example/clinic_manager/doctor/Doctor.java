package com.example.clinic_manager.doctor;

import com.example.clinic_manager.user.ClinicUser;
import com.example.clinic_manager.user.ClinicUserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalTime;


@Entity
@Data
@NoArgsConstructor
public class Doctor extends ClinicUser {

    private Specialty specialty;
    private LocalTime workStart;
    private LocalTime workEnd;
    private Boolean isAvailable;

    public Doctor(String firstName, String lastName, String username, String phone, String password, ClinicUserRole role, String profileImagePath, Specialty specialty, LocalTime workStart, LocalTime workEnd, Boolean isAvailable) {
        super(firstName, lastName, username, phone, password, role, profileImagePath);
        this.specialty = specialty;
        this.workStart = workStart;
        this.workEnd = workEnd;
        this.isAvailable = isAvailable;
    }
}