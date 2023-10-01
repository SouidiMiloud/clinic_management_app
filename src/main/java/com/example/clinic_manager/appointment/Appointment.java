package com.example.clinic_manager.appointment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime start;
    private Integer duration;
    private Status status;
    private String description;

    public Appointment(Long patientId, Long doctorId, LocalDateTime start, int duration, Status status, String description) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.start = start;
        this.duration = duration;
        this.status = status;
        this.description = description;
    }
}