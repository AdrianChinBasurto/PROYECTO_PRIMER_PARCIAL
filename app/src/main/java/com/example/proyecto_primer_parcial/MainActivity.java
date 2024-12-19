package com.example.proyecto_primer_parcial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button btnMensaje;
    private Button btnCrud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnMensaje = findViewById(R.id.btnMensaje);
        btnCrud = findViewById(R.id.btnCrud);

        btnMensaje.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MensajeActivity.class);
            startActivity(intent);
        });

        btnCrud.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrudActivity.class);
            startActivity(intent);
  });
    }


}