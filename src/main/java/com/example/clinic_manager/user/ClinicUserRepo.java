package com.example.clinic_manager.user;

import com.example.clinic_manager.doctor.Doctor;
import com.example.clinic_manager.doctor.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ClinicUserRepo extends JpaRepository<ClinicUser, Long> {

    Optional<ClinicUser> findByUsername(String username);

    @Query("SELECT d FROM ClinicUser d WHERE d.role='DOCTOR'")
    List<Doctor> getDoctors();
    
    @Query("SELECT d FROM Doctor d WHERE d.role='DOCTOR' AND d.specialty=?1")
    List<Doctor> findDoctorsBySpecialty(Specialty specialty);
}