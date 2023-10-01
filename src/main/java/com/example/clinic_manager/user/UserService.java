package com.example.clinic_manager.user;

import com.example.clinic_manager.appointment.*;
import com.example.clinic_manager.doctor.Doctor;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@AllArgsConstructor
public class UserService {

    private PasswordEncoder passwordEncoder;
    private ClinicUserRepo userRepo;
    private AppointmentRepo appointmentRepo;
    private SimpMessagingTemplate messagingTemplate;

    public ResponseEntity<String> register(String fName, String lName, String username, String phone,
                                           String pwd, String confirmPwd, MultipartFile image) throws IOException {

        if (!pwd.equals(confirmPwd))
            return ResponseEntity.badRequest().body("passwords don't match");

        String uniqueFileName = "";
        if (image != null && !image.isEmpty()) {
            uniqueFileName = UUID.randomUUID().toString() + '_' + image.getOriginalFilename();
            File dest = new File("C:/Users/Electro Ragragui/Downloads/clinic_manager_react/public/images/" + uniqueFileName);
            image.transferTo(dest);
        }
        pwd = passwordEncoder.encode(pwd);
        ClinicUser user = new ClinicUser(fName, lName, username, phone, pwd, ClinicUserRole.PATIENT, uniqueFileName);
        userRepo.save(user);
        return ResponseEntity.ok().body("saved successfully");
    }
    public ResponseEntity<String> takeAppointment(ClinicUser user, AppointmentRequest request) {

        Optional<ClinicUser> doctor_ = userRepo.findByUsername(request.getDoctorUsername());
        if (doctor_.isEmpty())
            return ResponseEntity.badRequest().body("doctor not found");
        Doctor doctor = (Doctor) doctor_.get();
        if(request.getStart().isBefore(LocalDateTime.now()))
            return ResponseEntity.badRequest().body("the date is not in the future");

        if(request.getStart().toLocalTime().isBefore(doctor.getWorkStart()) || request.getStart().plusMinutes(request.getDuration()).toLocalTime().isAfter(doctor.getWorkEnd()))
            return ResponseEntity.badRequest().body("the doctor works from " + doctor.getWorkStart().toString() + " to " + doctor.getWorkEnd().toString());

        if(!isValidAppointment(doctor.getId(), request.getStart(), request.getStart().plusMinutes(request.getDuration()), Status.CONFIRMED))
            return ResponseEntity.badRequest().body("this appointment is already taken");

        if(!isValidAppointment(doctor.getId(), request.getStart(), request.getStart().plusMinutes(request.getDuration()), Status.UNCHECKED))
            return ResponseEntity.ok().body("this appointment is already taken but not confirmed yet");

        return saveAppointment(user, request);
    }
    public ResponseEntity<String> saveAppointment(ClinicUser user, AppointmentRequest request){

        Optional<ClinicUser> doctor_ = userRepo.findByUsername(request.getDoctorUsername());
        Doctor doctor = (Doctor) doctor_.get();

        doctor.setAppointmentsNotifNum(doctor.getAppointmentsNotifNum() + 1);
        doctor.setNotificationsNum(doctor.getNotificationsNum() + 1);
        userRepo.save(doctor);
        appointmentRepo.save(new Appointment(user.getId(), doctor.getId(), request.getStart(), request.getDuration(), Status.UNCHECKED, request.getDescription()));
        sendNotifs(doctor.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body("appointment saved and sent to the doctor successfully");
    }
    public Boolean isValidAppointment(Long doctorId, LocalDateTime start, LocalDateTime end, Status status){

        List<Appointment> appointments = appointmentRepo.getAppointmentsByStatus(doctorId, status);
        for(Appointment a : appointments){
            if(start.isEqual(a.getStart()) || start.isEqual(a.getStart().plusMinutes(a.getDuration())) ||
               end.isEqual(a.getStart()) || end.isEqual(a.getStart().plusMinutes(a.getDuration())))
                return false;
            if((start.isAfter(a.getStart()) && start.isBefore(a.getStart().plusMinutes(a.getDuration())))
               || (end.isAfter(a.getStart())) && end.isBefore(a.getStart().plusMinutes(a.getDuration())))
                return false;
        }
        return true;
    }
    public ResponseEntity<Map<String, Object>> getAppointments(ClinicUser user, String searchedUsername) {

        Map<String, Object> map = new HashMap<>();
        map.put("role", user.getRole());
        List<Appointment> appointments = appointmentRepo.getAppointmentsByUserId(user.getId(), searchedUsername);
        List<AppointmentResponse> response = new ArrayList<>();
        String name, start;
        ClinicUser clinicUser;
        for(Appointment a : appointments){
            if(user.getRole().equals(ClinicUserRole.DOCTOR))
                clinicUser = userRepo.findById(a.getPatientId()).get();
            else clinicUser = userRepo.findById(a.getDoctorId()).get();
            name = clinicUser.getFirstName() + ' ' + clinicUser.getLastName();
            start = a.getStart().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            response.add(new AppointmentResponse(a.getId(), clinicUser.getUsername(), name,
                    start, a.getDuration(), a.getDescription(), a.getStatus().toString()));
        }
        map.put("appointments", response);
        if(user.getRole().equals(ClinicUserRole.PATIENT)) {
            user.setNotificationsNum(user.getNotificationsNum() - user.getAppointmentsNotifNum());
            user.setAppointmentsNotifNum(0);
            userRepo.save(user);
            sendNotifs(user.getId());
        }
        return ResponseEntity.ok().body(map);
    }

    public ResponseEntity<UserNotifResponse> getUserNotifs(ClinicUser user) {

        UserNotifResponse notifResponse = new UserNotifResponse(user.getRole().toString(), user.getUsername(),
                                            user.getAppointmentsNotifNum().toString(), user.getMessagesNum().toString());
        return ResponseEntity.ok().body(notifResponse);
    }

    public void sendNotifs(Long userId){

        Optional<ClinicUser> userOpt = userRepo.findById(userId);
        if(userOpt.isEmpty())
            return;
        ClinicUser user = userOpt.get();

        messagingTemplate.convertAndSendToUser(user.getUsername(), "/private", user.getNotificationsNum());
    }
}