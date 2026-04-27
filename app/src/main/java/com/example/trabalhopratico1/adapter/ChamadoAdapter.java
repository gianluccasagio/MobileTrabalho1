package com.example.trabalhopratico1.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trabalhopratico1.R;
import com.example.trabalhopratico1.model.Chamado;
import java.util.List;

public class ChamadoAdapter extends RecyclerView.Adapter<ChamadoAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Chamado chamado);
    }

    private List<Chamado> lista;
    private final Context context;
    private final OnItemClickListener listener;

    public ChamadoAdapter(Context context, List<Chamado> lista, OnItemClickListener listener) {
        this.context = context;
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_chamado, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Chamado c = lista.get(position);

        h.tvNumero.setText("#" + c.getId());
        h.tvTitulo.setText(c.getTitulo());
        h.tvData.setText("📅 " + formatarData(c.getData()));
        h.tvLocal.setText("📍 " + (c.getLocal() != null && !c.getLocal().isEmpty()
                ? c.getLocal() : "Não informado"));
        h.tvTipo.setText(c.getTipo());
        h.tvStatus.setText(c.getStatus());

        int corTipo = "TI".equals(c.getTipo())
                ? R.color.tipo_ti : R.color.tipo_infra;
        aplicarCorBadge(h.tvTipo, corTipo);

        int corStatus;
        switch (c.getStatus()) {
            case "Em Atendimento": corStatus = R.color.status_em_atendimento; break;
            case "Concluído":      corStatus = R.color.status_concluido;       break;
            default:               corStatus = R.color.status_aberto;
        }
        aplicarCorBadge(h.tvStatus, corStatus);

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(c);
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public void atualizarLista(List<Chamado> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }

    private void aplicarCorBadge(TextView tv, int colorRes) {
        Drawable d = ContextCompat.getDrawable(context, R.drawable.bg_badge);
        if (d != null) {
            d = DrawableCompat.wrap(d.mutate());
            DrawableCompat.setTint(d, ContextCompat.getColor(context, colorRes));
            tv.setBackground(d);
        }
    }

    public static String formatarData(String data) {
        if (data == null || data.length() != 10) return data != null ? data : "";
        try {
            String[] p = data.split("-");
            return p[2] + "/" + p[1] + "/" + p[0];
        } catch (Exception e) { return data; }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumero, tvTitulo, tvData, tvLocal, tvTipo, tvStatus;
        ViewHolder(View v) {
            super(v);
            tvNumero = v.findViewById(R.id.tvNumero);
            tvTitulo = v.findViewById(R.id.tvTitulo);
            tvData   = v.findViewById(R.id.tvData);
            tvLocal  = v.findViewById(R.id.tvLocal);
            tvTipo   = v.findViewById(R.id.tvTipo);
            tvStatus = v.findViewById(R.id.tvStatus);
        }
    }
}