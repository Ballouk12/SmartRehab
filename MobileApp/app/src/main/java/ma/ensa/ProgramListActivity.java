package ma.ensa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ma.ensa.adapters.ProgramAdapter;
import ma.ensa.utils.SessionManager;
import ma.ensa.viewmodels.ProgramViewModel;

public class ProgramListActivity extends AppCompatActivity {
    private RecyclerView rvPrograms;
    private ProgramAdapter adapter;
    private ProgramViewModel viewModel;
    private ProgressBar progressBar;
    private SessionManager sessionManager;
    private SearchView searchView;
    private ImageButton logoutButton; // Enlever l'initialisation ici
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_list);

        sessionManager = new SessionManager(this);
        initializeViews();
        setupRecyclerView();
        loadPrograms();
    }

    private void initializeViews() {
        rvPrograms = findViewById(R.id.rvPrograms);
        progressBar = findViewById(R.id.progressBar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = findViewById(R.id.searchView);
        logoutButton = findViewById(R.id.logoutButton); // Ajouter ici

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Centrer l'icône home
        bottomNavigationView.setItemIconSize(getResources().getDimensionPixelSize(R.dimen.bottom_nav_icon_size));

        // Gérer le clic sur l'icône home
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                // Rafraîchir la page
                loadPrograms();
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        adapter = new ProgramAdapter(program -> {
            Intent intent = new Intent(this, ExerciseListActivity.class);
            intent.putExtra("PROGRAM_ID", program.getId());
            startActivity(intent);
        });

        rvPrograms.setAdapter(adapter);
        rvPrograms.setLayoutManager(new LinearLayoutManager(this));

        // Déplacer la configuration de searchView ici car elle est déjà initialisée
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        logoutButton.setOnClickListener(v -> performLogout());
    }

    private void performLogout() {
        // Effacer les données de session
        sessionManager.logout();

        // Rediriger vers la page de connexion
        startLoginActivity();

        // Fermer l'activité actuelle
        finish();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        // Effacer la pile d'activités pour empêcher le retour arrière
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void loadPrograms() {
        viewModel = new ViewModelProvider(this).get(ProgramViewModel.class);

        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getProgramsByPatientId(sessionManager.getPatientId())
                .observe(this, programs -> {
                    if (programs != null) {
                        adapter.setPrograms(programs);
                    }
                });
    }
}