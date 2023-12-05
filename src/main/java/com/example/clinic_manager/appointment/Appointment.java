package com.example.clinic_manager.appointment;

import com.example.clinic_manager.doctor.Doctor;
import com.example.clinic_manager.user.ClinicUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private ClinicUser patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private ClinicUser doctor;
    private LocalDateTime start;
    private Integer duration;
    private Status status;
    private String description;

    public Appointment(ClinicUser patient, Doctor doctor, LocalDateTime start, int duration, Status status, String description) {
        this.patient = patient;
        this.doctor = doctor;
        this.start = start;
        this.duration = duration;
        this.status = status;
        this.description = description;
    }
}