package com.example.trabalhopratico1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trabalhopratico1.adapter.ChamadoAdapter;
import com.example.trabalhopratico1.database.ChamadoDAO;
import com.example.trabalhopratico1.model.Chamado;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class CadastroChamadoActivity extends AppCompatActivity {

    private TextInputEditText etTitulo, etData, etDescricao, etLocal;
    private RadioGroup rgTipo;
    private ChamadoDAO dao;
    private int anoSel, mesSel, diaSel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_chamado);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Cadastrar Chamado");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dao = new ChamadoDAO(this);

        etTitulo    = findViewById(R.id.etTitulo);
        etData      = findViewById(R.id.etData);
        etDescricao = findViewById(R.id.etDescricao);
        etLocal     = findViewById(R.id.etLocal);
        rgTipo      = findViewById(R.id.rgTipo);

        // Preenche data de hoje automaticamente
        Calendar hoje = Calendar.getInstance();
        anoSel = hoje.get(Calendar.YEAR);
        mesSel = hoje.get(Calendar.MONTH);
        diaSel = hoje.get(Calendar.DAY_OF_MONTH);
        atualizarCampoData();

        // Abre DatePicker ao clicar no campo data
        etData.setOnClickListener(v -> {
            DatePickerDialog dpd = new DatePickerDialog(this,
                    (view, year, month, day) -> {
                        anoSel = year; mesSel = month; diaSel = day;
                        atualizarCampoData();
                    }, anoSel, mesSel, diaSel);
            dpd.show();
        });

        findViewById(R.id.btnSalvar).setOnClickListener(v -> salvarChamado());
    }

    private void atualizarCampoData() {
        // Exibe no formato dd/MM/yyyy
        etData.setText(String.format("%02d/%02d/%04d", diaSel, mesSel + 1, anoSel));
    }

    private String getDataISO() {
        // Armazena no banco como yyyy-MM-dd (permite ordenação correta)
        return String.format("%04d-%02d-%02d", anoSel, mesSel + 1, diaSel);
    }

    private void salvarChamado() {
        String titulo = etTitulo.getText() != null ? etTitulo.getText().toString().trim() : "";

        if (TextUtils.isEmpty(titulo)) {
            etTitulo.setError("Informe o título do problema");
            etTitulo.requestFocus();
            return;
        }

        String tipo = (rgTipo.getCheckedRadioButtonId() == R.id.rbTI) ? "TI" : "Infraestrutura";
        String descricao = etDescricao.getText() != null ? etDescricao.getText().toString().trim() : "";
        String local     = etLocal.getText()     != null ? etLocal.getText().toString().trim()     : "";

        Chamado c = new Chamado(titulo, getDataISO(), descricao, local, tipo, "Aberto", "");
        long id = dao.inserir(c);

        if (id > 0) {
            Toast.makeText(this, "Chamado #" + id + " aberto com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar chamado.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}