package ma.ensa.adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.ensa.R;
import ma.ensa.models.Exercise;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises = new ArrayList<>();
    private final OnExerciseClickListener listener;

    public interface OnExerciseClickListener {
        void onExerciseClick(Exercise exercise);
    }

    public ExerciseAdapter(OnExerciseClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.bind(exercises.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivExercise;
        private final TextView tvExerciseName;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            ivExercise = itemView.findViewById(R.id.ivExercise);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
        }

        public void bind(Exercise exercise, OnExerciseClickListener listener) {
            tvExerciseName.setText(exercise.getName());

            // Charger l'image depuis base64
            if (exercise.getBase64Image() != null) {
                byte[] decodedImage = Base64.decode(exercise.getBase64Image(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
                ivExercise.setImageBitmap(bitmap);
            }

            itemView.setOnClickListener(v -> listener.onExerciseClick(exercise));
        }
    }
}
