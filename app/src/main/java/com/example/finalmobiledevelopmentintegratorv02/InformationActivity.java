package com.example.finalmobiledevelopmentintegratorv02;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InformationActivity extends AppCompatActivity {

    private TextView textCarousel;
    private SeekBar levelCursor;
    private Button btnBack, btnNext;

    private String[] carouselTexts = {
            "Información de la empresa:",
            "Visión:\n\nSer reconocidos como líderes en la industria de La aviación, ofreciendo una experiencia de vuelo inigualable y superando las expectativas de nuestros clientes.",
            "Destinos:\n\nBlue Label Airlines opera vuelos a una amplia gama de destinos nacionales e internacionales. Sus principales hubs se encuentran en ciudades importantes, lo que facilita la conexión de pasajeros a través de una red de rutas bien establecida.",
            "Compromiso con la seguridad:\n\nLa seguridad es una prioridad para Blue Label Airlines. La aerolínea cumple con los más altos estándares de seguridad y mantenimiento de aeronaves, y su tripulación y personal de tierra están altamente capacitados para garantizar vuelos seguros y sin problemas.",
            "Atencion Al Cliente:\n\nEl servicio al cliente es una parte fundamental de la experiencia de vuelo de Blue Label Airlines. La aerolínea se esfuerza por brindar un servicio amable, eficiente y personalizado para asegurar que todos los pasajeros se sientan bienvenidos y atendidos durante su viaje."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        textCarousel = findViewById(R.id.textCarousel);
        levelCursor = findViewById(R.id.levelCursor);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);

        // Configurar los eventos del cursor y los botones
        levelCursor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Actualizar el texto del carrusel y mostrar/ocultar los botones según el nivel del cursor
                updateCarouselText(progress);
                updateButtonsVisibility(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnBack.setOnClickListener(v -> {
            // Lógica para manejar el botón "Back" según el nivel del cursor
            int progress = levelCursor.getProgress();
            if (progress > 0) {
                levelCursor.setProgress(progress - 1);
            }
            // intento de que me devuelva a home en el ultimo nivel:
            // else if (progress == levelCursor.getMax()){
              //  finish();
            //}
            else {
                // Si el cursor está en el primer nivel, regresar al MainActivity
                finish();
            }
        });

        btnNext.setOnClickListener(v -> {
            // Lógica para manejar el botón "Next" según el nivel del cursor
            int progress = levelCursor.getProgress();
            if (progress < levelCursor.getMax()) {
                levelCursor.setProgress(progress + 1);
            } else {
                // Si el cursor está en el último nivel, regresar al MainActivity (no me funciona xd)
                finish();
            }
        });
    }

    private void updateCarouselText(int level) {
        textCarousel.setText(carouselTexts[level]);
    }

    private void updateButtonsVisibility(int level) {
        if (level == 0) {
            // Primer nivel: Mostrar "Back to Home" y "Next"
            btnBack.setText("Back to Home");
            btnNext.setVisibility(Button.VISIBLE);
        } else if (level == levelCursor.getMax()) {
            // Último nivel: Mostrar "Back"
            btnBack.setText("Back");
            btnNext.setVisibility(Button.GONE);
        } else {
            // Niveles intermedios: Mostrar "Back" y "Next"
            btnBack.setText("Back");
            btnNext.setVisibility(Button.VISIBLE);
        }
    }
}
