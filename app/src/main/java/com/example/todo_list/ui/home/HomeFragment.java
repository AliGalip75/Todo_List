package com.example.todo_list.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todo_list.R;
import com.example.todo_list.data.Task;
import com.example.todo_list.databinding.FragmentHomeBinding;
import com.example.todo_list.ui.profile.ProfileHelper;
import com.example.todo_list.ui.profile.ProfileViewModel;

import java.time.LocalDate;

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

        EditText editText = dialogView.findViewById(R.id.editTextTaskTitle);
        editText.setText(taskToUpdate.getTitle());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Task");
        builder.setView(dialogView);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String updatedText = editText.getText().toString().trim();
            if (!updatedText.isEmpty()) {
                taskToUpdate.setTitle(updatedText);
                homeViewModel.updateTask(taskToUpdate);
                Toast.makeText(context, "Todo Updated", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_corner);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.primary));  // Tema rengini kullan

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.unselected));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}