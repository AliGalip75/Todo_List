package com.example.todo_list.ui.calendar;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import com.example.todo_list.R;
import com.example.todo_list.data.Priority;
import com.example.todo_list.data.Task;
import com.example.todo_list.databinding.BottomSheetLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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


        // Spinner’lar
        Spinner prioSp = binding.spinnerPriority;
        Spinner catSp  = binding.spinnerCategory;

        // Öncelik enum’larını göster
        prioSp.setAdapter(new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item_padding,
                Priority.values()
        ));

        // Kategori listesini belirle
        String[] categories = {"General", "Work", "Personal"};
        catSp.setAdapter(new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item_padding,
                categories
        ));


        // Kaydet butonuna tıklanabilirlik
        binding.addTaskButton.setOnClickListener(v -> {
            String title = binding.newTaskEdit.getText().toString().trim();
            if (title.isEmpty()) {
                binding.newTaskEdit.setError("This field is required");
                return;
            }
            title = title.substring(0, 1).toUpperCase() + title.substring(1);

            // Tarih bilgisini al
            SharedPreferences sp = requireContext()
                    .getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String selectedDate = sp.getString("selectedDate", "");

            // Eğer tarih boşsa veya eski formatta ise düzelt
            if (selectedDate.isEmpty() || selectedDate.contains("/")) {
                try {
                    if (!selectedDate.isEmpty()) {
                        // eski formatı dönüştür
                        DateTimeFormatter oldFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                        LocalDate date = LocalDate.parse(selectedDate, oldFormatter);
                        selectedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } else {
                        // hiç tarih seçilmemişse bugünün tarihi
                        selectedDate = LocalDate.now().toString();
                    }

                    // Güncel değeri SharedPreferences'a tekrar yaz
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("selectedDate", selectedDate);
                    editor.apply();
                } catch (Exception e) {
                    selectedDate = LocalDate.now().toString(); // hata varsa yine bugünün tarihi
                }
            }

            // Spinner’dan seçimi al
            Priority selPrio = (Priority) prioSp.getSelectedItem();
            String selCat   = (String) catSp.getSelectedItem();

            // Önce başlık–tarih–durum ile obj oluştur
            Task task = new Task(title, selectedDate, false, selPrio, selCat);

            // Sonra ekle
            calendarViewModel.insert(task);


            // ===== SnackBar =====
            Snackbar snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content), "Todo Added", Snackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            snackbar.setTextColor(Color.WHITE);

            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextSize(20);
            textView.setTypeface(ResourcesCompat.getFont(binding.getRoot().getContext(), R.font.quicksand_variable_font_wght));

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
                    ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.bg_snackbar_rounded)
            );

            snackbar.show();
            dismiss();
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
