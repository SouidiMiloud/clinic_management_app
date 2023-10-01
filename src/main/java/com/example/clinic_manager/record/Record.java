package com.example.clinic_manager.record;

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
public class Record {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime dateOfVisit;
    private String disease;
    private String tests;
    private String results;
    private String medications;
    private String details;

    public Record(Long patientId, Long doctorId, LocalDateTime dateOfVisit, String disease,
                  String tests, String results, String medications, String details) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateOfVisit = dateOfVisit;
        this.disease = disease;
        this.tests = tests;
        this.results = results;
        this.medications = medications;
        this.details = details;
    }
}