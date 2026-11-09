package com.example.ra2_actividad2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var spCategory: Spinner
    private lateinit var imgCategoryBitmap: ImageView
    private lateinit var btnNextActivity: Button
    private lateinit var mainLayout: View

    // Categorías exigidas en el PDF de la práctica
    private val categories = arrayOf(
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
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.mainLayout)
        spCategory = findViewById(R.id.spCategory)
        imgCategoryBitmap = findViewById(R.id.imgCategoryBitmap)
        btnNextActivity = findViewById(R.id.btnNextActivity)

        // Asignar las categorías al Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = adapter

        // Requisito PDF: La imagen cambia según la categoría usando la clase Bitmap
        spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                loadCategoryBitmap(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Requisito PDF: Pasar la categoría a la segunda actividad (MainActivity2) usando Intent
        btnNextActivity.setOnClickListener {
            val selectedCategory = categories[spCategory.selectedItemPosition]
            val intent = Intent(this, MainActivity2::class.java).apply {
                putExtra("SELECTED_CATEGORY", selectedCategory)
            }
            startActivity(intent)
        }
    }

    /**
     * Requisito de evaluación: Uso explícito de Bitmap y BitmapFactory
     */
    private fun loadCategoryBitmap(categoryName: String) {
        // Usa una imagen por defecto (o los drawables que añadas a res/drawable)
        val drawableResId = when (categoryName) {
            "Distance" -> R.drawable.ic_launcher_foreground
            "Temperature" -> R.drawable.ic_launcher_foreground
            "Volume" -> R.drawable.ic_launcher_foreground
            "Mass and Weight" -> R.drawable.ic_launcher_foreground
            "Time" -> R.drawable.ic_launcher_foreground
            "Digital Information" -> R.drawable.ic_launcher_foreground
            "Currency" -> R.drawable.ic_launcher_foreground
            "Area" -> R.drawable.ic_launcher_foreground
            "Speed" -> R.drawable.ic_launcher_foreground
            "Pressure" -> R.drawable.ic_launcher_foreground
            else -> R.drawable.ic_launcher_foreground
        }

        // Carga explícita con la clase Bitmap
        val bitmap: Bitmap? = BitmapFactory.decodeResource(resources, drawableResId)

        if (bitmap != null) {
            imgCategoryBitmap.setImageBitmap(bitmap)
        } else {
            Snackbar.make(mainLayout, "Error al cargar la imagen", Snackbar.LENGTH_SHORT).show()
        }
    }
}