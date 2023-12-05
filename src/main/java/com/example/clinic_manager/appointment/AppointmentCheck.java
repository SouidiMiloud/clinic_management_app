package com.example.clinic_manager.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class AppointmentCheck {
    private Long id;
    private Status decision;
}