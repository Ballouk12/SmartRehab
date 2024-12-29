package ma.ensa.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ma.ensa.models.Program;
import ma.ensa.repositories.ProgramRepository;

public class ProgramViewModel extends ViewModel {
    private ProgramRepository repository;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public ProgramViewModel() {
        repository = new ProgramRepository();
    }

    public LiveData<List<Program>> getProgramsByPatientId(int patientId) {
        isLoading.setValue(true);
        LiveData<List<Program>> programs = repository.getProgramsByPatientId(patientId);
        isLoading.setValue(false);
        return programs;
    }

    public LiveData<String> getError() {
        return repository.getError();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}