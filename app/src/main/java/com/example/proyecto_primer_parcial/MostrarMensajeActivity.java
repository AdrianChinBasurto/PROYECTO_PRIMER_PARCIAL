package com.example.proyecto_primer_parcial;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MostrarMensajeActivity extends AppCompatActivity {

    private TextView tvMensajeFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        tvMensajeFinal = findViewById(R.id.tvMensajeFinal);

        String nombre = getIntent().getStringExtra("nombre");
        String mensaje = getIntent().getStringExtra("mensaje");

        tvMensajeFinal.setText("Hola "+nombre+ "! "+mensaje);
    }
}