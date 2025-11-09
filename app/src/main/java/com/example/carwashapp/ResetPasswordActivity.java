package com.example.carwashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    // 1. Declaración de Componentes del Layout y Firebase
    private TextInputEditText emailEditText;
    private MaterialButton resetButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth; // Instancia de Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password); // Asegúrate que el XML se llame 'activity_reset_password'

        // 2. Inicialización de Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 3. Enlace de los Componentes (Binding)
        emailEditText = findViewById(R.id.et_email);
        resetButton = findViewById(R.id.btn_reset_password);
        progressBar = findViewById(R.id.progress_bar);

        // 4. Configuración del Listener para el Botón
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptPasswordReset();
            }
        });
    }

    /**
     * Valida el correo electrónico y llama a la función de Firebase.
     */
    private void attemptPasswordReset() {
        // Obtener el texto del campo de correo y limpiarlo
        String email = emailEditText.getText().toString().trim();

        // Validación básica
        if (email.isEmpty()) {
            emailEditText.setError("El correo es requerido");
            emailEditText.requestFocus();
            return;
        }

        // Si el correo es válido, iniciar el proceso
        sendPasswordResetEmail(email);
    }

    /**
     * Envía el correo de restablecimiento usando Firebase.
     * @param email La dirección de correo del usuario.
     */
    private void sendPasswordResetEmail(String email) {
        // Mostrar la barra de progreso y deshabilitar el botón
        progressBar.setVisibility(View.VISIBLE);
        resetButton.setEnabled(false);

        // Llamada a la API de Firebase
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Ocultar la barra de progreso y habilitar el botón
                        progressBar.setVisibility(View.GONE);
                        resetButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            // **Éxito (Recomendación de Seguridad)**
                            // Siempre se debe mostrar un mensaje genérico de éxito,
                            // incluso si el correo no existe en la base de datos,
                            // para evitar que atacantes adivinen correos válidos.
                            Toast.makeText(ResetPasswordActivity.this,
                                    "Si la cuenta existe, se ha enviado un enlace a su correo.",
                                    Toast.LENGTH_LONG).show();

                            // Opcional: Cerrar la actividad y volver a la pantalla de Login
                            // finish();

                        } else {
                            // Fallo (ej: error de red, formato de correo inválido, etc.)
                            // NO revela si el correo existe o no, solo informa de un error técnico.
                            Toast.makeText(ResetPasswordActivity.this,
                                    "Error al intentar enviar el enlace. Por favor, revisa tu conexión.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
