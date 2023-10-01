package com.example.clinic_manager.user;

import com.example.clinic_manager.doctor.Doctor;
import com.example.clinic_manager.doctor.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ClinicUserRepo extends JpaRepository<ClinicUser, Long> {

    Optional<ClinicUser> findByUsername(String username);

    @Query("select d from ClinicUser d where d.role=?1")
    List<Doctor> findUsersByRole(ClinicUserRole role);

    @Query("select d from Doctor d where d.role=?1 and d.specialty=?2")
    List<Doctor> findDoctorsBySpecialty(ClinicUserRole role, Specialty specialty);
}