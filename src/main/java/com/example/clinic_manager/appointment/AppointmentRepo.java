package com.example.clinic_manager.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    @Query("select a from Appointment a where a.doctorId=?1 and a.status=?2")
    List<Appointment> getAppointmentsByStatus(Long doctorId, Status status);

    @Query("select distinct a from Appointment a inner join ClinicUser u on u.id in (a.patientId, a.doctorId) where u.username like concat('%', ?2, '%') and ?1 in (a.doctorId, a.patientId) order by a.start desc")
    List<Appointment> getAppointmentsByUserId(Long userId, String username);
}