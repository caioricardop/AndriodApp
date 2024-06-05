package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class compartilharTrilha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartilhar_trilha);

        // Recuperar coordenadas armazenadas
        String coordinates = getSharedPreferences("AppPreferences", MODE_PRIVATE)
                .getString("LastCopiedCoordinates", "No coordinates found");

        TextView tvCoordinates = findViewById(R.id.tvCoordinates);
        tvCoordinates.setText(coordinates);

        Button btnShareFacebook = findViewById(R.id.btnShareFacebook);
        Button btnShareTwitter = findViewById(R.id.btnShareTwitter);
        Button btnShareWhatsApp = findViewById(R.id.btnShareWhatsApp);

        // Definindo o listener para o botão do Facebook
        btnShareFacebook.setOnClickListener(view -> shareOnSocialMedia("com.facebook.katana", coordinates));

        // Definindo o listener para o botão do Twitter
        btnShareTwitter.setOnClickListener(view -> shareOnSocialMedia("com.twitter.android", coordinates));

        // Definindo o listener para o botão do WhatsApp
        btnShareWhatsApp.setOnClickListener(view -> shareOnSocialMedia("com.whatsapp", coordinates));
    }

    private void shareOnSocialMedia(String packageName, String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");

        // Verifica se o pacote (app) está instalado
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            shareIntent.setPackage(packageName);
            startActivity(shareIntent);
        } else {
            // Mostrar um erro caso o app não esteja instalado
            Toast.makeText(this, "App não instalado.", Toast.LENGTH_SHORT).show();
        }
    }
}
