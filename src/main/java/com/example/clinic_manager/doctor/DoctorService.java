package com.example.clinic_manager.doctor;

import com.example.clinic_manager.appointment.Appointment;
import com.example.clinic_manager.appointment.AppointmentCheck;
import com.example.clinic_manager.appointment.AppointmentRepo;
import com.example.clinic_manager.user.ClinicUser;
import com.example.clinic_manager.user.ClinicUserRepo;
import com.example.clinic_manager.user.ClinicUserRole;
import com.example.clinic_manager.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        if (!pwd.equals(confirmPwd))
            return ResponseEntity.badRequest().body("passwords don't match");

        String uniqueFileName = userService.getUniqueFileName(image);
        pwd = passwordEncoder.encode(pwd);
        Doctor doctor = new Doctor(fName, lName, username, phone, pwd, ClinicUserRole.DOCTOR,
                uniqueFileName, specialty, start, end, true);
        clinicUserRepo.save(doctor);

        return ResponseEntity.ok().body("saved successfully");
    }
    public ResponseEntity<List<DoctorResponse>> getDoctors(String specialty) {

        List<Doctor> doctors;
        if(specialty.equals("ALL"))
            doctors = clinicUserRepo.getDoctors();
        else
            doctors = clinicUserRepo.findDoctorsBySpecialty(Specialty.valueOf(specialty));

        List<DoctorResponse> doctorsResponse = new ArrayList<>();
        for(Doctor doctor : doctors) {
            doctorsResponse.add(processDoctor(doctor));
        }
        return ResponseEntity.ok().body(doctorsResponse);
    }
    private DoctorResponse processDoctor(Doctor doctor){
        String start = doctor.getWorkStart().format(DateTimeFormatter.ofPattern("HH:mm"));
        String end = doctor.getWorkEnd().format(DateTimeFormatter.ofPattern("HH:mm"));

        return new DoctorResponse(doctor.getFirstName(), doctor.getLastName(), doctor.getUsername(),
                doctor.getSpecialty(), start, end, doctor.getProfileImagePath());
    }

    public HttpStatus checkAppointment(AppointmentCheck check) {

        Optional<Appointment> appointmentOpt = appointmentRepo.findById(check.getId());
        if(appointmentOpt.isEmpty())
            return HttpStatus.BAD_REQUEST;
        Appointment appointment = appointmentOpt.get();
        appointment.setStatus(check.getDecision());
        appointmentRepo.save(appointment);
        updateAppointmentsNotif(appointment.getId());

        return HttpStatus.OK;
    }
    private void updateAppointmentsNotif(Long appointmentId){

        Appointment appointment = appointmentRepo.findById(appointmentId).get();
        ClinicUser patient = appointment.getPatient();
        userService.updateNotifications(patient, patient.getAppointmentsNotifNum() + 1, -1);

        Doctor doctor = (Doctor)appointment.getDoctor();
        userService.updateNotifications(doctor, doctor.getAppointmentsNotifNum() - 1, -1);
    }
}