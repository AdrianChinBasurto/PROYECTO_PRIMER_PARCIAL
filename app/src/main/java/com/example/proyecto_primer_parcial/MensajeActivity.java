package com.example.proyecto_primer_parcial;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MensajeActivity extends AppCompatActivity {

    private EditText etNombre;
    private Button btnMostrarMensaje;
    private TextView tvMensaje;
    private List<String> mensajes; // Lista para almacenar los mensajes



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre);
        btnMostrarMensaje = findViewById(R.id.btnMostrarMensaje);
        tvMensaje = findViewById(R.id.tvMensaje);

        // Configurar el evento de clic en el botón
        btnMostrarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarMensaje();
            }
        });

        mensajes = new ArrayList<>();
        consumirApi();
    }

    private void consumirApi() {
        String url = "http://192.168.10.36:4000/menssage";

        // Crear una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Crear una solicitud de tipo JsonArrayRequest
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Iterar sobre la respuesta JSON
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject mensajeObj = response.getJSONObject(i);
                                String texto = mensajeObj.getString("TEXTO");

                                // Agregar el texto a la lista
                                mensajes.add(texto);
                            }

                            // Mostrar los datos cargados en el log (o puedes actualizarlos en la UI)
                            for (String mensaje : mensajes) {
                                Log.d("Mensaje", mensaje);
                            }

                            Toast.makeText(MensajeActivity.this, "Datos cargados exitosamente", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("Error", "Error procesando el JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Error en la solicitud: " + error.getMessage());
                        Toast.makeText(MensajeActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Agregar la solicitud a la cola
        requestQueue.add(jsonArrayRequest);
    }


    private void mostrarMensaje() {
        // Obtener el nombre ingresado
        String nombre = etNombre.getText().toString().trim();

        // Verificar que el campo no esté vacío
        if (nombre.isEmpty()) {
            tvMensaje.setText("Por favor, ingresa tu nombre.");
            return;
        }

        // Verificar que la lista de mensajes no esté vacía
        if (mensajes.isEmpty()) {
            tvMensaje.setText("No hay mensajes disponibles. Intenta nuevamente más tarde.");
            return;
        }

        // Obtener un mensaje random de la lista cargada
        String mensajeRandom = mensajes.get(new Random().nextInt(mensajes.size()));

        // Concatenar el nombre con el mensaje
        String mensajeFinal = "Hola, " + nombre + ". " + mensajeRandom;

        // Mostrar el mensaje en el TextView
        tvMensaje.setText(mensajeFinal);
    }

}