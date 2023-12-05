package com.example.clinic_manager.appointment;

import com.example.clinic_manager.user.ClinicUserRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AppointmentsResponse {
    private ClinicUserRole role;
    private List<AppointmentResp> appointments;

    public AppointmentsResponse(ClinicUserRole role){
        this.role = role;
        appointments = new ArrayList<>();
    }
}