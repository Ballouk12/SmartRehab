package ma.ensa.utils;

import ma.ensa.models.Exercise;

public class ExerciseDataHolder {
    private static ExerciseDataHolder instance;
    private Exercise currentExercise;

    private ExerciseDataHolder() {}

    public static ExerciseDataHolder getInstance() {
        if (instance == null) {
            instance = new ExerciseDataHolder();
        }
        return instance;
    }

    public void setExercise(Exercise exercise) {
        this.currentExercise = exercise;
    }

    public Exercise getExercise() {
        return currentExercise;
    }

    public void clear() {
        currentExercise = null;
    }
}
