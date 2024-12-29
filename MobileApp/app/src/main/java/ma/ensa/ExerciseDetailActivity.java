package ma.ensa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ma.ensa.models.Exercise;
import ma.ensa.utils.ExerciseDataHolder;

public class ExerciseDetailActivity extends AppCompatActivity {
    private VideoView videoView;
    private ImageView imageView;
    private TextView tvName;
    private TextView tvDescription;
    private FloatingActionButton btnPlay;
    private Exercise exercise;
    private MaterialButton button ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        exercise = ExerciseDataHolder.getInstance().getExercise();
        Log.d("Detail", "Exercice Detaille"+exercise);
        if (exercise == null) {
            Toast.makeText(this, "Erreur: Données de l'exercice non trouvées", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initializeViews();
        setupViews();
    }

    private void initializeViews() {
        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.imageView);
        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        btnPlay = findViewById(R.id.btnPlay);
        button =   findViewById(R.id.btnStartExercise);

    }

    private void setupViews() {
        tvName.setText(exercise.getName());
        tvDescription.setText(exercise.getDescription());

        // Charger l'image depuis base64
        if (exercise.getBase64Image() != null) {
            byte[] decodedImage = Base64.decode(exercise.getBase64Image(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
            imageView.setImageBitmap(bitmap);
        }

        btnPlay.setOnClickListener(v -> playVideo());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("EXERCISE_ID", exercise.getId());
            startActivity(intent);
        });


        // Configurer le bouton retour
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void playVideo() {
        if (exercise.getBase64Video() != null) {
            // Créer un fichier temporaire pour la vidéo
            try {
                File videoFile = File.createTempFile("video", "mp4", getCacheDir());
                byte[] decodedVideo = Base64.decode(exercise.getBase64Video(), Base64.DEFAULT);
                FileOutputStream fos = new FileOutputStream(videoFile);
                fos.write(decodedVideo);
                fos.close();

                // Jouer la vidéo
                videoView.setVideoPath(videoFile.getPath());
                videoView.start();

                // Cacher le bouton play et l'image
                btnPlay.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                Toast.makeText(this, "Erreur lors de la lecture de la vidéo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}