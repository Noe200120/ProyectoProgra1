package com.example.carwashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class SolicitarCotizacionActivity extends AppCompatActivity {

    // Views de la interfaz de usuario
    private EditText etAutoModelo;
    private Spinner spinnerTipoServicio;
    private RadioGroup rgUbicacion;
    private RadioButton rbEnSitio;
    private RadioButton rbADomicilio;
    private Button btnSelectDate;
    private Button btnSelectTime;
    private TextView tvFechaHoraSeleccionada;
    private Button btnSolicitarCotizacion;

    // Variables para almacenar la selección
    private String selectedService = "";
    private String selectedDate = "";
    private String selectedTime = "";

    // Lista de servicios de "Car Wash El Catracho"
    private final String[] servicios = new String[]{
            "Seleccione un servicio...",
            "Lavado General (L. 100/150)",
            "Lavado Completo (L. 150/200)",
            "Cambio de Aceite", // CON RESTRICCIÓN: Solo en centro de servicio
            "Lavado de Motor (L. 400)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Asegúrate de que el nombre del layout coincida con el nombre del archivo XML
        setContentView(R.layout.activity_solicitar_cotizacion);

        // 1. Inicializar Views
        etAutoModelo = findViewById(R.id.et_auto_modelo);
        spinnerTipoServicio = findViewById(R.id.spinner_tipo_servicio);
        rgUbicacion = findViewById(R.id.rg_ubicacion);
        rbEnSitio = findViewById(R.id.rb_en_sitio);
        rbADomicilio = findViewById(R.id.rb_a_domicilio);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnSelectTime = findViewById(R.id.btn_select_time);
        tvFechaHoraSeleccionada = findViewById(R.id.tv_fecha_hora_seleccionada);
        btnSolicitarCotizacion = findViewById(R.id.btn_solicitar_cotizacion);

        // 2. Configurar el Spinner de Servicios
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, servicios);
        spinnerTipoServicio.setAdapter(adapter);

        // 3. Listener para la selección de servicio (maneja la restricción de "Cambio de Aceite")
        spinnerTipoServicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedService = servicios[position];
                // Verifica si el servicio seleccionado es "Cambio de Aceite"
                boolean isOilChange = selectedService.equals("Cambio de Aceite");

                // Restricción: Si es Cambio de Aceite, la opción "A Domicilio" se deshabilita
                rbADomicilio.setEnabled(!isOilChange);

                if (isOilChange) {
                    // Si es Cambio de Aceite, forzamos la selección a "En Centro de Servicio"
                    rbEnSitio.setChecked(true);
                    Toast.makeText(SolicitarCotizacionActivity.this,
                            "El Cambio de Aceite solo se realiza en el Centro de Servicio.",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedService = "";
            }
        });

        // 4. Listeners para seleccionar Fecha y Hora
        btnSelectDate.setOnClickListener(v -> showDatePickerDialog());
        btnSelectTime.setOnClickListener(v -> showTimePickerDialog());

        // 5. Listener para el botón principal de Cotización
        btnSolicitarCotizacion.setOnClickListener(v -> solicitarCotizacion());
    }

    /**
     * Muestra el diálogo para seleccionar la fecha del servicio.
     */
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, y, m, d) -> {
                    // Formatea la fecha y la almacena
                    selectedDate = String.format(Locale.getDefault(), "%d/%d/%d", d, m + 1, y);
                    updateDateTimeDisplay();
                }, year, month, day);

        // Opcional: Establecer la fecha mínima como hoy
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    /**
     * Muestra el diálogo para seleccionar la hora del servicio.
     */
    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, h, m) -> {
                    // Formatea la hora y la almacena
                    selectedTime = String.format(Locale.getDefault(), "%02d:%02d", h, m);
                    updateDateTimeDisplay();
                }, hour, minute, false); // 'false' para formato 12 horas, 'true' para 24 horas
        timePickerDialog.show();
    }

    /**
     * Actualiza el TextView con la fecha y hora seleccionadas.
     */
    private void updateDateTimeDisplay() {
        if (!selectedDate.isEmpty() && !selectedTime.isEmpty()) {
            tvFechaHoraSeleccionada.setText("Fecha y hora seleccionadas: " + selectedDate + " a las " + selectedTime);
        } else if (!selectedDate.isEmpty()) {
            tvFechaHoraSeleccionada.setText("Fecha seleccionada: " + selectedDate + " - Hora Pendiente");
        } else if (!selectedTime.isEmpty()) {
            tvFechaHoraSeleccionada.setText("Hora seleccionada: " + selectedTime + " - Fecha Pendiente");
        }
    }

    /**
     * Valida los datos y simula el envío de la cotización (Notificación Push).
     */
    private void solicitarCotizacion() {
        String autoModelo = etAutoModelo.getText().toString().trim();
        int selectedLocationId = rgUbicacion.getCheckedRadioButtonId();

        // 1. Validar campos
        if (autoModelo.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el modelo de su vehículo.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedService.isEmpty() || selectedService.equals(servicios[0])) {
            Toast.makeText(this, "Por favor, seleccione un tipo de servicio.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Por favor, seleccione la fecha y hora del servicio.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedLocationId == -1) {
            Toast.makeText(this, "Por favor, seleccione el tipo de ubicación.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Determinar la ubicación
        RadioButton selectedLocation = findViewById(selectedLocationId);
        String ubicacion = selectedLocation.getText().toString();

        // 3. Simular el proceso de cotización
        String resumen = String.format(
                "Cotización para: %s\nServicio: %s\nUbicación: %s\nFecha y Hora: %s a las %s",
                autoModelo, selectedService, ubicacion, selectedDate, selectedTime
        );

        // En una aplicación real, aquí se enviarían los datos a un servidor
        // para que genere la cotización y envíe la Notificación Push de respuesta.
        Toast.makeText(this, "Solicitud de Cotización Enviada. ¡Espere su Notificación Push de respuesta!", Toast.LENGTH_LONG).show();

        // Por demostración, mostramos el resumen en el log o un Toast más detallado.
        // Log.d("COTIZACION", resumen); // Usar Logcat en un proyecto real

        // Opcional: Limpiar la pantalla después del envío
        // etAutoModelo.setText("");
        // spinnerTipoServicio.setSelection(0);
        // selectedDate = "";
        // selectedTime = "";
        // tvFechaHoraSeleccionada.setText("Fecha y hora seleccionadas: N/A");
        // rbEnSitio.setChecked(true); // Restaurar la ubicación predeterminada
    }
}
