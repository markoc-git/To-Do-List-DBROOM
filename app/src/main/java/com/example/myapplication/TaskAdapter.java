package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    MainActivityViewModel viewModel;

    public TaskAdapter(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
        // ...
    }

    private List<Task> tasks = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        private final TextView descriptionText;
        private final TextView titleTextView;
        private final TextView dateTextView;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            descriptionText = itemView.findViewById(R.id.descText);
            ImageButton deleteImageView = itemView.findViewById(R.id.deleteButton);
            ImageButton editButton = itemView.findViewById(R.id.editButton);

            deleteImageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = tasks.get(position);
                    if (position < 0) {
                        Toast.makeText(itemView.getContext(), "Invalid position", Toast.LENGTH_SHORT).show();
                    } else {
                        viewModel.deleteTask(task);
                    }
                }
            });

            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = tasks.get(position);
                    if (task != null) {
                        String taskId = String.valueOf(task.getId());

                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                        View dialogView = inflater.inflate(R.layout.activity_edit_task, null);
                        builder.setView(dialogView);
                        AlertDialog dialog = builder.create();

                        EditText editTitleEditText = dialogView.findViewById(R.id.editTitleEditText);
                        EditText editDescriptionEditText = dialogView.findViewById(R.id.editDescriptionEditText);
                        Button updateButton = dialogView.findViewById(R.id.updateButton);

                        editTitleEditText.setText(task.getTitle());
                        editDescriptionEditText.setText(task.getDescription());

                        updateButton.setOnClickListener(updateView -> {
                            String updatedTitle = editTitleEditText.getText().toString().trim();
                            String updatedDescription = editDescriptionEditText.getText().toString().trim();

                            if (updatedTitle.isEmpty()) {
                                Toast.makeText(itemView.getContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Task updatedTask = new Task(taskId, updatedTitle, updatedDescription);
                            viewModel.updateTask(updatedTask);
                            updateTask(updatedTask);
                            dialog.dismiss();
                        });

                        dialog.show();
                    } else {
                        Toast.makeText(itemView.getContext(), "Invalid task", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        void bind(Task task) {
            titleTextView.setText(task.getTitle());
            dateTextView.setText(task.getDate());
            descriptionText.setText(task.getDescription());
        }
    }

    // Dodali smo updateTask metodu
    private void updateTask(Task task) {
        new Thread(() -> viewModel.updateTask(task)).start();

    }
}
