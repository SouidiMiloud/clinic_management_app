package com.example.clinic_manager.record;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecordResp {
    private String userName;
    private String dateOfVisit;
    private String disease;
    private String tests;
    private String results;
    private String medications;
    private String details;
}