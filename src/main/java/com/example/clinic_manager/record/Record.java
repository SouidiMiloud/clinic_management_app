package com.example.clinic_manager.record;

import com.example.clinic_manager.doctor.Doctor;
import com.example.clinic_manager.user.ClinicUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
public class Record {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private ClinicUser patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    private LocalDateTime dateOfVisit;
    private String disease;
    private String tests;
    private String results;
    private String medications;
    private String details;

    public Record(ClinicUser patient, Doctor doctor, LocalDateTime dateOfVisit, String disease,
                  String tests, String results, String medications, String details) {

        this.patient = patient;
        this.doctor = doctor;
        this.dateOfVisit = dateOfVisit;
        this.disease = disease;
        this.tests = tests;
        this.results = results;
        this.medications = medications;
        this.details = details;
    }
}