package ma.ensa.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.ensa.models.Patient;
import ma.ensa.repositories.LoginRepository;

public class LoginViewModel extends ViewModel {
    private final LoginRepository repository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LoginViewModel() {
        repository = new LoginRepository();
    }

    public LiveData<Patient> login(String login, String password) {
        isLoading.setValue(true);
        LiveData<Patient> result = repository.login(login, password);
        isLoading.setValue(false);
        return result;
    }

    public LiveData<String> getError() {
        return repository.getError();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
