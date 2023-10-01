package com.example.clinic_manager.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RecordRepo extends JpaRepository<Record, Long> {

    @Query("select r from Record r where ?1 in (r.doctorId, r.patientId) order by r.dateOfVisit desc")
    List<Record> getRecords(Long userId);
}