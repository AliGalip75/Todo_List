package com.example.todo_list;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.todo_list.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = binding.navView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //Navbar geçişlerini otomatik yapmasın, biz yönetelim
        //NavigationUI.setupWithNavController(binding.navView, navController);

        navView.setOnItemSelectedListener(item -> {
            int currentId = navController.getCurrentDestination().getId();
            int selectedId = item.getItemId();

            NavOptions navOptions = new NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.slide_out_left)
                    .setPopEnterAnim(R.anim.slide_in_left)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build();

            if (currentId == R.id.navigation_home && selectedId == R.id.navigation_calendar) {
                navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .build();
            } else if (currentId == R.id.navigation_calendar && selectedId == R.id.navigation_home) {
                navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_left)
                        .setExitAnim(R.anim.slide_out_right)
                        .build();
            }else if (currentId == R.id.navigation_home && selectedId == R.id.navigation_profile) {
                navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .build();
            }else if (currentId == R.id.navigation_profile && selectedId == R.id.navigation_home) {
                navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_left)
                        .setExitAnim(R.anim.slide_out_right)
                        .build();
            }else if (currentId == R.id.navigation_calendar && selectedId == R.id.navigation_profile) {
                navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .build();
            }else if (currentId == R.id.navigation_profile && selectedId == R.id.navigation_calendar) {
                navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_left)
                        .setExitAnim(R.anim.slide_out_right)
                        .build();
            }

            int itemId = item.getItemId();
            if (navController.getCurrentDestination().getId() != itemId) {
                navController.navigate(itemId, null, navOptions);
            }
            return true;
        });

    }

}