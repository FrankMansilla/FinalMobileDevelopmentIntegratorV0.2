package com.example.finalmobiledevelopmentintegratorv02;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BuyActivity extends AppCompatActivity {

    private EditText etOrigin, etDestination;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        etOrigin = findViewById(R.id.etOrigin);
        etDestination = findViewById(R.id.etDestination);
        Button btnSwap = findViewById(R.id.btnSwap);
        Button btnBuy = findViewById(R.id.btnBuy);

        // Recuperar el objeto DatabaseHelper del Intent
        databaseHelper = (DatabaseHelper) getIntent().getSerializableExtra("databaseHelper");



        btnSwap.setOnClickListener(v -> {
            // Intercambiar los valores de los campos ORIGIN y DESTINATION
            String origin = etOrigin.getText().toString();
            String destination = etDestination.getText().toString();
            etOrigin.setText(destination);
            etDestination.setText(origin);
        });

        btnBuy.setOnClickListener(v -> {
            // Obtener los valores de ORIGIN y DESTINATION
            String origin = etOrigin.getText().toString().trim();
            String destination = etDestination.getText().toString().trim();

            // Validar que los campos no estén vacíos
            if (origin.isEmpty() || destination.isEmpty()) {
                showToast("Por favor ingrese el ORIGIN y DESTINATION.");
                return;
            }

            // Guardar los datos en la tabla "FlightsPurchased" de la base de datos
            boolean success = saveFlightData(origin, destination);

            if (success) {
                showToast("Compra realizada con éxito.");
            } else {
                showToast("Error al guardar los datos de la compra.");
            }
        });
    }

    private boolean saveFlightData(String origin, String destination) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("origin", origin);
        values.put("destination", destination);

        // Insertar los datos en la tabla "FlightsPurchased"
        long result = db.insert("FlightsPurchased", null, values);
        db.close();

        return result != -1;
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
