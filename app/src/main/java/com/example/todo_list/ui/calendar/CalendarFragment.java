package com.example.todo_list.ui.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.todo_list.databinding.FragmentCalendarBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;

    private CalendarViewModel calendarViewModel;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        //fragment ilk çalıştığında bugünü al ve sharedpreferences'a kaydet
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String today = day + "/" + (month + 1) + "/" + year;
        saveSelectedDateToPrefs(today);
    }
    private void saveSelectedDateToPrefs(String date) {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        prefs.edit().putString("selectedDate", date).apply();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        binding.calendarView.setMinDate(System.currentTimeMillis() - 1000);
        
        binding.floatingActionButton.setOnClickListener(v -> {

            TaskBottomSheetFragment bottomSheetFragment = new TaskBottomSheetFragment(calendarViewModel);
            bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());

        });

        // CalendarView'a listener ekledik ve güncel olarak seçili olan günü sharedPreferences'a aktardık
        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // CalendarView'da month 0-indexli (Ocak = 0), bu yüzden +1
                LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);

                // yyyy-MM-dd formatına çevir
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = selectedDate.format(formatter); // Örn: "2025-05-12"

                // SharedPreferences'a kaydet
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectedDate", formattedDate);
                editor.apply();
            }
        });

    }

    @Override
    public void onDestroyView() { // Memory leak'i önlemek için
        super.onDestroyView();
        binding = null;
    }
}