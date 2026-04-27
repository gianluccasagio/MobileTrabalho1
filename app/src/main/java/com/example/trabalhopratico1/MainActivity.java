package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
                novoTema = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                novoTema = AppCompatDelegate.MODE_NIGHT_NO;
            }

            prefs.edit().putInt("tema_escolhido", novoTema).apply();

            AppCompatDelegate.setDefaultNightMode(novoTema);
        });

    }
}