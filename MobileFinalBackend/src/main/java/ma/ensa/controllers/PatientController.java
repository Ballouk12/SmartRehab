package ma.ensa.controllers;

import ma.ensa.dto.LoginRequest;
import ma.ensa.entities.Patient;
import ma.ensa.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Ajouter un patient
    @PostMapping("/create")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.addPatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

    // Modifier un patient
    @PutMapping("/update/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable int id, @RequestBody Patient patientDetails) {
        Patient updatedPatient = patientService.updatePatient(id, patientDetails);

        if (updatedPatient != null) {
            return ResponseEntity.ok(updatedPatient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un patient
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable int id) {
        boolean isDeleted = patientService.deletePatient(id);

        if (isDeleted) {
            return ResponseEntity.ok("Patient supprimé avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer un patient par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable int id) {
        Optional<Patient> patient = patientService.getPatientById(id);

        if (patient.isPresent()) {
            return ResponseEntity.ok(patient.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer tous les patients
    @GetMapping("/all")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    @PostMapping("/login")
    public Patient login(@RequestBody LoginRequest loginRequest) {
        return patientService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
    }
}
