package ma.ensa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import ma.ensa.utils.SessionManager;
import ma.ensa.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private EditText etLogin;
    private EditText etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            startProgramListActivity();
            finish();
        }

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        // Utiliser le ViewModel pour l'authentification
        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.login(login, password).observe(this, result -> {
            progressBar.setVisibility(View.GONE);
            if (result != null && result.getId() > 0) {
                sessionManager.savePatientId(result.getId());
                startProgramListActivity();
                finish();
            } else {
                Toast.makeText(this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startProgramListActivity() {
        Intent intent = new Intent(this, ProgramListActivity.class);
        startActivity(intent);
    }
}
