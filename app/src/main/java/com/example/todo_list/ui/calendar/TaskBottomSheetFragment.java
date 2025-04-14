package com.example.todo_list.ui.calendar;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import com.example.todo_list.data.Task;
import com.example.todo_list.databinding.BottomSheetLayoutBinding;
import com.example.todo_list.ui.home.HomeViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

public class TaskBottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetLayoutBinding binding;
    private CalendarViewModel calendarViewModel;

    public TaskBottomSheetFragment(CalendarViewModel calendarViewModel){
        this.calendarViewModel = calendarViewModel;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //todoo ekleme işleminde edittext ile klavye arasına biraz boşluk ekledik
    @Override
    public void onStart() {
        super.onStart();

        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (dialog != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            View rootView = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (rootView != null) {
                // Window insets listener
                ViewCompat.setOnApplyWindowInsetsListener(rootView, new OnApplyWindowInsetsListener() {
                    @NonNull
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                        int bottomInset = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
                        v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bottomInset);
                        return insets;
                    }
                });
            }
        }
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstantState){
        super.onViewCreated(view, savedInstantState);

        // Kaydet butonuna tıklanabilirlik
        binding.addTaskButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String selectedDate = sharedPreferences.getString("selectedDate", "");
            String title = binding.newTaskEdit.getText().toString().trim();
            if (!title.isEmpty()) {
                Task task = new Task(title, selectedDate, false);
                calendarViewModel.insert(task);
                Toast.makeText(getContext(), "Todo added", Toast.LENGTH_SHORT).show();
                dismiss();  // BottomSheet'i kapat
            }
        });
    }

    // todoo ekleme işleminde layout'a radius ekledik
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;

            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                MaterialShapeDrawable background = new MaterialShapeDrawable();
                background.setShapeAppearanceModel(
                        ShapeAppearanceModel.builder()
                                .setTopLeftCorner(CornerFamily.ROUNDED, 60)
                                .setTopRightCorner(CornerFamily.ROUNDED, 60)
                                .build()
                );

                background.setFillColor(ColorStateList.valueOf(Color.WHITE));
                bottomSheet.setBackground(background);
            }
        });

        return dialog;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Fragment view'u yok olunca binding'i null yap
        binding = null;
    }
}
