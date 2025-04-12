package com.example.todo_list.ui.calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todo_list.R;
import com.example.todo_list.data.Task;
import com.example.todo_list.databinding.BottomSheetLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TaskBottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetLayoutBinding binding;
    private CalendarViewModel calendarViewModel;


    public TaskBottomSheetFragment(CalendarViewModel calendarViewModel){
        this.calendarViewModel = calendarViewModel;
    }

    // Fragment oluşturulurken binding'e ihtiyacımız yok, çünkü ViewBinding otomatik olarak bağlanacak.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // ViewBinding kullanarak layout'u bağla
        binding = BottomSheetLayoutBinding.inflate(inflater, container, false);

        // Kaydet butonuna tıklanabilirlik
        binding.addTaskButton.setOnClickListener(v -> {
            String selectedDate = "2025-04-09";
            String title = binding.newTaskEdit.getText().toString().trim();
            if (!title.isEmpty()) {
                Task task = new Task(title, selectedDate);
                calendarViewModel.insert(task);
                Toast.makeText(getContext(), "Görev eklendi", Toast.LENGTH_SHORT).show();
                dismiss();  // BottomSheet'i kapat
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Fragment view'u yok olunca binding'i null yap
        binding = null;
    }
}
