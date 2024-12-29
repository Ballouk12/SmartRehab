package ma.ensa.repositories;

import ma.ensa.entities.Exercice;
import ma.ensa.entities.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {
    @Query("SELECT p FROM Program p LEFT JOIN FETCH p.exercises pe LEFT JOIN FETCH pe.exercice")
    List<Program> findAllWithExercises();
    List<Program> findByPatientId(int patientId);
}
