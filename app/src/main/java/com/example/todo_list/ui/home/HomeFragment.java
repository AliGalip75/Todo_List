package com.example.todo_list.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_list.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public Button buton;
    public HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        recyclerView = binding.recylerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);
        homeViewModel.getAllTasks().observe(getViewLifecycleOwner(), adapter::setTasks);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState){
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}