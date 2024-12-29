package ma.ensa.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ma.ensa.config.ApiService;
import ma.ensa.models.Exercise;
import ma.ensa.models.Program;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProgramRepository {
    private static final String BASE_URL = "http://192.168.1.135:8083/";
    private ApiService apiService;
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public ProgramRepository() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public LiveData<List<Program>> getProgramsByPatientId(int patientId) {
        MutableLiveData<List<Program>> programsData = new MutableLiveData<>();

        apiService.getProgramsByPatientId(patientId).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Program> programs = new ArrayList<>();
                    for (Map<String, Object> programMap : response.body()) {
                        Program program = parseProgram(programMap);
                        programs.add(program);
                    }
                    programsData.setValue(programs);
                } else {
                    errorLiveData.setValue("Erreur lors du chargement des programmes");
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                errorLiveData.setValue("Erreur réseau: " + t.getMessage());
            }
        });

        return programsData;
    }

    private Program parseProgram(Map<String, Object> programMap) {
        Program program = new Program();
        Map<String, Object> programData = (Map<String, Object>) programMap.get("program");

        program.setId(((Number) programData.get("id")).intValue());
        program.setName((String) programData.get("name"));
        program.setDescription((String) programData.get("description"));

        List<Exercise> exercises = new ArrayList<>();
        List<Map<String, Object>> exercisesData = (List<Map<String, Object>>) programMap.get("exercises");

        if (exercisesData != null) {
            for (Map<String, Object> exerciseMap : exercisesData) {
                Exercise exercise = parseExercise(exerciseMap);
                exercises.add(exercise);
            }
        }

        program.setExercises(exercises);
        return program;
    }

    private Exercise parseExercise(Map<String, Object> exerciseMap) {
        Exercise exercise = new Exercise();
        Map<String, Object> exerciseData = (Map<String, Object>) exerciseMap.get("exercice");

        exercise.setId(((Number) exerciseData.get("id")).intValue());
        exercise.setName((String) exerciseData.get("name"));
        exercise.setDescription((String) exerciseData.get("description"));
        exercise.setBase64Image((String) exerciseMap.get("image"));
        exercise.setBase64Video((String) exerciseMap.get("vedio"));

        return exercise;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }
}
