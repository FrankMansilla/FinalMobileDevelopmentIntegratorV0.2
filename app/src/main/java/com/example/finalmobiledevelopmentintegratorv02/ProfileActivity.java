package com.example.finalmobiledevelopmentintegratorv02;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etRepeatPassword;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        Button btnCreateUser = findViewById(R.id.btnCreateUser);
        LinearLayout profileFormContainer = findViewById(R.id.profileFormContainer);

        // Inicializar la base de datos
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        database = databaseHelper.getWritableDatabase();

        boolean hideProfile = getIntent().getBooleanExtra("hide_profile", false);

        if (hideProfile) {
            profileFormContainer.setVisibility(View.GONE);
        } else {
            profileFormContainer.setVisibility(View.VISIBLE);
        }

        btnCreateUser.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String repeatPassword = etRepeatPassword.getText().toString().trim();

            // Verificar si el nombre de usuario tiene entre 4 y 15 caracteres
            if (username.length() < 4 || username.length() > 15) {
                showToast("El nombre de usuario debe tener entre 4 y 15 caracteres");
            } else if (password.isEmpty()) {
                showToast("La contraseña no debe estar vacía");
            } else if (!password.equals(repeatPassword)) {
                showToast("Las contraseñas no coinciden");
            } else {
                // Insertar el nuevo usuario en la base de datos
                if (insertUser(username, password)) {
                    showToast("Usuario creado exitosamente");
                    // Cerrar la actividad y volver a la página principal (MainActivity)
                    finish();
                } else {
                    showToast("Error al crear el usuario");
                }
            }
        });
    }

    // Método para insertar un nuevo usuario en la base de datos
    private boolean insertUser(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        long newRowId = database.insert(DatabaseHelper.TABLE_NAME, null, values);
        return newRowId != -1;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
