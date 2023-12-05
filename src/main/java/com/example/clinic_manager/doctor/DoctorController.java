package com.example.clinic_manager.doctor;

import com.example.clinic_manager.appointment.AppointmentCheck;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/doctor")
@AllArgsConstructor
public class DoctorController {

    private DoctorService doctorService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String username, @RequestParam String phone,
                                           @RequestParam String password, @RequestParam String confirmPwd, @RequestParam String workStart, @RequestParam String workEnd,
                                           @RequestParam Specialty specialty, @RequestParam(value = "image", required = false) MultipartFile image) throws Exception{

        return doctorService.register(firstName, lastName, username, phone, password, confirmPwd, LocalTime.parse(workStart), LocalTime.parse(workEnd), specialty, image);
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> getDoctors(@RequestParam String specialty){

        return doctorService.getDoctors(specialty);
    }

    @PostMapping("/checkAppointment")
    public HttpStatus checkAppointment(@RequestBody AppointmentCheck check){

        return doctorService.checkAppointment(check);
    }
}