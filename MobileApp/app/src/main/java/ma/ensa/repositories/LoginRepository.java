package ma.ensa.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import ma.ensa.config.ApiService;
import ma.ensa.models.LoginRequest;
import ma.ensa.models.Patient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRepository {
    private final String TAG = "login request";
    private final ApiService apiService;
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LoginRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http:/192.168.1.135:8083/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public LiveData<Patient> login(String login, String password) {
        MutableLiveData<Patient> patientData = new MutableLiveData<>();

        LoginRequest request = new LoginRequest(login, password);
        apiService.login(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG,"le serveur reponde avec succes rest just quelque ajustement");
                    Log.d(TAG,"le patient recuperer"+response);

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Patient patient = new Patient();
                        patient.setId(jsonObject.getInt("id"));
                        patient.setNom(jsonObject.getString("nom"));
                        patient.setPrenom(jsonObject.getString("prenom"));
                        patient.setEmail(jsonObject.getString("email"));
                        Log.d(TAG,"le patient recuperer"+patient);

                        patientData.setValue(patient);
                    } catch (Exception e) {
                        errorLiveData.setValue("Erreur lors de la lecture des données");
                        Log.d(TAG,"erreur lors de la lecture des donnees de patient");

                    }
                } else {
                    Log.d(TAG,"le serveur reponde avec succes mes les infos sont incorrects");
                    Log.d(TAG,"voici le patient recuperer"+response);
                    errorLiveData.setValue("Identifiants incorrects");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG,"le serveur reponde n'est pas acessible erreur reseau");
                errorLiveData.setValue("Erreur réseau: " + t.getMessage());
            }
        });

        return patientData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }
}