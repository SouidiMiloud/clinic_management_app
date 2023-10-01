package com.example.clinic_manager.record;

import com.example.clinic_manager.appointment.Appointment;
import com.example.clinic_manager.appointment.AppointmentRepo;
import com.example.clinic_manager.user.ClinicUser;
import com.example.clinic_manager.user.ClinicUserRepo;
import com.example.clinic_manager.user.ClinicUserRole;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@AllArgsConstructor
public class RecordService {

    private RecordRepo recordRepo;
    private AppointmentRepo appointmentRepo;
    private ClinicUserRepo clinicUserRepo;


    public ResponseEntity<String> saveRecord(ClinicUser doctor, Long appointmentId, RecordRequest request){

        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if(appointment.isEmpty())
            return ResponseEntity.badRequest().body("appointment not found");
        ClinicUser patient = clinicUserRepo.findById(appointment.get().getPatientId()).get();

        Record record = new Record(patient.getId(), doctor.getId(), LocalDateTime.now(), request.getDisease(),
                request.getTests(), request.getResults(), request.getMedications(), request.getDetails());

        recordRepo.save(record);
        return ResponseEntity.ok().body("record saved successfully");
    }

    public ResponseEntity<Map<String, Object>> getRecords(ClinicUser user) {

        Map<String, Object> map = new HashMap<>();
        map.put("role", user.getRole());
        List<Record> records = recordRepo.getRecords(user.getId());
        List<RecordResponse> response = new ArrayList<>();
        String name, dateOfVisit;
        ClinicUser participant;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for(Record r : records){
            if(user.getRole().equals(ClinicUserRole.PATIENT))
                participant = clinicUserRepo.findById(r.getDoctorId()).get();
            else participant = clinicUserRepo.findById(r.getPatientId()).get();
            name = participant.getFirstName() + ' ' + participant.getLastName();
            dateOfVisit = r.getDateOfVisit().format(formatter);
            response.add(new RecordResponse(name, dateOfVisit, r.getDisease(), r.getTests(),
                              r.getResults(), r.getMedications(), r.getDetails()));
        }
        map.put("records", response);
        return ResponseEntity.ok().body(map);
    }
}