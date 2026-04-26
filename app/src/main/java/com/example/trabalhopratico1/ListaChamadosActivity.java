package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trabalhopratico1.adapter.ChamadoAdapter;
import com.example.trabalhopratico1.database.ChamadoDAO;
import com.example.trabalhopratico1.model.Chamado;
import java.util.List;

public class ListaChamadosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvVazio, tvContador;
    private ChamadoAdapter adapter;
    private ChamadoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_chamados);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chamados");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dao         = new ChamadoDAO(this);
        recyclerView= findViewById(R.id.recyclerView);
        tvVazio     = findViewById(R.id.tvVazio);
        tvContador  = findViewById(R.id.tvContador);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.fabNovo).setOnClickListener(v ->
                startActivity(new Intent(this, CadastroChamadoActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    private void carregarLista() {
        List<Chamado> lista = dao.listarTodos();

        if (lista.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvVazio.setVisibility(View.VISIBLE);
            tvContador.setText("0 chamados");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvVazio.setVisibility(View.GONE);
            tvContador.setText(lista.size() + " chamado(s)");

            if (adapter == null) {
                adapter = new ChamadoAdapter(this, lista, chamado ->
                        abrirAtendimento(chamado.getId()));
                recyclerView.setAdapter(adapter);
            } else {
                adapter.atualizarLista(lista);
            }
        }
    }

    private void abrirAtendimento(int id) {
        Intent i = new Intent(this, AtendimentoActivity.class);
        i.putExtra("chamado_id", id);
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}