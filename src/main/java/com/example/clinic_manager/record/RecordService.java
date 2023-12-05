package com.example.clinic_manager.record;

import com.example.clinic_manager.appointment.Appointment;
import com.example.clinic_manager.appointment.AppointmentRepo;
import com.example.clinic_manager.doctor.Doctor;
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


    public ResponseEntity<String> saveRecord(ClinicUser doctor, Long appointmentId, RecordRequest request){

        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if(appointment.isEmpty())
            return ResponseEntity.badRequest().body("appointment not found");
        ClinicUser patient = appointment.get().getPatient();

        Record record = new Record(patient, (Doctor)doctor, LocalDateTime.now(), request.getDisease(),
                request.getTests(), request.getResults(), request.getMedications(), request.getDetails());

        recordRepo.save(record);
        return ResponseEntity.ok().body("record saved successfully");
    }

    public ResponseEntity<RecordsResponse> getRecords(ClinicUser user) {

        RecordsResponse response = new RecordsResponse(user.getRole());
        List<Record> records = recordRepo.getRecords(user);

        for(Record record : records){
            response.getRecords().add(processRecord(user, record));
        }
        return ResponseEntity.ok().body(response);
    }
    private RecordResp processRecord(ClinicUser user, Record record){
        ClinicUser participant;
        if(user.getRole().equals(ClinicUserRole.PATIENT))
            participant = record.getDoctor();
        else
            participant = record.getPatient();
        String name = participant.getFirstName() + ' ' + participant.getLastName();
        String dateOfVisit = record.getDateOfVisit().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        return new RecordResp(name, dateOfVisit, record.getDisease(), record.getTests(),
                record.getResults(), record.getMedications(), record.getDetails());
    }
}