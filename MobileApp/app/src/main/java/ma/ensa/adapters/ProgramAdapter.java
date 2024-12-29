package ma.ensa.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.R;
import ma.ensa.models.Program;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> {
    private List<Program> programs = new ArrayList<>();
    private List<Program> programsFiltered = new ArrayList<>(); // Liste filtrée
    private final OnProgramClickListener listener;

    public interface OnProgramClickListener {
        void onProgramClick(Program program);
    }

    public ProgramAdapter(OnProgramClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_program, parent, false);
        return new ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder holder, int position) {
        holder.bind(programsFiltered.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return programsFiltered.size();
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
        this.programsFiltered = new ArrayList<>(programs);
        notifyDataSetChanged();
    }

    // Méthode de filtrage
    public void filter(String query) {
        programsFiltered.clear();
        if (query.isEmpty()) {
            programsFiltered.addAll(programs);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (Program program : programs) {
                if (program.getName().toLowerCase().contains(lowerCaseQuery)) {
                    programsFiltered.add(program);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ProgramViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvProgramName;
        private final TextView tvProgramDescription;

        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProgramName = itemView.findViewById(R.id.tvProgramName);
            tvProgramDescription = itemView.findViewById(R.id.tvProgramDescription);
        }

        public void bind(Program program, OnProgramClickListener listener) {
            tvProgramName.setText(program.getName());
            tvProgramDescription.setText(program.getDescription());
            itemView.setOnClickListener(v -> listener.onProgramClick(program));
        }
    }
}