package com.example.todo_list.ui.calendar;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todo_list.R;
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
            /*
            String title = binding.newTextTask.getText().toString().trim();
            String selectedDate = "2025-04-09"; // seçilen tarih (takvim eklersin sonra)

            if (!title.isEmpty()) {
                Task task = new Task(title, selectedDate);
                calendarViewModel.insert(task);
                Toast.makeText(getContext(), "Görev eklendi", Toast.LENGTH_SHORT).show();
            }

             */

            TaskBottomSheetFragment bottomSheetFragment = new TaskBottomSheetFragment(calendarViewModel);
            bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}