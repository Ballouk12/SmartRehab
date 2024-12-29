package ma.ensa.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ma.ensa.config.ApiService;
import ma.ensa.models.Exercise;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExerciseRepository {
    private final ApiService apiService;
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public ExerciseRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.135:8083/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public LiveData<List<Exercise>> getExercisesByProgramId(int programId) {
        MutableLiveData<List<Exercise>> exercisesData = new MutableLiveData<>();

        apiService.getExercisesByProgramId(programId).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Exercise> exercises = new ArrayList<>();
                    for (Map<String, Object> exerciseMap : response.body()) {
                        Exercise exercise = parseExercise(exerciseMap);
                        exercises.add(exercise);
                    }
                    exercisesData.setValue(exercises);
                } else {
                    errorLiveData.setValue("Erreur lors du chargement des exercices");
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                errorLiveData.setValue("Erreur réseau: " + t.getMessage());
            }
        });

        return exercisesData;
    }

    private Exercise parseExercise(Map<String, Object> exerciseMap) {
        Exercise exercise = new Exercise();
        Map<String, Object> exerciseData = (Map<String, Object>) exerciseMap.get("exercice");

        exercise.setId(((Number) exerciseData.get("id")).intValue());
        exercise.setName((String) exerciseData.get("nom"));
        exercise.setDescription((String) exerciseData.get("description"));
        exercise.setBase64Image((String) exerciseMap.get("image"));
        exercise.setBase64Video((String) exerciseMap.get("vedio"));
        String ved = (String) exerciseMap.get("vedio") ;
        if ( ved.length() >= 3) {
            String troisPremieresLettres = ved.substring(0, 3);
            Log.d("vedio request", "parseExercise:  la vedio "+troisPremieresLettres);

        } else {
            Log.d("vedio request", "parseExercise:  la vedio est vide");
        }
        return exercise;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }
}
