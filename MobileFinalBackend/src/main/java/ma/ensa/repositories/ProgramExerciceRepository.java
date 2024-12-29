package ma.ensa.repositories;

import ma.ensa.entities.ProgramExercice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramExerciceRepository extends JpaRepository<ProgramExercice, Integer> {
    List<ProgramExercice> findByProgramId(int programId);
    List<ProgramExercice> findByExerciceId(int exerciceId);
}
