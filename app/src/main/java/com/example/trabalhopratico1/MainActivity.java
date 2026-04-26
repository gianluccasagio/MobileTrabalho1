package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.res.Configuration;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("ConfiguracoesApp", MODE_PRIVATE);
        int temaSalvo = prefs.getInt("tema_escolhido", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(temaSalvo);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        findViewById(R.id.btnNovoChamado).setOnClickListener(v ->
                startActivity(new  Intent(this, CadastroChamadoActivity.class)));

        findViewById(R.id.btnListaChamados).setOnClickListener(v ->
                startActivity(new Intent(this, ListaChamadosActivity.class)));

        findViewById(R.id.btnFiltros).setOnClickListener(v ->
                startActivity(new Intent(this, ListaFiltrosActivity.class)));

        SwitchMaterial switchTema = findViewById(R.id.switchTema);
        int modoAtual = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switchTema.setChecked(modoAtual == Configuration.UI_MODE_NIGHT_YES);
        switchTema.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int novoTema;
            if (isChecked) {
                // Se a pessoa ligou o switch -> Tema Escuro
                novoTema = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                // Se a pessoa desligou o switch -> Tema Claro
                novoTema = AppCompatDelegate.MODE_NIGHT_NO;
            }

            // Salva a escolha
            prefs.edit().putInt("tema_escolhido", novoTema).apply();

            // Aplica a mudança de tema
            AppCompatDelegate.setDefaultNightMode(novoTema);
        });

    }
}