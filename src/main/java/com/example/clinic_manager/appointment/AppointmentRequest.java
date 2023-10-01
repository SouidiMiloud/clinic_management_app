package com.example.clinic_manager.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class AppointmentRequest {

    private String doctorUsername;
    private LocalDateTime start;
    private Integer duration;
    private String description;
}