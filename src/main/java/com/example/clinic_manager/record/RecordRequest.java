package com.example.clinic_manager.record;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class RecordRequest {

    private String disease;
    private String tests;
    private String results;
    private String medications;
    private String details;
}