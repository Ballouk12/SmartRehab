package ma.ensa.controllers;

import jakarta.annotation.Resource;
import ma.ensa.dto.ProgramRequest;
import ma.ensa.dto.ProgramUpdateDTO;
import ma.ensa.entities.Program;
import ma.ensa.services.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    @Autowired
    private ProgramService programService;
    private static final String UPLOAD_DIR = "D:/uploads/";


    @PostMapping("/create")
    public Program createProgram(@RequestBody ProgramRequest request) {
        return programService.createProgram(request);
    }

    // Récupérer tous les programmes avec leurs exercices
    @GetMapping
    public List<Program> getAllProgramsWithExercises() {
        return programService.getAllProgramsWithExercises();
    }

    // Servir un fichier (image ou vidéo)
    @GetMapping("/files/{type}/{filename}")
    public ResponseEntity<Resource> getFile(
            @PathVariable String type,
            @PathVariable String filename) {
        try {
            // Définir le chemin complet du fichier
            Path filePath = Paths.get(UPLOAD_DIR + type + "/" + filename);
            Resource resource = (Resource) new UrlResource(filePath.toUri());

            // Vérifier si le fichier existe et est lisible
            if (Files.exists(filePath) && Files.isReadable(filePath)) {
                // Utiliser Path.getFileName() pour obtenir le nom du fichier
                String fileName = filePath.getFileName().toString();

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public String updateProgram(@PathVariable int id, @RequestBody ProgramUpdateDTO programUpdateDTO) {
        try {
            programService.updateProgram(id, programUpdateDTO);
            return "Programme modifié avec succès.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la modification du programme.";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteProgram(@PathVariable int id) {
        try {
            programService.deleteProgram(id);
            return "Programme supprimé avec succès.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la suppression du programme.";
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Map<String, Object>>> getProgramsByPatientId(@PathVariable int patientId) {
        try {
            List<Map<String, Object>> programs = programService.getALLProgramsByIdPatient(patientId);
            return ResponseEntity.ok(programs);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(Collections.singletonMap("error", "Erreur lors de la récupération des programmes")));
        }

}}

