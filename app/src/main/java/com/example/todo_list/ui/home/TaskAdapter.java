package com.example.todo_list.ui.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_list.R;
import com.example.todo_list.data.Task;
import com.example.todo_list.databinding.ItemTaskBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    //recyclerview'ın göstereceği veriler burada tutulur
    private List<Task> tasks = new ArrayList<>();


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

        holder.binding.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.binding.getRoot().getContext(), "seçilen card: " + current.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }


}

