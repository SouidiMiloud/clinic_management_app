package com.example.clinic_manager.user;

import com.example.clinic_manager.appointment.AppointmentRequest;
import com.example.clinic_manager.appointment.AppointmentsResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String username, @RequestParam String phone,
                                           @RequestParam String password, @RequestParam String confirmPwd, @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        return userService.register(firstName, lastName, username, phone, password, confirmPwd, image);
    }

    @PostMapping("/appointment")
    public ResponseEntity<String> takeAppointment(@AuthenticationPrincipal ClinicUser user, @RequestBody AppointmentRequest request){

        return userService.takeAppointment(user, request);
    }

    @PostMapping("/saveAppointment")
    public ResponseEntity<String> saveAppointment(@AuthenticationPrincipal ClinicUser user, @RequestBody AppointmentRequest request){

        return userService.saveAppointment(user, request);
    }

    @GetMapping("/appointments")
    public ResponseEntity<AppointmentsResponse> getAppointments(@AuthenticationPrincipal ClinicUser user, @RequestParam String username){

        return userService.getAppointments(user, username);
    }

    @GetMapping("/getUserNotifs")
    public ResponseEntity<UserNotifResponse> userNotifInfo(@AuthenticationPrincipal ClinicUser user){

        return userService.getUserNotifs(user);
    }

    @GetMapping("/getUsername")
    public ResponseEntity<String> getUsername(@AuthenticationPrincipal ClinicUser user){

        return ResponseEntity.ok().body(user.getUsername());
    }
}