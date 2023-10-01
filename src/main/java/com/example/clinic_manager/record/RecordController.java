package com.example.clinic_manager.record;

import com.example.clinic_manager.user.ClinicUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/record")
public class RecordController {

    private RecordService recordService;


    @PostMapping("/saveRecord")
    public ResponseEntity<String> saveRecord(@AuthenticationPrincipal ClinicUser doctor, @RequestParam Long appointmentId, @RequestBody RecordRequest request){

        return recordService.saveRecord(doctor, appointmentId, request);
    }

    @GetMapping("/getRecords")
    public ResponseEntity<Map<String, Object>> getRecords(@AuthenticationPrincipal ClinicUser user){

        return recordService.getRecords(user);
    }
}