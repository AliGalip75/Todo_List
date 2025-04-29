package com.example.todo_list.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.todo_list.R;
import com.example.todo_list.databinding.FragmentProfileBinding;
import com.example.todo_list.ui.home.HomeViewModel;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ProfileHelper.resetIfNeeded(requireContext());

        profileViewModel.loadData();
        profileViewModel.getCompletedCount().observe(getViewLifecycleOwner(), count -> {
            binding.textCompleted.setText(count + "");
        });
        homeViewModel.getIncompleteTaskCount().observe(getViewLifecycleOwner(), count -> {
            binding.textPending.setText(String.valueOf(count));
        });


        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, ProfileHelper.getCompletedCountForDay(requireContext(), 0))); // Pazartesi
        entries.add(new BarEntry(1, ProfileHelper.getCompletedCountForDay(requireContext(), 1))); // Salı
        entries.add(new BarEntry(2, ProfileHelper.getCompletedCountForDay(requireContext(), 2))); // Çarşamba
        entries.add(new BarEntry(3, ProfileHelper.getCompletedCountForDay(requireContext(), 3))); // Perşembe
        entries.add(new BarEntry(4, ProfileHelper.getCompletedCountForDay(requireContext(), 4))); // Cuma
        entries.add(new BarEntry(5, ProfileHelper.getCompletedCountForDay(requireContext(), 5))); // Cumartesi
        entries.add(new BarEntry(6, ProfileHelper.getCompletedCountForDay(requireContext(), 6))); // Pazar

        BarDataSet dataSet = new BarDataSet(entries, "Completed Tasks");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.on_secondary));
        dataSet.setDrawValues(false); // Bu satırla sütunların üstündeki değerleri kaldırıyoruz

        // Yatayda ve dikeyde etiket konumunu ayarlıyoruz
        dataSet.setValueTextSize(12f); // Etiketin yazı boyutunu ayarlayabilirsin
        dataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.on_background)); // Etiketin rengini ayarla

        // Başlık yazısının pozisyonunu ayarlamak için
        binding.barChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);  // Başlık aşağıda olsun
        binding.barChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT); // Başlık solda
        binding.barChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL); // Başlık yatay olarak yerleştirilsin
        binding.barChart.getLegend().setDrawInside(false); // Başlık grafiğin içinde olmasın

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);
        binding.barChart.setData(barData);

        // Y Ekseni Ayarları
        YAxis leftAxis = binding.barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // En düşük değer 0

        // Maksimum değeri dinamik belirle (8'den büyükse ona göre ayarla)
        float maxValue = barData.getYMax(); // Grafikteki en yüksek değer
        if (maxValue <= 8) {
            leftAxis.setAxisMaximum(8f); // Max 8 ise eksen 8'e kadar gitsin
            leftAxis.setLabelCount(5, true); // 0, 2, 4, 6, 8 göster (5 etiket)
        } else {
            leftAxis.setAxisMaximum(maxValue + 2); // Max değer + 2 (örneğin 10 ise 12'ye kadar çiz)
            leftAxis.setLabelCount(5, true); // 5 eşit aralıklı etiket (0, 3, 6, 9, 12)
        }

        String[] days = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(days.length);

        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.getAxisRight().setEnabled(false);
        binding.barChart.getXAxis().setDrawGridLines(false); // X ekseni grid çizgileri kapalı
        binding.barChart.getAxisLeft().setDrawGridLines(false); // Y ekseni grid çizgileri kapalı
        binding.barChart.animateY(750);
        binding.barChart.setScaleEnabled(false); // Hem yatay hem dikey zoom devre dışı
        binding.barChart.setPinchZoom(false);    // Pinch zoom devre dışı
        binding.barChart.setHighlightPerTapEnabled(false); // Sütunlara dokununca renk değişmesin
        binding.barChart.setHighlightPerDragEnabled(false); // Swipe yaparken de highlight oluşmasın
        binding.barChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}