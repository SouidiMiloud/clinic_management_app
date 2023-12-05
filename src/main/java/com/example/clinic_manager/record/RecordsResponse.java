package com.example.clinic_manager.record;


import com.example.clinic_manager.user.ClinicUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class RecordsResponse {

    private ClinicUserRole role;
    private List<RecordResp> records;

    public RecordsResponse(ClinicUserRole role){
        this.role = role;
        records = new ArrayList<>();
    }
}