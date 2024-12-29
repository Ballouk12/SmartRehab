package ma.ensa.controllers;

import ma.ensa.entities.Exercice;
import ma.ensa.services.ExerciceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exercices")
public class ExerciceController {

    @Autowired
    private ExerciceService exerciceService;

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllExercicesWithFiles() {
        try {
            // Appeler le service pour récupérer tous les exercices avec fichiers
            List<Map<String, Object>> exercices = exerciceService.getAllExercicesWithFiles();
            return ResponseEntity.ok(exercices);
        } catch (IOException e) {
            // En cas d'erreur, retourner un statut 500 avec un message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @PostMapping("/create")
    public String createExercice(
            @RequestParam("json") String exerciceJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @RequestPart(value = "vedio", required = false) MultipartFile vedioFile
    ) {
        return exerciceService.createExercice(exerciceJson, imageFile, vedioFile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExerciceWithFiles(@PathVariable int id) {
        try {
            Map<String, Object> exerciceData = exerciceService.getExerciceWithFiles(id);
            return ResponseEntity.ok(exerciceData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur : " + e.getMessage());
        }
    }

    @GetMapping("/program/{programId}")
    public List<Map<String, Object>> getExercicesByProgramId(@PathVariable int programId) throws IOException {
        return exerciceService.getExercicesByProgramId(programId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercice(@PathVariable int id) {
        try {
            exerciceService.deleteExercice(id);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
