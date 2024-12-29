package ma.ensa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProgramExercice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Exercice exercice;
    @JsonIgnore
    @ManyToOne
    private Program program;
    private float exerciceScore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public float getExerciceScore() {
        return exerciceScore;
    }

    public void setExerciceScore(float exerciceScore) {
        this.exerciceScore = exerciceScore;
    }

    @Override
    public String toString() {
        return "ProgramExercice{" +
                "id=" + id +
                ", exercice=" + exercice +
                ", program=" + program +
                ", exerciceScore=" + exerciceScore +
                '}';
    }
}
