package com.example.clinic_manager.appointment;

import com.example.clinic_manager.doctor.Doctor;
import com.example.clinic_manager.user.ClinicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.doctor=?1 AND a.status=?2")
    List<Appointment> getAppointmentsByStatus(Doctor doctor, Status status);

    @Query("SELECT a FROM Appointment a" +
            " WHERE (a.patient=?1 AND a.doctor.username LIKE CONCAT('%', ?2, '%'))" +
            "OR (a.doctor=?1 AND a.patient.username LIKE CONCAT('%', ?2, '%'))" +
            "ORDER BY a.start DESC")
    List<Appointment> getAppointmentsByUserId(ClinicUser user, String searchedUsername);
}