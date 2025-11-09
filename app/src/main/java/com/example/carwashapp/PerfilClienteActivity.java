package com.example.carwashapp;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PerfilClienteActivity extends AppCompatActivity {

    // Vistas de la sección de Perfil
    private ImageView ivProfilePhoto;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etPais;
    private Button btnGuardarPerfil;

    // Vistas de la sección de Vehículos
    private TextView tvVehicleListPlaceholder;
    private Button btnAgregarVehiculo;
    private List<Vehicle> registeredVehicles; // Lista para guardar los vehículos

    // Vistas de la sección de Notificaciones
    private Switch switchNotificaciones;

    // Vistas de la sección de Historial
    private Button btnHistorialAceite;
    private Button btnHistorialLavados;

    // Clase interna para representar un vehículo
    public static class Vehicle {
        String brand;
        String model;
        int year;
        String oilType;

        public Vehicle(String brand, String model, int year, String oilType) {
            this.brand = brand;
            this.model = model;
            this.year = year;
            this.oilType = oilType;
        }

        @Override
        public String toString() {
            return brand + " " + model + " (" + year + ") - Aceite: " + oilType;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);

        // Inicializar la lista de vehículos (simulando carga de datos)
        registeredVehicles = new ArrayList<>();
        loadMockProfileData(); // Carga datos simulados al inicio

        // 1. Inicializar Views
        // Perfil
        ivProfilePhoto = findViewById(R.id.iv_profile_photo);
        etNombres = findViewById(R.id.et_nombres);
        etApellidos = findViewById(R.id.et_apellidos);
        etPais = findViewById(R.id.et_pais);
        btnGuardarPerfil = findViewById(R.id.btn_guardar_perfil);

        // Vehículos
        tvVehicleListPlaceholder = findViewById(R.id.tv_vehicle_list_placeholder);
        btnAgregarVehiculo = findViewById(R.id.btn_agregar_vehiculo);

        // Notificaciones
        switchNotificaciones = findViewById(R.id.switch_notificaciones);

        // Historial
        btnHistorialAceite = findViewById(R.id.btn_historial_aceite);
        btnHistorialLavados = findViewById(R.id.btn_historial_lavados);

        // 2. Configurar Listeners

        // Listener para Guardar Perfil
        btnGuardarPerfil.setOnClickListener(v -> saveProfileData());

        // Listener para Agregar Vehículo (simula la apertura de una nueva pantalla de registro)
        btnAgregarVehiculo.setOnClickListener(v -> {
            Toast.makeText(this, "Abriendo pantalla para agregar vehículo...", Toast.LENGTH_SHORT).show();
            // En una aplicación real, se usaría un Intent para abrir la actividad de registro de vehículos
            // Intent intent = new Intent(PerfilClienteActivity.this, AgregarVehiculoActivity.class);
            // startActivity(intent);

            // Simulación: agregar un vehículo de prueba
            Vehicle newCar = new Vehicle("Toyota", "Corolla", 2020, "5W-30 Sintético");
            registeredVehicles.add(newCar);
            updateVehicleListDisplay();
            Toast.makeText(this, "Vehículo agregado simuladamente: " + newCar.model, Toast.LENGTH_SHORT).show();
        });

        // Listener para Notificaciones Push (guardar preferencia)
        switchNotificaciones.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ? "ACTIVADAS" : "DESACTIVADAS";
            // En una aplicación real, este estado se guardaría en SharedPreferences o en el servidor
            Toast.makeText(this, "Notificaciones de cotización: " + status, Toast.LENGTH_SHORT).show();
        });

        // Listener para Historial de Cambios de Aceite
        btnHistorialAceite.setOnClickListener(v -> {
            Toast.makeText(this, "Abriendo Historial de Cambios de Aceite...", Toast.LENGTH_SHORT).show();
            // Abrir la actividad de historial de aceite
        });

        // Listener para Historial de Lavados
        btnHistorialLavados.setOnClickListener(v -> {
            Toast.makeText(this, "Abriendo Historial de Lavados...", Toast.LENGTH_SHORT).show();
            // Abrir la actividad de historial de lavados
        });

        // 3. Mostrar la lista inicial de vehículos
        updateVehicleListDisplay();
    }

    /**
     * Simula la carga de datos del perfil desde una fuente (ej. base de datos o SharedPreferences).
     */
    private void loadMockProfileData() {
        // Datos de perfil simulados
        etNombres.setText("Juan");
        etApellidos.setText("Pérez");
        etPais.setText("Honduras");

        // Datos de vehículos simulados
        registeredVehicles.add(new Vehicle("Honda", "CRV", 2018, "10W-30 Mineral"));
        registeredVehicles.add(new Vehicle("Ford", "Ranger", 2022, "5W-40 Sintético"));
    }

    /**
     * Valida y simula el guardado de los datos del perfil.
     */
    private void saveProfileData() {
        String nombres = etNombres.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String pais = etPais.getText().toString().trim();

        if (nombres.isEmpty() || apellidos.isEmpty() || pais.isEmpty()) {
            Toast.makeText(this, "Todos los campos del perfil son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulación: guardar los datos en la fuente de datos
        // Aquí iría la lógica de guardado real (ej. Firestore, API REST)
        String resumen = "Perfil Guardado:\nNombre: " + nombres + "\nApellido: " + apellidos + "\nPaís: " + pais;

        Toast.makeText(this, "Datos de perfil actualizados con éxito.", Toast.LENGTH_LONG).show();
        // Opcional: Log.d("PROFILE_SAVE", resumen);
    }

    /**
     * Actualiza el TextView con la lista de vehículos registrados.
     */
    private void updateVehicleListDisplay() {
        if (registeredVehicles.isEmpty()) {
            tvVehicleListPlaceholder.setText("Aún no tienes vehículos registrados.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Vehicle vehicle : registeredVehicles) {
            sb.append("• ").append(vehicle.toString()).append("\n");
        }
        tvVehicleListPlaceholder.setText(sb.toString().trim());
    }
}
