package ma.ensa.services;

import ma.ensa.entities.Doctor;
import ma.ensa.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    private  DoctorRepository doctorRepository;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;

    // Méthode pour enregistrer un nouveau docteur
    public Doctor registerDoctor(Doctor doctor) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);
    }

    // Méthode pour authentifier un docteur
    public boolean authenticate(String login, String rawPassword) {
        Optional<Doctor> doctorOptional = doctorRepository.findByLogin(login);

        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            return passwordEncoder.matches(rawPassword, doctor.getPassword());
        }
        return false;
    }
}
