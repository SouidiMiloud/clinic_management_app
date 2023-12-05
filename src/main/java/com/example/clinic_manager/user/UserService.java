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
import java.time.LocalTime;
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
        String uniqueFileName = getUniqueFileName(image);

        pwd = passwordEncoder.encode(pwd);
        ClinicUser user = new ClinicUser(fName, lName, username, phone, pwd, ClinicUserRole.PATIENT, uniqueFileName);
        userRepo.save(user);
        return ResponseEntity.ok().body("saved successfully");
    }
    public String getUniqueFileName(MultipartFile image) throws IOException{
        String uniqueFileName = "";
        if (image != null && !image.isEmpty()) {
            uniqueFileName = UUID.randomUUID().toString() + '_' + image.getOriginalFilename();
            File dest = new File("C:/Users/Electro Ragragui/Downloads/clinic_manager_react/public/images/" + uniqueFileName);
            image.transferTo(dest);
        }
        return uniqueFileName;
    }
    public ResponseEntity<String> takeAppointment(ClinicUser user, AppointmentRequest request) {

        Optional<ClinicUser> doctorOpt = userRepo.findByUsername(request.getDoctorUsername());
        if (doctorOpt.isEmpty())
            return ResponseEntity.badRequest().body("doctor not found");
        Doctor doctor = (Doctor) doctorOpt.get();
        ResponseEntity<String> response = validateAppointment(doctor, request.getStart(), request.getDuration());
        if(response.getStatusCode() == HttpStatus.OK)
            return saveAppointment(user, request);
        return response;
    }
    public ResponseEntity<String> saveAppointment(ClinicUser user, AppointmentRequest request){
        Optional<ClinicUser> doctorOpt = userRepo.findByUsername(request.getDoctorUsername());
        if(doctorOpt.isEmpty())
            return ResponseEntity.badRequest().body("Doctor not found");
        Doctor doctor = (Doctor) doctorOpt.get();
        updateNotifications(doctor, doctor.getAppointmentsNotifNum() + 1, -1);

        appointmentRepo.save(new Appointment(user, doctor, request.getStart(), request.getDuration(), Status.UNCHECKED, request.getDescription()));
        return ResponseEntity.status(HttpStatus.CREATED).body("appointment saved and sent to the doctor successfully");
    }
    private Boolean isAppointmentAvailable(Doctor doctor, LocalDateTime start, int duration, Status status){
        LocalDateTime end = start.plusMinutes(duration);
        List<Appointment> appointments = appointmentRepo.getAppointmentsByStatus(doctor, status);
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
    private ResponseEntity<String> validateAppointment(Doctor doctor, LocalDateTime start, int duration){
        if(start.isBefore(LocalDateTime.now()))
            return ResponseEntity.badRequest().body("The date is not in the future");
        if(!isInWorkingHours(doctor, start, duration))
            return ResponseEntity.badRequest().body("The doctor works from "
                    + doctor.getWorkStart().toString() + " to " + doctor.getWorkEnd().toString());

        if(!isAppointmentAvailable(doctor, start, duration, Status.CONFIRMED))
            return ResponseEntity.badRequest().body("This appointment is already taken");
        if(!isAppointmentAvailable(doctor, start, duration, Status.UNCHECKED))
            return ResponseEntity.accepted().body("This appointment is already taken but not confirmed yet");
        return ResponseEntity.ok().body("");
    }
    private boolean isInWorkingHours(Doctor doctor, LocalDateTime start, int duration){
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = start.plusMinutes(duration).toLocalTime();
        return startTime.isAfter(doctor.getWorkStart()) && endTime.isBefore(doctor.getWorkEnd());
    }
    public ResponseEntity<AppointmentsResponse> getAppointments(ClinicUser user, String searchedUsername) {

        AppointmentsResponse response = new AppointmentsResponse(user.getRole());
        List<Appointment> appointments = appointmentRepo.getAppointmentsByUserId(user, searchedUsername);
        for(Appointment appointment : appointments){
            response.getAppointments().add(processAppointment(user, appointment));
        }
        if(user.getRole().equals(ClinicUserRole.PATIENT))
            updateNotifications(user, 0, -1);

        return ResponseEntity.ok().body(response);
    }
    private AppointmentResp processAppointment(ClinicUser user, Appointment app){
        ClinicUser participantUser;
        if(user.getRole().equals(ClinicUserRole.DOCTOR))
            participantUser = app.getPatient();
        else
            participantUser = app.getDoctor();
        String name = participantUser.getFirstName() + ' ' + participantUser.getLastName();
        String start = app.getStart().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        return new AppointmentResp(app.getId(), participantUser.getUsername(), name,
                start, app.getDuration(), app.getDescription(), app.getStatus().toString());
    }

    public void updateNotifications(ClinicUser user, int appointmentsNotif, int messagesNotif){
        if(appointmentsNotif != -1)
            user.setAppointmentsNotifNum(appointmentsNotif);
        if(messagesNotif != -1)
            user.setMessagesNum(messagesNotif);
        sendNotifs(user.getId());
        userRepo.save(user);
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
        int notifsNum = user.getAppointmentsNotifNum() + user.getMessagesNum();
        messagingTemplate.convertAndSendToUser(user.getUsername(), "/private", notifsNum);
    }
}