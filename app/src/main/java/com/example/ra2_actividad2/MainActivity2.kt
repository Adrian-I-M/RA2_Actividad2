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

    // 1. DISTANCE (Base: Metros)
    private val distanceFactors = mapOf(
        "Metros" to 1.0,
        "Kilómetros" to 1000.0,
        "Millas" to 1609.34,
        "Pies" to 0.3048,
        "Pulgadas" to 0.0254
    )

    // 2. MASS (Base: Gramos)
    private val massFactors = mapOf(
        "Gramos" to 1.0,
        "Kilogramos" to 1000.0,
        "Libras" to 453.592,
        "Onzas" to 28.3495
    )

    // 3. VOLUME (Base: Litros)
    private val volumeFactors = mapOf(
        "Litros" to 1.0,
        "Mililitros" to 0.001,
        "Galones" to 3.78541
    )

    // 4. TIME (Base: Segundos)
    private val timeFactors = mapOf(
        "Segundos" to 1.0,
        "Minutos" to 60.0,
        "Horas" to 3600.0,
        "Días" to 86400.0
    )

    // 5. DIGITAL INFORMATION (Base: Bytes)
    private val digitalFactors = mapOf(
        "Bytes" to 1.0,
        "Kilobytes" to 1024.0,
        "Megabytes" to 1048576.0,
        "Gigabytes" to 1073741824.0
    )

    // 6. SPEED (Base: m/s)
    private val speedFactors = mapOf(
        "m/s" to 1.0,
        "km/h" to 0.277778,
        "mph" to 0.44704
    )

    // 7. CURRENCY (Base: Euros - Tasas de ejemplo según enunciado)
    private val currencyFactors = mapOf(
        "Euros" to 1.0,
        "Dólares (USD)" to 0.92,
        "Libras (GBP)" to 1.17,
        "Yenes (JPY)" to 0.0062
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        tvCategoryTitle = findViewById(R.id.tvCategoryTitle)
        etInputValue = findViewById(R.id.etInputValue)
        spFromUnit = findViewById(R.id.spFromUnit)
        spToUnit = findViewById(R.id.spToUnit)
        btnConvert = findViewById(R.id.btnConvert)
        tvResult = findViewById(R.id.tvResult)
        btnBack = findViewById(R.id.btnBack)

        // Recibir categoría enviada por el compañero (Importante: acuerdo de clave "EXTRA_CATEGORY")
        currentCategory = intent.getStringExtra("EXTRA_CATEGORY") ?: "Distance"
        tvCategoryTitle.text = "Categoría: $currentCategory"

        setupSpinners()

        btnConvert.setOnClickListener {
            performConversion()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupSpinners() {
        val units = when (currentCategory) {
            "Temperature" -> listOf("Celsius", "Fahrenheit", "Kelvin")
            "Mass" -> massFactors.keys.toList()
            "Volume" -> volumeFactors.keys.toList()
            "Time" -> timeFactors.keys.toList()
            "Digital Information" -> digitalFactors.keys.toList()
            "Speed" -> speedFactors.keys.toList()
            "Currency" -> currencyFactors.keys.toList()
            else -> distanceFactors.keys.toList()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spFromUnit.adapter = adapter
        spToUnit.adapter = adapter
    }

    private fun performConversion() {
        val inputText = etInputValue.text.toString().trim()

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
            val factors = when (currentCategory) {
                "Mass" -> massFactors
                "Volume" -> volumeFactors
                "Time" -> timeFactors
                "Digital Information" -> digitalFactors
                "Speed" -> speedFactors
                "Currency" -> currencyFactors
                else -> distanceFactors
            }

            val fromFactor = factors[fromUnit] ?: 1.0
            val toFactor = factors[toUnit] ?: 1.0

            // Cálculo genérico: (Valor * Factor Origen) / Factor Destino
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