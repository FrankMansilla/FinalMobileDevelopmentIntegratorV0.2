package com.example.finalmobiledevelopmentintegratorv02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private LinearLayout loginFormContainer;
    private SQLiteDatabase database;

    // Nombre de las preferencias y clave para el estado de inicio de sesión
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    // Instancia única de DatabaseHelper utilizando el patrón Singleton
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        loginFormContainer = findViewById(R.id.loginFormContainer);

        setSupportActionBar(toolbar);

        // Obtener la instancia única de DatabaseHelper utilizando el patrón Singleton
        databaseHelper = DatabaseHelper.getInstance(this);
        database = databaseHelper.getWritableDatabase();

        // Obtener el estado de inicio de sesión almacenado en SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean(KEY_IS_LOGGED_IN, false);

        // Mostrar u ocultar el formulario de inicio de sesión en función del estado de inicio de sesión
        if (isLoggedIn) {
            hideLoginForm();
            showProfileView();
        } else {
            showLoginForm();
            hideProfileView();
        }

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Verificar las credenciales del usuario en la base de datos
            if (checkCredentials(username, password)) {
                showToast("Inicio de sesión exitoso");
                // Al iniciar sesión correctamente, actualizamos el estado y ocultamos el formulario de inicio de sesión
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.apply();
                hideLoginForm();
                showProfileView();
            } else {
                showToast("Credenciales incorrectas");
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Manejar la navegación según el ítem seleccionado
                if (item.getItemId() == R.id.action_buy) {
                    showBuyView();
                    showToast("Buy Clicked");
                    return true;
                } else if (item.getItemId() == R.id.action_information) {
                    Intent intent = new Intent(MainActivity.this, InformationActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.action_profile) {
                    if (isLoggedIn) {
                        showToast("Profile Clicked");
                    } else {
                        showToast("Profile Clicked");
                        showLoginForm();
                        hideProfileView();
                    }
                    return true;
                } else if (item.getItemId() == R.id.action_my_trips) {
                    showToast("My Trips Clicked");
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Al cerrar sesión, actualizamos el estado y mostramos el formulario de inicio de sesión
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, false);
            editor.apply();
            showToast("Cerrar sesión");
            showLoginForm();
            hideProfileView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Método para verificar las credenciales del usuario en la base de datos
    private boolean checkCredentials(String username, String password) {
        String[] columns = {DatabaseHelper.COLUMN_USERNAME};
        String selection = DatabaseHelper.COLUMN_USERNAME + " = ? AND " +
                DatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        boolean exists = cursor != null && cursor.moveToFirst(); // Check if the cursor is not null and has at least one row

        if (exists) {
            // If the cursor has valid data, you can access the username column
            int columnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME);
            String retrievedUsername = cursor.getString(columnIndex);
            showToast("Username found: " + retrievedUsername);
        }

        if (cursor != null) {
            cursor.close(); // Close the cursor after use to avoid resource leaks
        }

        return exists;
    }

    private void hideLoginForm() {
        loginFormContainer.setVisibility(View.GONE);
    }

    private void showLoginForm() {
        loginFormContainer.setVisibility(View.VISIBLE);
    }

    private void showProfileView() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void hideProfileView() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("hide_profile", true);
        startActivity(intent);
    }

    private void showBuyView() {
        startActivity(new Intent(this, BuyActivity.class));
    }
}
