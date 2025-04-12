package com.example.todo_list.ui.calendar;

import static androidx.core.content.ContextCompat.getSystemService;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todo_list.data.Task;
import com.example.todo_list.databinding.FragmentCalendarBinding;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;

    private CalendarViewModel calendarViewModel;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        
        binding.floatingActionButton.setOnClickListener(v -> {

            showInputLayout();
            
        });

    }

    private void showInputLayout() {
        // Input layout'u görünür yap
        binding.inputLayout.setVisibility(View.VISIBLE);
        // EditText'e focus ver
        binding.newTaskEdit.requestFocus();

        // Klavyeyi aç
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(binding.newTaskEdit, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void addTodoItem() {
        String title = binding.newTaskEdit.getText().toString().trim();
        String selectedDate = "2025-04-09";
        if (!title.isEmpty()) {
            // Listeye yeni öğe ekle
            Task task = new Task(title, selectedDate);
            calendarViewModel.insert(task);
            Toast.makeText(getContext(), "Görev eklendi", Toast.LENGTH_SHORT).show();

            // Input alanını temizle ve gizle
            binding.newTaskEdit.setText("");
            binding.inputLayout.setVisibility(View.GONE);

            // Klavyeyi kapat
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(binding.newTaskEdit.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}