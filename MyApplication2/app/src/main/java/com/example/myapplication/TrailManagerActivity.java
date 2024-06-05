package com.example.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class TrailManagerActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private String trailUrl;

    // Método chamado quando a atividade é criada
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_manager);

        // Inicialização banco de dados
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getReadableDatabase();

        // callback mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Voltar
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Compartilhar
        Button btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyTrailLinkToClipboard();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        showTrail();
    }

    //exibir a trilha
    private void showTrail() {
        // Consulta ao banco de dados para obter os pontos da trilha
        Cursor cursor = database.query(DatabaseHelper.TABLE_WAYPOINTS, null, null, null, null, null, null);
        PolylineOptions polylineOptions = new PolylineOptions();
        LatLng lastLatLng = null;
        StringBuilder urlBuilder = new StringBuilder("https://www.google.com/maps/dir/");

        // Loop para adicionar os pontos da trilha à linha poligonal e construir a URL da trilha
        while (cursor.moveToNext()) {
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LONGITUDE));
            LatLng latLng = new LatLng(latitude, longitude);
            polylineOptions.add(latLng);
            urlBuilder.append(latitude).append(",").append(longitude).append("/");

            lastLatLng = latLng;
        }
        cursor.close();

        if (lastLatLng != null) {
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 15));
        }

        // Adiciona a linha poligonal ao mapa
        myMap.addPolyline(polylineOptions);
        // Atualiza a URL da trilha
        trailUrl = urlBuilder.toString();
    }

    // Copiar Link
    private void copyTrailLinkToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Trail Link", trailUrl);
        clipboard.setPrimaryClip(clip);

        // Exibe uma mensagem informando que o link foi copiado com sucesso
        Toast.makeText(this, "Link copiado para a área de transferência", Toast.LENGTH_SHORT).show();
    }
}





