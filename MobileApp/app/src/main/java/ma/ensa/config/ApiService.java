package ma.ensa.config;

import java.util.List;
import java.util.Map;

import ma.ensa.models.LoginRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/api/patients/login")
    Call<ResponseBody> login(@Body LoginRequest loginRequest);

    @GET("/api/programs/patient/{patientId}")
    Call<List<Map<String, Object>>> getProgramsByPatientId(@Path("patientId") int patientId);

    @GET("/api/exercices/program/{programId}")
    Call<List<Map<String, Object>>> getExercisesByProgramId(@Path("programId") int programId);
}