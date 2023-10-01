package com.example.clinic_manager.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;
    private String participantUsername;
    private String name;
    private String start;
    private Integer duration;
    private String description;
    private String status;
}