package ma.ensa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ma.ensa.adapters.ExerciseAdapter;
import ma.ensa.utils.ExerciseDataHolder;
import ma.ensa.viewmodels.ExerciseViewModel;

public class ExerciseListActivity extends AppCompatActivity {
    private RecyclerView rvExercises;
    private ExerciseAdapter adapter;
    private ExerciseViewModel viewModel;
    private ProgressBar progressBar;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        int programId = getIntent().getIntExtra("PROGRAM_ID", -1);
        if (programId == -1) {
            finish();
            return;
        }

        initializeViews();
        setupRecyclerView();
        loadExercises(programId);
    }

    private void initializeViews() {
        rvExercises = findViewById(R.id.rvExercises);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        adapter = new ExerciseAdapter(exercise -> {
            // Stocker l'exercice dans le DataHolder
            ExerciseDataHolder.getInstance().setExercise(exercise);

            // Passer uniquement l'ID dans l'Intent
            Intent intent = new Intent(this, ExerciseDetailActivity.class);
            intent.putExtra("EXERCISE_ID", exercise.getId());
            startActivity(intent);
        });

        rvExercises.setAdapter(adapter);
        rvExercises.setLayoutManager(new GridLayoutManager(this, 2));

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Configuration du bouton retour
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }



    private void loadExercises(int programId) {
        viewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        viewModel.getExercisesByProgramId(programId).observe(this, exercises -> {
            if (exercises != null) {
                adapter.setExercises(exercises);
            }
        });
    }
}

