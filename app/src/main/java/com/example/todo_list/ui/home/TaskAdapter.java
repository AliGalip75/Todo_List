package com.example.todo_list.ui.home;

import static android.webkit.WebSettings.RenderPriority.HIGH;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_list.R;
import com.example.todo_list.data.Task;
import com.example.todo_list.databinding.ItemTaskBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasks = new ArrayList<>();  // Recyclerview'ın göstereceği veriler burada tutulur

    private final OnTaskDeleteClickListener deleteListener;

    private final OnTaskEditClickListener editListener;

    private final OnTaskCheckedChangeListener checkboxListener;

    public interface OnTaskDeleteClickListener { // Delete işlemi için kendi listener interface'imiz
        void onTaskDelete(Task task);
    }

    public interface OnTaskEditClickListener { // Update işlemi için kendi listener interface'imiz
        void onTaskEdit(Task task);
    }

    public interface OnTaskCheckedChangeListener {
        void onTaskCheckChanged(Task task, boolean isChecked);
    }

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

        // RecyclerView'daki todoların silme butonu için listener
        holder.binding.garbageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onTaskDelete(current);
                Snackbar snackbar = Snackbar.make(holder.binding.getRoot(), "Todo Deleted", Snackbar.LENGTH_SHORT);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.snackbar));

                snackbar.setTextColor(Color.WHITE);
                TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextSize(20); // sp cinsinden
                textView.setTypeface(ResourcesCompat.getFont(holder.binding.getRoot().getContext(), R.font.quicksand_variable_font_wght));

                // Yazı kadar genişlik ve ortalama için:
                ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
                if (params instanceof FrameLayout.LayoutParams) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) params;

                    // Konum: alt-orta
                    layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

                    // Genişliği WRAP_CONTENT yap
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;

                    snackbarView.setLayoutParams(layoutParams);
                }

                // Kenarlardan biraz boşluk olsun diye padding ver
                snackbarView.setPadding(24, 16, 24, 16);

                // İsteğe bağlı: köşe yumuşatma
                snackbarView.setBackground(
                        ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.bg_snackbar_rounded)
                );

                snackbar.show();
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Animation clickAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.card_click);
            v.startAnimation(clickAnim);
        });


        // RecyclerView'daki todoların update butonu için listener
        holder.binding.editBtn.setOnClickListener(v -> {
            editListener.onTaskEdit(current);
        });

        Chip chip = holder.binding.chipPriority;

        switch (current.getPriority()) {
            case High:
                chip.setText("HIGH");
                chip.setChipBackgroundColorResource(R.color.high); // tanımlıysa
                break;
            case Medium:
                chip.setText("MEDIUM");
                chip.setChipBackgroundColorResource(R.color.medium);
                break;
            case Low:
                chip.setText("LOW");
                chip.setChipBackgroundColorResource(R.color.low);
                break;
        }
        // Dik şeridin arka plan rengini ayarla
        holder.binding.textViewCategory.setText(current.getCategory());

        MaterialCardView card = holder.itemView.findViewById(R.id.card);

        switch (current.getPriority()) {
            case High:
                card.setStrokeColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.high));
                break;
            case Medium:
                card.setStrokeColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.medium));
                break;
            case Low:
                card.setStrokeColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.low));
                break;
        }


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

    public List<Task> getTasks() {
        return this.tasks;
    }

}

