package ma.ensa.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ma.ensa.models.Exercise;
import ma.ensa.repositories.ExerciseRepository;

public class ExerciseViewModel extends ViewModel {
    private final ExerciseRepository repository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public ExerciseViewModel() {
        repository = new ExerciseRepository();
    }

    public LiveData<List<Exercise>> getExercisesByProgramId(int programId) {
        isLoading.setValue(true);
        LiveData<List<Exercise>> exercises = repository.getExercisesByProgramId(programId);
        isLoading.setValue(false);
        return exercises;
    }

    public LiveData<String> getError() {
        return repository.getError();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}