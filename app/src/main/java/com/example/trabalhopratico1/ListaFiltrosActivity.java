package com.example.trabalhopratico1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trabalhopratico1.adapter.ChamadoAdapter;
import com.example.trabalhopratico1.database.ChamadoDAO;
import com.example.trabalhopratico1.model.Chamado;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ListaFiltrosActivity extends AppCompatActivity {

    private Spinner spinnerFiltroStatus;
    private TextInputEditText etDataInicio, etDataFim;
    private RecyclerView recyclerView;
    private TextView tvVazio, tvResultados;
    private ChamadoAdapter adapter;
    private ChamadoDAO dao;

    private String dataInicioISO = "";
    private String dataFimISO    = "";

    private final List<String> statusOpcoes =
            Arrays.asList("Todos", "Aberto", "Em Atendimento", "Concluído");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_filtros);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.filtros));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dao          = new ChamadoDAO(this);
        recyclerView = findViewById(R.id.recyclerFiltros);
        tvVazio      = findViewById(R.id.tvVazioFiltros);
        tvResultados = findViewById(R.id.tvResultados);
        etDataInicio = findViewById(R.id.etDataInicio);
        etDataFim    = findViewById(R.id.etDataFim);
        spinnerFiltroStatus = findViewById(R.id.spinnerFiltroStatus);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> spAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusOpcoes);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroStatus.setAdapter(spAdapter);

        etDataInicio.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> {
                dataInicioISO = String.format("%04d-%02d-%02d", y, m + 1, d);
                etDataInicio.setText(String.format("%02d/%02d/%04d", d, m + 1, y));
            }, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        etDataFim.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> {
                dataFimISO = String.format("%04d-%02d-%02d", y, m + 1, d);
                etDataFim.setText(String.format("%02d/%02d/%04d", d, m + 1, y));
            }, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        findViewById(R.id.btnAplicarFiltros).setOnClickListener(v -> aplicarFiltros());
        findViewById(R.id.btnLimparFiltros).setOnClickListener(v -> limparFiltros());

        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String status = spinnerFiltroStatus.getSelectedItem().toString();
        List<Chamado> lista = dao.listarComFiltros(status, dataInicioISO, dataFimISO);
        exibirResultados(lista);
    }

    private void limparFiltros() {
        spinnerFiltroStatus.setSelection(0);
        etDataInicio.setText("");
        etDataFim.setText("");
        dataInicioISO = "";
        dataFimISO    = "";
        aplicarFiltros();
    }

    private void exibirResultados(List<Chamado> lista) {
        tvResultados.setText("Resultados: " + lista.size());
        if (lista.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvVazio.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvVazio.setVisibility(View.GONE);
            if (adapter == null) {
                adapter = new ChamadoAdapter(this, lista, chamado -> {
                    Intent i = new Intent(this, AtendimentoActivity.class);
                    i.putExtra("chamado_id", chamado.getId());
                    startActivity(i);
                });
                recyclerView.setAdapter(adapter);
            } else {
                adapter.atualizarLista(lista);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        aplicarFiltros();
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}