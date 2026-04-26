package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }
}