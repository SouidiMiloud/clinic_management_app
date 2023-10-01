package com.example.clinic_manager.doctor;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;


@Data
@AllArgsConstructor
public class DoctorResponse {

    private String firstName;
    private String lastName;
    private String username;
    private Specialty specialty;
    private String workStart;
    private String workEnd;
    private String profileImagePath;
}