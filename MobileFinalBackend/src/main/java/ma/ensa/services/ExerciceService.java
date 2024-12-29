package ma.ensa.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ensa.entities.Exercice;
import ma.ensa.entities.Program;
import ma.ensa.entities.ProgramExercice;
import ma.ensa.repositories.ExerciceRepository;
import ma.ensa.repositories.ProgramExerciceRepository;
import ma.ensa.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ExerciceService {

    private static final String UPLOAD_DIR = "D:/uploads/";

    @Autowired
    private ExerciceRepository exerciceRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProgramExerciceRepository programExerciceRepository;

    public String createExercice(String exerciceJson, MultipartFile imageFile, MultipartFile vedioFile) {
        try {
            // Convert JSON en objet Exercice
            ObjectMapper objectMapper = new ObjectMapper();
            Exercice exercice = objectMapper.readValue(exerciceJson, Exercice.class);

            // Gérer le fichier image
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveFile(imageFile, "images");
                exercice.setImage(imagePath);
            }

            // Gérer le fichier vidéo
            if (vedioFile != null && !vedioFile.isEmpty()) {
                String vedioPath = saveFile(vedioFile, "videos");
                exercice.setVedio(vedioPath);
            }

            // Sauvegarder l'entité Exercice
            exerciceRepository.save(exercice);

            return "Exercice créé avec succès!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la création de l'exercice.";
        }
    }

    private String saveFile(MultipartFile file, String subFolder) throws IOException {
        // Définir le chemin complet pour le fichier
        String folderPath = UPLOAD_DIR + subFolder + "/";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs(); // Créer les dossiers si nécessaire
        }

        // Générer un nom unique pour le fichier
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = folderPath + fileName;

        // Sauvegarder le fichier
        file.transferTo(new File(filePath));

        // Retourner le chemin relatif pour stocker dans la base de données
        return "/uploads/" + subFolder + "/" + fileName;
    }


    public Map<String, Object> getExerciceWithFiles(int exerciceId) throws IOException {
        // Récupérer l'exercice depuis la base de données
        Exercice exercice = exerciceRepository.findById(exerciceId).orElse(null);

        if (exercice == null) {
            throw new IllegalArgumentException("Exercice introuvable pour l'ID : " + exerciceId);
        }

        // Préparer la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("exercice", exercice);

        // Encoder l'image en base64
        if (exercice.getImage() != null) {
            Path imagePath = Paths.get("D:/uploads/" + exercice.getImage().replaceFirst("/uploads/", ""));
            if (Files.exists(imagePath)) {
                byte[] imageBytes = Files.readAllBytes(imagePath);
                String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                response.put("image", imageBase64);
            } else {
                response.put("image", null);
            }
        }

        // Encoder la vidéo en base64
        if (exercice.getVedio() != null) {
            Path vedioPath = Paths.get("D:/uploads/" + exercice.getVedio().replaceFirst("/uploads/", ""));
            if (Files.exists(vedioPath)) {
                byte[] vedioBytes = Files.readAllBytes(vedioPath);
                String vedioBase64 = Base64.getEncoder().encodeToString(vedioBytes);
                response.put("vedio", vedioBase64);
            } else {
                response.put("vedio", null);
            }
        }

        return response;
    }

    public List<Map<String, Object>> getExercicesByProgramId(int programId) throws IOException {
        List<Map<String, Object>> response = new ArrayList<>();

        // Récupérer le programme avec ses exercices
        Optional<Program> programOptional = programRepository.findById(programId);
        if (!programOptional.isPresent()) {
            throw new IllegalArgumentException("Programme introuvable pour l'ID : " + programId);
        }
        List<ProgramExercice> programExercices = programOptional.get().getExercises() ;
        for (ProgramExercice pexercice : programExercices) {
            try {
                // Ajouter chaque exercice avec ses fichiers (image, vidéo) encodés
                response.add(getExerciceWithFiles(pexercice.getExercice().getId()));
            } catch (Exception e) {
                e.printStackTrace();
                // Ajouter un message d'erreur pour cet exercice
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Erreur lors de la récupération de l'exercice avec l'ID : " + pexercice.getExercice().getId());
                response.add(errorResponse);
            }
        }

        return response;
    }

    public List<Exercice> getAllExercices() {
        return exerciceRepository.findAll();
    }
    public List<Map<String, Object>> getAllExercicesWithFiles() throws IOException {
        // Récupérer tous les exercices depuis la base de données
        List<Exercice> exercices = exerciceRepository.findAll();

        // Préparer la liste de réponse
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Exercice exercice : exercices) {
            // Préparer la map pour un exercice
            Map<String, Object> response = new HashMap<>();
            response.put("exercice", exercice);

            // Encoder l'image en base64
            if (exercice.getImage() != null) {
                Path imagePath = Paths.get("D:/uploads/" + exercice.getImage().replaceFirst("/uploads/", ""));
                if (Files.exists(imagePath)) {
                    byte[] imageBytes = Files.readAllBytes(imagePath);
                    String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                    response.put("image", imageBase64);
                } else {
                    response.put("image", null);
                }
            }

            // Encoder la vidéo en base64
            if (exercice.getVedio() != null) {
                Path vedioPath = Paths.get("D:/uploads/" + exercice.getVedio().replaceFirst("/uploads/", ""));
                if (Files.exists(vedioPath)) {
                    byte[] vedioBytes = Files.readAllBytes(vedioPath);
                    String vedioBase64 = Base64.getEncoder().encodeToString(vedioBytes);
                    response.put("vedio", vedioBase64);
                } else {
                    response.put("vedio", null);
                }
            }

            // Ajouter la map de l'exercice à la liste
            responseList.add(response);
        }

        return responseList;
    }

    public void deleteExercice(int id) {
        List<ProgramExercice> liens = programExerciceRepository.findByExerciceId(id) ;
        if(liens.size()>0) {
            programExerciceRepository.deleteAll(liens);
        }
        exerciceRepository.deleteById(id);
    }
}
