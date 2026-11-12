package com.example.ra2_actividad2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private Spinner spCategory;
    private ImageView imgCategoryBitmap;
    private Button btnNextActivity;
    private View mainLayout;

    // Categorías exigidas en el PDF de la práctica
    private final String[] categories = {
            "Distance",
            "Temperature",
            "Volume",
            "Mass and Weight",
            "Time",
            "Digital Information",
            "Currency",
            "Area",
            "Speed",
            "Pressure"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.mainLayout);
        spCategory = findViewById(R.id.spCategory);
        imgCategoryBitmap = findViewById(R.id.imgCategoryBitmap);
        btnNextActivity = findViewById(R.id.btnNextActivity);

        // Asignar las categorías al Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        // Requisito PDF: La imagen cambia según la categoría usando la clase Bitmap
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories[position];
                loadCategoryBitmap(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se requiere acción
            }
        });

        // Requisito PDF: Pasar la categoría a la segunda actividad (MainActivity2) usando Intent
        btnNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = categories[spCategory.getSelectedItemPosition()];
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("SELECTED_CATEGORY", selectedCategory);
                startActivity(intent);
            }
        });
    }

    /**
     * Requisito de evaluación: Uso explícito de Bitmap y BitmapFactory
     */
    private void loadCategoryBitmap(String categoryName) {
        int drawableResId;

        // Usa una imagen por defecto (o los drawables que añadas a res/drawable)
        switch (categoryName) {
            case "Distance":
                drawableResId = R.drawable.ic_launcher_foreground;
                break;
            case "Temperature":
                drawableResId = R.drawable.ic_launcher_foreground;
                break;
            case "Volume":
                drawableResId = R.drawable.ic_launcher_foreground;
                break;
            case "Mass and Weight":
                drawableResId = R.drawable.ic_launcher_foreground;
                break;
            case "Time":
                drawableResId = R.drawable.ic_launcher_foreground;
                break;
            case "Digital Information":
                drawableResId = R.drawable.ic_launcher_foreground;
                break;
            case "Currency":
                drawableResId = R.drawable.ic_launcher_foreground;
                break;
            case "Area":
                drawableResId = R.drawable.ic_launcher_foreground;
                break;
            case "Speed":
                drawableResId = R.drawable.ic_launcher_foreground;
                break;
            case "Pressure":
                drawableResId = R.drawable.ic_launcher_foreground;
                break;
            default:
                drawableResId = R.drawable.ic_launcher_foreground;
                break;
        }

        // Carga explícita con la clase Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableResId);

        if (bitmap != null) {
            imgCategoryBitmap.setImageBitmap(bitmap);
        } else {
            Snackbar.make(mainLayout, "Error al cargar la imagen", Snackbar.LENGTH_SHORT).show();
        }
    }
}