package ma.ensa.services;

import ma.ensa.entities.Patient;
import ma.ensa.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Ajouter un patient
    public Patient addPatient(Patient patient) {
        patient.setPassword(bCryptPasswordEncoder.encode(patient.getPassword()));

        return patientRepository.save(patient);
    }

    // Modifier un patient existant
    public Patient updatePatient(int id, Patient patientDetails) {
        Optional<Patient> patient = patientRepository.findById(id);

        if (patient.isPresent()) {
            Patient existingPatient = patient.get();
            existingPatient.setNom(patientDetails.getNom());
            existingPatient.setPrenom(patientDetails.getPrenom());
            existingPatient.setLogin(patientDetails.getLogin());
            existingPatient.setPassword(bCryptPasswordEncoder.encode(patientDetails.getPassword()));
            existingPatient.setEmail(patientDetails.getEmail());
            return patientRepository.save(existingPatient);
        }

        return null; // Si le patient n'existe pas
    }

    // Supprimer un patient
    public boolean deletePatient(int id) {
        Optional<Patient> patient = patientRepository.findById(id);

        if (patient.isPresent()) {
            patientRepository.delete(patient.get());
            return true;
        }

        return false; // Si le patient n'existe pas
    }

    // Récupérer un patient par son ID
    public Optional<Patient> getPatientById(int id) {
        return patientRepository.findById(id);
    }

    // Récupérer tous les patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient authenticate(String login, String password) {
        Optional<Patient> patient = patientRepository.findByLogin(login);

        if (patient.isPresent()) {
            Patient existingPatient = patient.get();

            // Vérification du mot de passe
            if (bCryptPasswordEncoder.matches(password, existingPatient.getPassword())) {
                return existingPatient;
            }
        }
        return null;
    }

}
