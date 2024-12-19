package com.example.proyecto_primer_parcial;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CrudActivity extends AppCompatActivity {



    private Button btnAgregar;
    private EditText etMensaje;

    private MensajeAdapter adaptador;
    private List<Mensaje> mensajes = new ArrayList<>();
    private RecyclerView recyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        etMensaje = findViewById(R.id.etMensaje);
        btnAgregar = findViewById(R.id.btnAgregar);
        mensajes = new ArrayList<>();


        btnAgregar.setOnClickListener(view -> {
            String textoMensaje = etMensaje.getText().toString().trim();  // Usamos trim() para eliminar espacios innecesarios
            Log.d("Mensaje", "Mensaje ingresado: " + textoMensaje);  // Verificar el mensaje ingresado

            if (textoMensaje.isEmpty()) {
                Toast.makeText(CrudActivity.this, "Por favor, ingresa un mensaje", Toast.LENGTH_SHORT).show();
            } else {
                // Crear un nuevo objeto Mensaje
                Mensaje mensaje = new Mensaje(0, textoMensaje);  // Asumiendo que id es generado en el servidor o es autoincremental
                insertarMensaje(mensaje);

                etMensaje.setText("");
            }
        });

        //LOGICA PARA CARGAR LOS DATOS
        recyclerView = findViewById(R.id.recyclerView);
        adaptador = new MensajeAdapter(mensajes, this::mostrarOpcionesMensaje);
        //adaptador = new MensajeAdapter(mensajes, mensaje -> mostrarOpcionesMensaje(mensajes.indexOf(mensaje), mensaje));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);


        cargarMensajes();


    }

    private void insertarMensaje(Mensaje mensaje) {
        if (mensaje == null || mensaje.getTexto().trim().isEmpty()) {
            Toast.makeText(CrudActivity.this, "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.10.36:4000/menssage";

        try {
            JSONObject mensajeJson = new JSONObject();
            mensajeJson.put("TEXTO", mensaje.getTexto());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mensajeJson,
                    response -> {
                        try {
                            String mensajeResponse = response.getString("TEXTO");
                            Toast.makeText(CrudActivity.this, "Mensaje insertado: " + mensajeResponse, Toast.LENGTH_SHORT).show();
                            mensajes.add(mensaje);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CrudActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Toast.makeText(CrudActivity.this, "Error al insertar el mensaje", Toast.LENGTH_SHORT).show();
                    });

            Volley.newRequestQueue(this).add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarMensajes();
    }

    private void cargarMensajes() {
        String url = "http://192.168.10.36:4000/menssage";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        mensajes.clear(); // Limpiar la lista antes de agregar nuevos mensajes
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject mensajeJson = response.getJSONObject(i);
                            int id = mensajeJson.getInt("id"); // Asegúrate de que la clave coincida
                            String texto = mensajeJson.getString("TEXTO"); // Asegúrate de que la clave coincida
                            mensajes.add(new Mensaje(id, texto));
                        }
                        adaptador.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar los datos del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error al cargar mensajes: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        Volley.newRequestQueue(this).add(request);
    }




    private void mostrarOpcionesMensaje(int position, Mensaje mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones del mensaje");
        builder.setItems(new CharSequence[]{"Editar", "Eliminar"}, (dialog, which) -> {
            if (which == 0) {
                // Editar mensaje
                editarMensaje(position, mensaje); // Pasar posición y objeto Mensaje
            } else if (which == 1) {
                // Eliminar mensaje
                eliminarMensaje(position, mensaje); // Pasar posición y objeto Mensaje
            }
        });
        builder.show();
    }




    private void editarMensaje(int position, Mensaje mensaje) {
        // Crear un campo de texto para editar
        EditText input = new EditText(this);
        input.setText(mensaje.getTexto());
        input.setSelection(mensaje.getTexto().length()); // Coloca el cursor al final del texto

        // Crear el diálogo para la edición
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Mensaje");
        builder.setView(input);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoTexto = input.getText().toString().trim();
            if (!nuevoTexto.isEmpty()) {
                mensaje.setTexto(nuevoTexto); // Actualizamos el objeto localmente
                adaptador.notifyItemChanged(position); // Notificamos al adaptador
                actualizarMensajeEnServidor(mensaje); // Actualizamos en el servidor
            } else {
                Toast.makeText(this, "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null); // No hace nada si se cancela
        builder.show();
    }



    private void actualizarMensajeEnServidor(Mensaje mensaje) {
        String url = "http://192.168.10.36:4000/menssage/" + mensaje.getId(); // URL con el ID del mensaje

        try {
            JSONObject mensajeJson = new JSONObject();
            mensajeJson.put("id", mensaje.getId());
            mensajeJson.put("TEXTO", mensaje.getTexto()); // Solo actualizamos el texto

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, mensajeJson,
                    response -> {
                        Toast.makeText(this, "Mensaje actualizado", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Toast.makeText(this, "Error al actualizar el mensaje", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    });

            Volley.newRequestQueue(this).add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void eliminarMensaje(int position, Mensaje mensaje) {
        String url = "http://192.168.10.36:4000/menssage/" + mensaje.getId(); // Construimos la URL con el ID del mensaje

        // Confirmación antes de eliminar
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Mensaje")
                .setMessage("¿Estás seguro de que deseas eliminar este mensaje?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Solicitud DELETE
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                            response -> {
                                mensajes.remove(position); // Eliminamos localmente el mensaje usando la posición
                                adaptador.notifyItemRemoved(position); // Notificamos al adaptador
                                Toast.makeText(this, "Mensaje eliminado correctamente", Toast.LENGTH_SHORT).show();

                            },
                            error -> {
                                Toast.makeText(this, "Error al eliminar el mensaje: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                error.printStackTrace(); // Mostrar más detalles del error
                            });

                    Volley.newRequestQueue(this).add(request); // Agregamos la solicitud a la cola
                })
                .setNegativeButton("No", null) // No hacemos nada si se cancela
                .show();
    }
}