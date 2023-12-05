package com.example.clinic_manager.record;

import com.example.clinic_manager.user.ClinicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RecordRepo extends JpaRepository<Record, Long> {

    @Query("SELECT r FROM Record r" +
            " WHERE ?1 IN (r.doctor, r.patient)" +
            "ORDER BY r.dateOfVisit DESC")
    List<Record> getRecords(ClinicUser user);
}