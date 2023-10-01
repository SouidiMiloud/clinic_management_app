package com.example.clinic_manager.doctor;

import com.example.clinic_manager.appointment.Appointment;
import com.example.clinic_manager.appointment.AppointmentRepo;
import com.example.clinic_manager.appointment.Status;
import com.example.clinic_manager.user.ClinicUser;
import com.example.clinic_manager.user.ClinicUserRepo;
import com.example.clinic_manager.user.ClinicUserRole;
import com.example.clinic_manager.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@AllArgsConstructor
public class DoctorService {

    private ClinicUserRepo clinicUserRepo;
    private AppointmentRepo appointmentRepo;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    public ResponseEntity<String> register(String fName, String lName, String username, String phone,
                                           String pwd, String confirmPwd, LocalTime start, LocalTime end,
                                           Specialty specialty, MultipartFile image) throws IOException {

        if(!pwd.equals(confirmPwd))
            return ResponseEntity.badRequest().body("passwords don't match");

        String uniqueFileName = "";
        if(image != null && !image.isEmpty()) {
            uniqueFileName = UUID.randomUUID().toString() + '_' + image.getOriginalFilename();
            File dest = new File("C:/Users/Electro Ragragui/Downloads/clinic_manager_react/public/images/" + uniqueFileName);
            image.transferTo(dest);
        }
        pwd = passwordEncoder.encode(pwd);
        Doctor doctor = new Doctor(fName, lName, username, phone, pwd, ClinicUserRole.DOCTOR,
                uniqueFileName, specialty, start, end, true);
        clinicUserRepo.save(doctor);

        return ResponseEntity.ok().body("saved successfully");
    }

    public ResponseEntity<List<DoctorResponse>> getDoctors(String specialty) {

        List<Doctor> doctors_;
        if(specialty.equals("ALL"))
            doctors_ = clinicUserRepo.findUsersByRole(ClinicUserRole.DOCTOR);
        else
            doctors_ = clinicUserRepo.findDoctorsBySpecialty(ClinicUserRole.DOCTOR, Specialty.valueOf(specialty));

        List<DoctorResponse> doctors = new ArrayList<>();
        String start, end;
        for(Doctor d : doctors_) {
            start = d.getWorkStart().format(DateTimeFormatter.ofPattern("HH:mm"));
            end = d.getWorkEnd().format(DateTimeFormatter.ofPattern("HH:mm"));
            doctors.add(new DoctorResponse(d.getFirstName(), d.getLastName(), d.getUsername(),
                    d.getSpecialty(), start, end, d.getProfileImagePath()));
        }
        return ResponseEntity.ok().body(doctors);
    }

    public HttpStatus checkAppointment(Map<String, String> request) {

        Optional<Appointment> appointment_ = appointmentRepo.findById(Long.parseLong(request.get("id")));
        if(appointment_.isEmpty())
            return HttpStatus.BAD_REQUEST;
        Appointment appointment = appointment_.get();
        appointment.setStatus(Status.valueOf(request.get("decision")));
        appointmentRepo.save(appointment);
        updateAppointmentsNotif(appointment.getId());

        return HttpStatus.OK;
    }
    private void updateAppointmentsNotif(Long appointmentId){

        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        ClinicUser patient = clinicUserRepo.findById(appointment.getPatientId()).get();
        patient.setAppointmentsNotifNum(patient.getAppointmentsNotifNum() + 1);
        patient.setNotificationsNum(patient.getNotificationsNum() + 1);
        clinicUserRepo.save(patient);

        Doctor doctor = (Doctor)clinicUserRepo.findById(appointment.getDoctorId()).get();
        doctor.setAppointmentsNotifNum(doctor.getAppointmentsNotifNum() - 1);
        doctor.setNotificationsNum(doctor.getNotificationsNum() - 1);
        clinicUserRepo.save(doctor);

        userService.sendNotifs(patient.getId());
        userService.sendNotifs(doctor.getId());
    }
}