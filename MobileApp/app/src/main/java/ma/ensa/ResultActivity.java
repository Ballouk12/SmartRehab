package ma.ensa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private MaterialButton backButton ;
    private TextView felText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultText = findViewById(R.id.resultText);
        felText = findViewById(R.id.felText);
        // Use getSerializableExtra and cast it to ArrayList<Double>
        ArrayList<Double> cosineValues = (ArrayList<Double>) getIntent().getSerializableExtra("cosineValues");
        Log.e("messi", "list: " + cosineValues);
        backButton =   findViewById(R.id.btnReturn);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExerciseListActivity.class);
            startActivity(intent);
        });
        // Now you can process the values as needed
        double mean = calculateMean(cosineValues);
        if(mean * 100<98) {
            resultText.setText("Not good enough, you can do much bette!");
            felText.setText("It's a start!");
        } else if (mean * 100<99) {
            resultText.setText("Not bad, but it could be better!");
            felText.setText("Nice try!");
        } else {
            resultText.setText("Great job, keep it up!");
            felText.setText("Congratulations!");
        }
    }

    private double calculateMean(ArrayList<Double> values) {
        if (values == null || values.isEmpty()) return 0.0;
        double sum = 0;
        for (Double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

}