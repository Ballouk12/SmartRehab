package ma.ensa.controllers;

import ma.ensa.dto.LoginRequest;
import ma.ensa.entities.Doctor;
import ma.ensa.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://127.0.0.1:5173",
        "http://frontend:5173",
        "http://172.18.0.4:5173"
})
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    // Endpoint pour l'authentification
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = doctorService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
        if (isAuthenticated) {
            return "Authentification réussie!";
        } else {
            return "Échec de l'authentification. Veuillez vérifier vos informations.";
        }
    }


    // Endpoint pour enregistrer un nouveau docteur
    @PostMapping("/register")
    public String register(@RequestBody Doctor doctor) {
        Doctor doctor1 = new Doctor();
        doctor1.setPrenom(doctor.getPrenom());
        doctor1.setNom(doctor.getNom());
        doctor1.setLogin(doctor.getLogin());
        doctor1.setPassword(doctor.getPassword());

        doctorService.registerDoctor(doctor1);
        return "Docteur enregistré avec succès!";
    }
}
