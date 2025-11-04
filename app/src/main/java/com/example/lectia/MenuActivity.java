package com.example.lectia;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lectia.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button btnLibros = findViewById(R.id.btnLibros);

        btnLibros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Código a ejecutar al hacer clic
                Intent intent = new Intent(MenuActivity.this, ListlibrosActivity.class);

                // 5. Inicia la nueva actividad usando el Intent.
                startActivity(intent);
            };
        });
    }

}
