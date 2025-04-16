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

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public HomeViewModel homeViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class); //homeViewModel'ı bağla
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.recylerView;
        LinearLayout emptyView = binding.emptyView; // görev kalmayınca görünecek layout
        TextView homeText = binding.homeText; // TODOS text
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // recyclerView'ı dikey olarak ayarladık

        TaskAdapter adapter = new TaskAdapter( //adapter'ın içine silme ve güncelleme fonksiyonlarını gömdük
                task -> homeViewModel.deleteTask(task),
                task -> showUpdateDialog(requireContext(), task, homeViewModel)
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