package com.example.todo_list.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todo_list.R;
import com.example.todo_list.MainActivity;

public class SplashActivity extends AppCompatActivity {
    // Logo kaç milisaniye ekranda kalsın?
    private static final long SPLASH_DELAY = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Başlık çubuğunu gizle
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_splash);

        ImageView ivLogo = findViewById(R.id.ivLogo);
        // Animasyonu yükle
        Animation splashAnim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        ivLogo.startAnimation(splashAnim);

        // Belirlenen süre sonra MainActivity'e geç
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DELAY);
    }
}
