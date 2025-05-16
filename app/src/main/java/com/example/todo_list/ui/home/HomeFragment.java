package com.example.todo_list.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todo_list.R;
import com.example.todo_list.data.Task;
import com.example.todo_list.databinding.FragmentHomeBinding;
import com.example.todo_list.ui.profile.ProfileHelper;
import com.example.todo_list.ui.profile.ProfileViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.example.todo_list.data.Priority;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public HomeViewModel homeViewModel;
    public ProfileViewModel profileViewModel;

    public HomeFragment() {}

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class); //homeViewModel'ı bağla
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.recylerView;
        LinearLayout emptyView = binding.emptyView; // görev kalmayınca görünecek layout
        TextView homeText = binding.homeText; // TODOS text
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // recyclerView'ı dikey olarak ayarladık

        // Haftanın gününü al (0=Pazartesi, 6=Pazar)
        int currentDayOfWeek = LocalDate.now().getDayOfWeek().getValue() - 1; // Java'da Pazartesi=1, Pazar=7
        if (currentDayOfWeek < 0) currentDayOfWeek = 6; // Pazar için düzeltme
        int finalCurrentDayOfWeek = currentDayOfWeek;

        TaskAdapter adapter = new TaskAdapter(
                task -> {
                    if (!task.getDone()) {
                        profileViewModel.incrementDay(requireContext(), finalCurrentDayOfWeek); // silinen task tiksiz ise hem o gün için tamamlanan görevsayısını
                    }                                                                           // hem de haftalık toplam tamamlann görev sayısını bir artır.
                    homeViewModel.deleteTask(task);
                },

                task -> showUpdateDialog(requireContext(), task, homeViewModel),

                (task, isChecked) -> {
                    task.setDone(isChecked);
                    homeViewModel.updateTask(task);

                    if (isChecked) {
                        profileViewModel.incrementDay(requireContext(), finalCurrentDayOfWeek);
                    } else {
                        int dayCount = ProfileHelper.getCompletedCountForDay(requireContext(), finalCurrentDayOfWeek);
                        if (dayCount > 0) {
                            ProfileHelper.setCompletedCountForDay(requireContext(), finalCurrentDayOfWeek, dayCount - 1);
                            ProfileHelper.setCompletedCount(requireContext(), ProfileHelper.getCompletedCount(requireContext()) - 1);
                        }
                        profileViewModel.loadData();
                    }
                }
        );

        recyclerView.setAdapter(adapter);
        // todoListesini dinler ve değişiklik olursa dinamik olarak sürekli çalışır. Okun solundaki "tasks" değişen verinin son halini tutar
        homeViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            adapter.setTasks(tasks);

            if (tasks.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                homeText.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                homeText.setVisibility(View.VISIBLE);
            }
        });

        // SÜRÜKLEMEYİ AKTİF ET
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                List<Task> currentTasks = adapter.getTasks();
                Collections.swap(currentTasks, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Kaydırma yok
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState){
        binding = FragmentHomeBinding.inflate(inflater, container, false); // Binding'i bağla
        return binding.getRoot();
    }

    // Update işlemi için metod
    private void showUpdateDialog(Context context, Task taskToUpdate, HomeViewModel homeViewModel) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_update_task, null);

        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        Spinner spinnerPriority = dialogView.findViewById(R.id.spinnerPriority);
        EditText editTextDate = dialogView.findViewById(R.id.editTextDate);
        EditText editText = dialogView.findViewById(R.id.editTextTaskTitle);
        editText.setText(taskToUpdate.getTitle());

        // ===== Builder Setup =====
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Task");
        builder.setView(dialogView);
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // ===== Dialog Setup =====
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_corner);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.on_background));  // Tema rengini kullan
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.gray));

        // Save butonunun davranışı
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String updatedText = editText.getText().toString().trim();
            updatedText = updatedText.substring(0,1).toUpperCase() + updatedText.substring(1);
            String updatedDate = editTextDate.getText().toString().trim();
            Priority updatedPriority = (Priority) spinnerPriority.getSelectedItem();
            String updatedCategory = spinnerCategory.getSelectedItem().toString();

            if (updatedText.isEmpty()) {
                editText.setError("This field is required");
                return;
            }

            if (updatedDate.isEmpty()) {
                editTextDate.setError("This field is required");
                return;
            }

            taskToUpdate.setTitle(updatedText);
            taskToUpdate.setDate(updatedDate);
            taskToUpdate.setPriority(updatedPriority);
            taskToUpdate.setCategory(updatedCategory);

            homeViewModel.updateTask(taskToUpdate);

            Snackbar snackbar = Snackbar.make(binding.getRoot(), "Todo Updated", Snackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            snackbar.setTextColor(Color.WHITE);

            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextSize(20);
            textView.setTypeface(ResourcesCompat.getFont(context, R.font.quicksand_variable_font_wght));

            ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
            if (params instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) params;
                layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                snackbarView.setLayoutParams(layoutParams);
            }

            snackbarView.setPadding(24, 16, 24, 16);
            snackbarView.setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.bg_snackbar_rounded));
            snackbar.show();

            dialog.dismiss(); // Tüm işlemler bittiyse dialog'u kapat
        });


        // ===== Takvim seçimi =====
        Calendar calendar = Calendar.getInstance();

        editTextDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDay) -> {
                // Tarihi yazıya çevir
                @SuppressLint("DefaultLocale")
                String formattedDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                editTextDate.setText(formattedDate);
            }, year, month, day);

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        // ===== Priority ve Category seçimi =====


        // Priority Spinner (Enum kullanıyorsan)
        ArrayAdapter<Priority> priorityAdapter = new ArrayAdapter<>(context, R.layout.spinner_item_padding, Priority.values());
        spinnerPriority.setAdapter(priorityAdapter);
        spinnerPriority.setSelection(priorityAdapter.getPosition(taskToUpdate.getPriority()));

        // Category Spinner
        String[] categories = {"General", "Work", "Personal"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(context, R.layout.spinner_item_padding, categories);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setSelection(categoryAdapter.getPosition(taskToUpdate.getCategory()));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}