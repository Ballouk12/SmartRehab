package ma.ensa.services;

import ma.ensa.dto.ProgramRequest;
import ma.ensa.dto.ProgramUpdateDTO;
import ma.ensa.entities.Exercice;
import ma.ensa.entities.Patient;
import ma.ensa.entities.Program;
import ma.ensa.entities.ProgramExercice;
import ma.ensa.repositories.ExerciceRepository;
import ma.ensa.repositories.PatientRepository;
import ma.ensa.repositories.ProgramExerciceRepository;
import ma.ensa.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProgramService {

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ExerciceRepository exerciceRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ProgramExerciceRepository programExerciceRepository;

    @Autowired
    private ExerciceService exerciceService;  // Injecter ExerciceService pour accéder à `getExerciceWithFiles`

    public List<Map<String, Object>> getALLProgramsByIdPatient(int patientId) throws IOException {
        // Récupérer tous les programmes associés à un patient
        List<Program> programs = programRepository.findByPatientId(patientId);

        // Préparer la liste de réponse avec les exercices enrichis
        List<Map<String, Object>> enrichedPrograms = new ArrayList<>();

        for (Program program : programs) {
            Map<String, Object> programResponse = new HashMap<>();
            programResponse.put("program", program);  // Ajout du programme de base

            // Ajouter les exercices associés à ce programme avec les fichiers encodés
            List<Map<String, Object>> enrichedExercises = new ArrayList<>();
            for (ProgramExercice programExercice : program.getExercises()) {
                try {
                    // Récupérer l'exercice avec les fichiers encodés
                    Map<String, Object> exerciceWithFiles = exerciceService.getExerciceWithFiles(programExercice.getExercice().getId());
                    enrichedExercises.add(exerciceWithFiles);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Ajouter un message d'erreur pour cet exercice
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Erreur lors de la récupération de l'exercice avec l'ID : " + programExercice.getExercice().getId());
                    enrichedExercises.add(errorResponse);
                }
            }

            programResponse.put("exercises", enrichedExercises);  // Ajouter la liste des exercices enrichis
            enrichedPrograms.add(programResponse);  // Ajouter le programme enrichi à la liste finale
        }

        return enrichedPrograms;  // Retourner la liste des programmes enrichis
    }

    public Program createProgram(ProgramRequest request) {
        // Créer une nouvelle instance de Program
        Program program = new Program();
        program.setName(request.getName());
        program.setDescription(request.getDescription());
        program.setPatient(patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient non trouvé")));

        // Ajouter les exercices au programme
        List<ProgramExercice> programExercices = new ArrayList<>();
        for (Integer exerciseId : request.getExerciseIds()) {
            Exercice exercice = exerciceRepository.findById(exerciseId)
                    .orElseThrow(() -> new RuntimeException("Exercice non trouvé avec ID : " + exerciseId));

            // Créer une association entre Program et Exercice
            ProgramExercice programExercice = new ProgramExercice();
            programExercice.setExercice(exercice);
            programExercice.setProgram(program);
            programExercices.add(programExercice);
        }
        program.setExercises(programExercices);

        // Sauvegarder le programme avec les associations
        return programRepository.save(program);
    }
    private static final String BASE_FILE_URL = "http://localhost:8080/api/programs/files/";

    public List<Program> getAllProgramsWithExercises() {
        List<Program> programs = programRepository.findAllWithExercises();

        // Ajouter des URLs pour les fichiers
        programs.forEach(program -> program.getExercises().forEach(programExercice -> {
            if (programExercice.getExercice().getImage() != null) {
                programExercice.getExercice().setImage(
                        BASE_FILE_URL + "images/" + extractFileName(programExercice.getExercice().getImage())
                );
            }
            if (programExercice.getExercice().getVedio() != null) {
                programExercice.getExercice().setVedio(
                        BASE_FILE_URL + "videos/" + extractFileName(programExercice.getExercice().getVedio())
                );
            }
        }));

        return programs;
    }

    private String extractFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf('/') + 1);
    }
    public void updateProgram(int id, ProgramUpdateDTO programUpdateDTO) {
        Program existingProgram = programRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programme avec ID " + id + " non trouvé."));

        // Mettre à jour les champs du programme
        existingProgram.setName(programUpdateDTO.getName());
        existingProgram.setDescription(programUpdateDTO.getDescription());

        // Récupérer le patient à partir de l'ID
        Patient patient = patientRepository.findById(programUpdateDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient avec ID " + programUpdateDTO.getPatientId() + " non trouvé."));
        existingProgram.setPatient(patient);

        List<ProgramExercice> programExercices = new ArrayList<>();
        for (Integer exerciseId : programUpdateDTO.getExerciseIds()) {
            System.out.println("les ids exercice "+exerciseId);
            Exercice exercice = exerciceRepository.findById(exerciseId)
                    .orElseThrow(() -> new RuntimeException("Exercice non trouvé avec ID : " + exerciseId));

            // Créer une association entre Program et Exercice
            ProgramExercice programExercice = new ProgramExercice();
            programExercice.setExercice(exercice);
            programExercice.setProgram(existingProgram);
            programExercices.add(programExercice);
            System.out.println("les prog exercice "+programExercice);
        }
            // Supprimer les anciennes associations et ajouter les nouvelles
            existingProgram.getExercises().clear();
            existingProgram.setExercises(programExercices);


        // Sauvegarder les modifications
        programRepository.save(existingProgram);
    }


    public void deleteProgram(int id) {
        if (programRepository.existsById(id)) {
            List<ProgramExercice> liens = programExerciceRepository.findByProgramId(id);
            if (liens.size() > 0) {
                programExerciceRepository.deleteAll(liens);
            }
            programRepository.deleteById(id);
        } else {
            throw new RuntimeException("Programme avec ID " + id + " n'existe pas.");
        }
    }



}
