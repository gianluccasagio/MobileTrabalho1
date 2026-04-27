package com.example.trabalhopratico1;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import com.example.trabalhopratico1.adapter.ChamadoAdapter;
import com.example.trabalhopratico1.database.ChamadoDAO;
import com.example.trabalhopratico1.model.Chamado;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Arrays;
import java.util.List;

public class AtendimentoActivity extends AppCompatActivity {

    private ChamadoDAO dao;
    private Chamado chamado;
    private Spinner spinnerStatus;
    private TextInputEditText etSolucao;

    private final List<String> statusList =
            Arrays.asList("Aberto", "Em Atendimento", "Concluído");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atendimento);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Atendimento");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dao = new ChamadoDAO(this);
        int chamadoId = getIntent().getIntExtra("chamado_id", -1);

        if (chamadoId == -1) { finish(); return; }

        chamado = dao.buscarPorId(chamadoId);
        if (chamado == null) { finish(); return; }

        preencherDetalhes();
        configurarSpinner();

        etSolucao = findViewById(R.id.etSolucao);
        if (chamado.getSolucao() != null && !chamado.getSolucao().isEmpty())
            etSolucao.setText(chamado.getSolucao());

        findViewById(R.id.btnSalvarAtendimento).setOnClickListener(v -> salvar());
    }

    private void preencherDetalhes() {
        ((TextView) findViewById(R.id.tvDetalheNumero))
                .setText("#" + chamado.getId());
        ((TextView) findViewById(R.id.tvDetalheTitulo))
                .setText(chamado.getTitulo());
        ((TextView) findViewById(R.id.tvDetalheData))
                .setText("📅 " + ChamadoAdapter.formatarData(chamado.getData()));
        ((TextView) findViewById(R.id.tvDetalheLocal))
                .setText("📍 " + (chamado.getLocal() != null && !chamado.getLocal().isEmpty()
                        ? chamado.getLocal() : "Não informado"));
        ((TextView) findViewById(R.id.tvDetalheDescricao))
                .setText(chamado.getDescricao() != null && !chamado.getDescricao().isEmpty()
                        ? chamado.getDescricao() : "Sem descrição");

        TextView tvTipo = findViewById(R.id.tvDetalheTipo);
        tvTipo.setText(chamado.getTipo());
        int corTipo = "TI".equals(chamado.getTipo()) ? R.color.tipo_ti : R.color.tipo_infra;
        aplicarCorBadge(tvTipo, corTipo);

        TextView tvStatus = findViewById(R.id.tvDetalheStatus);
        tvStatus.setText(chamado.getStatus());
        aplicarCorBadge(tvStatus, getCorStatus(chamado.getStatus()));
    }

    private void configurarSpinner() {
        spinnerStatus = findViewById(R.id.spinnerStatus);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        int idx = statusList.indexOf(chamado.getStatus());
        if (idx >= 0) spinnerStatus.setSelection(idx);
    }

    private void salvar() {
        String novoStatus = spinnerStatus.getSelectedItem().toString();
        String solucao = etSolucao.getText() != null
                ? etSolucao.getText().toString().trim() : "";

        chamado.setStatus(novoStatus);
        chamado.setSolucao(solucao);

        int rows = dao.atualizar(chamado);
        if (rows > 0) {
            Toast.makeText(this, "Atendimento salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar.", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCorStatus(String status) {
        switch (status) {
            case "Em Atendimento": return R.color.status_em_atendimento;
            case "Concluído":      return R.color.status_concluido;
            default:               return R.color.status_aberto;
        }
    }

    private void aplicarCorBadge(TextView tv, int colorRes) {
        Drawable d = ContextCompat.getDrawable(this, R.drawable.bg_badge);
        if (d != null) {
            d = DrawableCompat.wrap(d.mutate());
            DrawableCompat.setTint(d, ContextCompat.getColor(this, colorRes));
            tv.setBackground(d);
        }
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_atendimento, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se ele clicou na Lixeira
        if (item.getItemId() == R.id.action_excluir) {
            new AlertDialog.Builder(this)
                    .setTitle("Excluir Chamado")
                    .setMessage("Tem certeza que deseja excluir este chamado permanentemente?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        // Deleta do banco
                        if (dao.excluir(chamado.getId())) {
                            Toast.makeText(this, "Chamado excluído!", Toast.LENGTH_SHORT).show();
                            finish(); // Fecha a tela e volta pra lista
                        } else {
                            Toast.makeText(this, "Erro ao excluir.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}