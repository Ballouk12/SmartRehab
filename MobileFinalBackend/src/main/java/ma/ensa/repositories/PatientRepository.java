package ma.ensa.repositories;

import ma.ensa.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    public Optional<Patient> findByLogin(String login);
}
