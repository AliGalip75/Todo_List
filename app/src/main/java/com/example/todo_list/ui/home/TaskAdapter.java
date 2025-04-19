package com.example.todo_list.ui.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todo_list.data.Task;
import com.example.todo_list.databinding.ItemTaskBinding;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasks = new ArrayList<>();  // Recyclerview'ın göstereceği veriler burada tutulur

    public interface OnTaskDeleteClickListener { // Delete işlemi için kendi listener interface'imiz
        void onTaskDelete(Task task);
    }

    public interface OnTaskEditClickListener { // Update işlemi için kendi listener interface'imiz
        void onTaskEdit(Task task);
    }

    public interface OnTaskCheckedChangeListener {
        void onTaskCheckChanged(Task task, boolean isChecked);
    }

    private final OnTaskDeleteClickListener deleteListener;

    private final OnTaskEditClickListener editListener;

    private final OnTaskCheckedChangeListener checkboxListener;

    public TaskAdapter(OnTaskDeleteClickListener deleteListener, OnTaskEditClickListener editListener, OnTaskCheckedChangeListener checkboxListener) {
        this.deleteListener = deleteListener;
        this.editListener = editListener;
        this.checkboxListener = checkboxListener;
    }

    // Viewholder tek bir satırı temsil ediyor
    public static class TaskHolder extends RecyclerView.ViewHolder {
        private final ItemTaskBinding binding;

        public TaskHolder(ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskHolder(binding);
    }

    // getItemCount() kadar çalışacak
    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task current = tasks.get(position);
        holder.binding.textViewTitle.setText(current.getTitle());
        holder.binding.textViewDate.setText(current.getDate());

        holder.binding.isDone.setOnCheckedChangeListener(null);
        holder.binding.isDone.setChecked(current.getDone());
        holder.binding.isDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkboxListener.onTaskCheckChanged(current, isChecked);
        });

        holder.binding.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.binding.getRoot().getContext(), "seçilen card: " + current.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });

        // RecyclerView'daki todoların silme butonu için listener
        holder.binding.garbageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onTaskDelete(current);
            }
        });

        // RecyclerView'daki todoların update butonu için listener
        holder.binding.editBtn.setOnClickListener(v -> {
            editListener.onTaskEdit(current);
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged(); // liste değişince UI'ı güncelle
    }

}

