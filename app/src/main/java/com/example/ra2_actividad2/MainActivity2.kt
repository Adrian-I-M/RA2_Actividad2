package com.example.ra2_actividad2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity2 : AppCompatActivity() {

    private lateinit var tvCategoryTitle: TextView
    private lateinit var etInputValue: EditText
    private lateinit var spFromUnit: Spinner
    private lateinit var spToUnit: Spinner
    private lateinit var btnConvert: Button
    private lateinit var tvResult: TextView
    private lateinit var btnBack: Button

    private var currentCategory: String = "Distance"

    // Factores de conversión genéricos respecto a una unidad base (Sin cadenas if-else)
    private val distanceFactors = mapOf(
        "Metros" to 1.0,
        "Kilómetros" to 1000.0,
        "Millas" to 1609.34,
        "Pies" to 0.3048
    )

    private val massFactors = mapOf(
        "Gramos" to 1.0,
        "Kilogramos" to 1000.0,
        "Libras" to 453.592,
        "Onzas" to 28.3495
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2) // Apunta a activity_main2.xml

        // Inicializar vistas
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle)
        etInputValue = findViewById(R.id.etInputValue)
        spFromUnit = findViewById(R.id.spFromUnit)
        spToUnit = findViewById(R.id.spToUnit)
        btnConvert = findViewById(R.id.btnConvert)
        tvResult = findViewById(R.id.tvResult)
        btnBack = findViewById(R.id.btnBack)

        // Recibir la categoría enviada desde MainActivity
        currentCategory = intent.getStringExtra("EXTRA_CATEGORY") ?: "Distance"
        tvCategoryTitle.text = "Categoría: $currentCategory"

        setupSpinners()

        // Botón para calcular la conversión
        btnConvert.setOnClickListener {
            performConversion()
        }

        // Botón para volver a la primera actividad
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupSpinners() {
        val units = when (currentCategory) {
            "Mass" -> massFactors.keys.toList()
            else -> distanceFactors.keys.toList()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spFromUnit.adapter = adapter
        spToUnit.adapter = adapter
    }

    private fun performConversion() {
        val inputText = etInputValue.text.toString().trim()

        // Validaciones obligatorias con Snackbar (Sin usar Toast)
        if (inputText.isEmpty()) {
            Snackbar.make(findViewById(R.id.main), "Por favor, introduce un valor", Snackbar.LENGTH_SHORT).show()
            return
        }

        val inputVal = inputText.toDoubleOrNull()
        if (inputVal == null) {
            Snackbar.make(findViewById(R.id.main), "Número no válido", Snackbar.LENGTH_SHORT).show()
            return
        }

        val fromUnit = spFromUnit.selectedItem.toString()
        val toUnit = spToUnit.selectedItem.toString()

        val result: Double = if (currentCategory == "Temperature") {
            convertTemperature(inputVal, fromUnit, toUnit)
        } else {
            val factors = if (currentCategory == "Mass") massFactors else distanceFactors
            val fromFactor = factors[fromUnit] ?: 1.0
            val toFactor = factors[toUnit] ?: 1.0

            // Fórmula genérica: (Valor * FactorOrigen) / FactorDestino
            (inputVal * fromFactor) / toFactor
        }

        tvResult.text = "Resultado: %.4f $toUnit".format(result)
        Snackbar.make(findViewById(R.id.main), "Conversión realizada con éxito", Snackbar.LENGTH_SHORT).show()
    }

    private fun convertTemperature(value: Double, from: String, to: String): Double {
        if (from == to) return value
        val celsius = when (from) {
            "Fahrenheit" -> (value - 32) * 5 / 9
            "Kelvin" -> value - 273.15
            else -> value
        }
        return when (to) {
            "Fahrenheit" -> (celsius * 9 / 5) + 32
            "Kelvin" -> celsius + 273.15
            else -> celsius
        }
    }
}