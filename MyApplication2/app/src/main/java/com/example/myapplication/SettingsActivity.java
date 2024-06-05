package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup rgMapType, rgSpeedUnit, rgCoordinateFormat, rgMapOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Inicialização dos grupos de botões de rádio
        rgMapType = findViewById(R.id.rgMapType);
        rgSpeedUnit = findViewById(R.id.rgSpeedUnit);
        rgCoordinateFormat = findViewById(R.id.rgCoordinateFormat);
        rgMapOrientation = findViewById(R.id.rgMapOrientation);

        Button btnBackFromSettings = findViewById(R.id.btnBackFromSettings);
        btnBackFromSettings.setOnClickListener(this);

        // Carregar configurações prévias
        loadSettings();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnBackFromSettings) {
            saveSettings();
            finish();
        }
    }

    private void saveSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        // Salvar as configurações com base na seleção do usuário
        editor.putInt("MAP_TYPE", getSelectedMapType());
        editor.putString("SPEED_UNIT", getSelectedSpeedUnit());
        editor.putString("COORDINATE_FORMAT", getSelectedCoordinateFormat());
        editor.putInt("MAP_ORIENTATION", getSelectedMapOrientation());

        editor.apply();
    }

    private void loadSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Aplicar as configurações salvas aos botões de rádio
        rgMapType.check(findRadioButtonIdByMapType(prefs.getInt("MAP_TYPE", GoogleMap.MAP_TYPE_NORMAL)));
        rgSpeedUnit.check(findRadioButtonIdBySpeedUnit(prefs.getString("SPEED_UNIT", "km/h")));
        rgCoordinateFormat.check(findRadioButtonIdByCoordinateFormat(prefs.getString("COORDINATE_FORMAT", "Degrees")));
        rgMapOrientation.check(findRadioButtonIdByMapOrientation(prefs.getInt("MAP_ORIENTATION", 0)));
    }

    // Métodos para recuperar o valor selecionado nos grupos de botões de rádio
    private int getSelectedMapType() {
        int selectedMapTypeId = rgMapType.getCheckedRadioButtonId();
        if (selectedMapTypeId == R.id.rbSatellite) {
            return GoogleMap.MAP_TYPE_SATELLITE;
        }
        return GoogleMap.MAP_TYPE_NORMAL;  // Default case
    }

    private String getSelectedSpeedUnit() {
        int selectedSpeedUnitId = rgSpeedUnit.getCheckedRadioButtonId();
        if (selectedSpeedUnitId == R.id.rbMS) {
            return "m/s";
        }
        return "km/h";  // Default case
    }

    private String getSelectedCoordinateFormat() {
        int selectedCoordinateFormatId = rgCoordinateFormat.getCheckedRadioButtonId();
        if (selectedCoordinateFormatId == R.id.rbDegreesMinutesSeconds) {
            return "Degrees, Minutes, and Seconds";
        } else if (selectedCoordinateFormatId == R.id.rbDegreesMinutes) {
            return "Degrees and Minutes";
        }
        return "Degrees";  // Default case
    }

    private int getSelectedMapOrientation() {
        int selectedMapOrientationId = rgMapOrientation.getCheckedRadioButtonId();
        if (selectedMapOrientationId == R.id.rbNorthUp) {
            return 1;  // North up
        } else if (selectedMapOrientationId == R.id.rbCourseUp) {
            return 2;  // Course up
        }
        return 0;  // Default case (No rotation)
    }

    // Métodos auxiliares para encontrar os IDs de botões de rádio com base nos valores salvos
    private int findRadioButtonIdByMapType(int mapType) {
        if (mapType == GoogleMap.MAP_TYPE_SATELLITE) {
            return R.id.rbSatellite;
        }
        return R.id.rbVector;  // Default to vector
    }

    private int findRadioButtonIdBySpeedUnit(String speedUnit) {
        if ("m/s".equals(speedUnit)) {
            return R.id.rbMS;
        }
        return R.id.rbKMH;  // Default to km/h
    }

    private int findRadioButtonIdByCoordinateFormat(String coordinateFormat) {
        if ("Degrees and Minutes".equals(coordinateFormat)) {
            return R.id.rbDegreesMinutes;
        } else if ("Degrees, Minutes, and Seconds".equals(coordinateFormat)) {
            return R.id.rbDegreesMinutesSeconds;
        }
        return R.id.rbDegrees;  // Default to Degrees
    }

    private int findRadioButtonIdByMapOrientation(int orientation) {
        if (orientation == 1) {
            return R.id.rbNorthUp;
        } else if (orientation == 2) {
            return R.id.rbCourseUp;
        }
        return R.id.rbNone;  // Default to no rotation
    }
}
