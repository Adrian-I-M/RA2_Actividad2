package com.example.ra2_actividad2;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private TextView tvCategoryTitle;
    private EditText etInputValue;
    private Spinner spFromUnit;
    private Spinner spToUnit;
    private Button btnConvert;
    private TextView tvResult;
    private Button btnBack;

    private String currentCategory = "Distance";

    // Se utiliza LinkedHashMap para garantizar que el orden de inserción de las unidades
    // se mantenga intacto al mostrarlas en los Spinners de la interfaz.
    private final Map<String, Double> distanceFactors = new LinkedHashMap<>();
    private final Map<String, Double> massFactors = new LinkedHashMap<>();
    private final Map<String, Double> volumeFactors = new LinkedHashMap<>();
    private final Map<String, Double> timeFactors = new LinkedHashMap<>();
    private final Map<String, Double> digitalFactors = new LinkedHashMap<>();
    private final Map<String, Double> speedFactors = new LinkedHashMap<>();
    private final Map<String, Double> currencyFactors = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initFactors();

        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        etInputValue = findViewById(R.id.etInputValue);
        spFromUnit = findViewById(R.id.spFromUnit);
        spToUnit = findViewById(R.id.spToUnit);
        btnConvert = findViewById(R.id.btnConvert);
        tvResult = findViewById(R.id.tvResult);
        btnBack = findViewById(R.id.btnBack);

        String extra = getIntent().getStringExtra("EXTRA_CATEGORY");
        if (extra != null && !extra.isEmpty()) {
            currentCategory = extra;
        }
        tvCategoryTitle.setText("Categoría: " + currentCategory);

        setupSpinners();

        btnConvert.setOnClickListener(v -> performConversion());
        btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Inicializa los factores de conversión estableciendo una unidad base (valor 1.0)
     * por cada categoría y calculando la equivalencia del resto respecto a dicha base.
     */
    private void initFactors() {
        distanceFactors.put("Metros", 1.0);
        distanceFactors.put("Kilómetros", 1000.0);
        distanceFactors.put("Millas", 1609.34);
        distanceFactors.put("Pies", 0.3048);
        distanceFactors.put("Pulgadas", 0.0254);

        massFactors.put("Gramos", 1.0);
        massFactors.put("Kilogramos", 1000.0);
        massFactors.put("Libras", 453.592);
        massFactors.put("Onzas", 28.3495);

        volumeFactors.put("Litros", 1.0);
        volumeFactors.put("Mililitros", 0.001);
        volumeFactors.put("Galones", 3.78541);

        timeFactors.put("Segundos", 1.0);
        timeFactors.put("Minutos", 60.0);
        timeFactors.put("Horas", 3600.0);
        timeFactors.put("Días", 86400.0);

        digitalFactors.put("Bytes", 1.0);
        digitalFactors.put("Kilobytes", 1024.0);
        digitalFactors.put("Megabytes", 1048576.0);
        digitalFactors.put("Gigabytes", 1073741824.0);

        speedFactors.put("m/s", 1.0);
        speedFactors.put("km/h", 0.277778);
        speedFactors.put("mph", 0.44704);

        currencyFactors.put("Euros", 1.0);
        currencyFactors.put("Dólares (USD)", 0.92);
        currencyFactors.put("Libras (GBP)", 1.17);
        currencyFactors.put("Yenes (JPY)", 0.0062);
    }

    private void setupSpinners() {
        List<String> units;
        switch (currentCategory) {
            case "Temperature": units = Arrays.asList("Celsius", "Fahrenheit", "Kelvin"); break;
            case "Mass": units = new ArrayList<>(massFactors.keySet()); break;
            case "Volume": units = new ArrayList<>(volumeFactors.keySet()); break;
            case "Time": units = new ArrayList<>(timeFactors.keySet()); break;
            case "Digital Information": units = new ArrayList<>(digitalFactors.keySet()); break;
            case "Speed": units = new ArrayList<>(speedFactors.keySet()); break;
            case "Currency": units = new ArrayList<>(currencyFactors.keySet()); break;
            default: units = new ArrayList<>(distanceFactors.keySet()); break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFromUnit.setAdapter(adapter);
        spToUnit.setAdapter(adapter);
    }

    /**
     * Procesa la conversión aplicando un enfoque genérico para evitar estructuras if-else masivas.
     */
    private void performConversion() {
        String inputText = etInputValue.getText().toString().trim();

        if (inputText.isEmpty()) {
            Snackbar.make(findViewById(R.id.main), "Por favor, introduce un valor", Snackbar.LENGTH_SHORT).show();
            return;
        }

        double inputVal;
        try {
            inputVal = Double.parseDouble(inputText);
        } catch (NumberFormatException e) {
            Snackbar.make(findViewById(R.id.main), "Número no válido", Snackbar.LENGTH_SHORT).show();
            return;
        }

        String fromUnit = spFromUnit.getSelectedItem().toString();
        String toUnit = spToUnit.getSelectedItem().toString();
        double result;

        if ("Temperature".equals(currentCategory)) {
            result = convertTemperature(inputVal, fromUnit, toUnit);
        } else {
            Map<String, Double> factors;
            switch (currentCategory) {
                case "Mass": factors = massFactors; break;
                case "Volume": factors = volumeFactors; break;
                case "Time": factors = timeFactors; break;
                case "Digital Information": factors = digitalFactors; break;
                case "Speed": factors = speedFactors; break;
                case "Currency": factors = currencyFactors; break;
                default: factors = distanceFactors; break;
            }

            Double fromFactor = factors.get(fromUnit);
            Double toFactor = factors.get(toUnit);
            if (fromFactor == null) fromFactor = 1.0;
            if (toFactor == null) toFactor = 1.0;

            // Fórmula de conversión universal basada en los factores extraídos del mapa
            result = (inputVal * fromFactor) / toFactor;
        }

        tvResult.setText(String.format(Locale.getDefault(), "Resultado: %.4f %s", result, toUnit));
        Snackbar.make(findViewById(R.id.main), "Conversión realizada con éxito", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Lógica dedicada para las temperaturas, ya que no operan sobre un cero común.
     * Utiliza Celsius como puente intermedio de conversión.
     */
    private double convertTemperature(double value, String from, String to) {
        if (from.equals(to)) return value;

        double celsius;
        switch (from) {
            case "Fahrenheit": celsius = (value - 32) * 5 / 9; break;
            case "Kelvin": celsius = value - 273.15; break;
            default: celsius = value; break;
        }

        switch (to) {
            case "Fahrenheit": return (celsius * 9 / 5) + 32;
            case "Kelvin": return celsius + 273.15;
            default: return celsius;
        }
    }
}