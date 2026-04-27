package com.example.trabalhopratico1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.trabalhopratico1.model.Chamado;
import java.util.ArrayList;
import java.util.List;

public class ChamadoDAO {

    private final DatabaseHelper helper;

    public ChamadoDAO(Context context) {
        helper = new DatabaseHelper(context);
    }

    public long inserir(Chamado c) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(DatabaseHelper.COL_TITULO,    c.getTitulo());
        v.put(DatabaseHelper.COL_DATA,      c.getData());
        v.put(DatabaseHelper.COL_DESCRICAO, c.getDescricao());
        v.put(DatabaseHelper.COL_LOCAL,     c.getLocal());
        v.put(DatabaseHelper.COL_TIPO,      c.getTipo());
        v.put(DatabaseHelper.COL_STATUS,    c.getStatus());
        v.put(DatabaseHelper.COL_SOLUCAO,   c.getSolucao());
        long id = db.insert(DatabaseHelper.TABLE, null, v);
        db.close();
        return id;
    }

    public int atualizar(Chamado c) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(DatabaseHelper.COL_STATUS,  c.getStatus());
        v.put(DatabaseHelper.COL_SOLUCAO, c.getSolucao());
        int rows = db.update(DatabaseHelper.TABLE, v,
                DatabaseHelper.COL_ID + " = ?",
                new String[]{String.valueOf(c.getId())});
        db.close();
        return rows;
    }

    public boolean excluir(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE,
                DatabaseHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    public List<Chamado> listarTodos() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Chamado> lista = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE, null,
                null, null, null, null,
                DatabaseHelper.COL_ID + " DESC");
        if (cursor.moveToFirst()) {
            do { lista.add(fromCursor(cursor)); }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public List<Chamado> listarComFiltros(String status, String dataInicio, String dataFim) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Chamado> lista = new ArrayList<>();

        StringBuilder where = new StringBuilder();
        List<String> args = new ArrayList<>();

        if (status != null && !status.equals("Todos")) {
            where.append(DatabaseHelper.COL_STATUS).append(" = ?");
            args.add(status);
        }
        if (dataInicio != null && !dataInicio.isEmpty()) {
            if (where.length() > 0) where.append(" AND ");
            where.append(DatabaseHelper.COL_DATA).append(" >= ?");
            args.add(dataInicio);
        }
        if (dataFim != null && !dataFim.isEmpty()) {
            if (where.length() > 0) where.append(" AND ");
            where.append(DatabaseHelper.COL_DATA).append(" <= ?");
            args.add(dataFim);
        }

        String wClause = where.length() > 0 ? where.toString() : null;
        String[] wArgs = args.isEmpty() ? null : args.toArray(new String[0]);

        Cursor cursor = db.query(DatabaseHelper.TABLE, null,
                wClause, wArgs, null, null,
                DatabaseHelper.COL_DATA + " DESC");
        if (cursor.moveToFirst()) {
            do { lista.add(fromCursor(cursor)); }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public Chamado buscarPorId(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE, null,
                DatabaseHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        Chamado c = null;
        if (cursor.moveToFirst()) c = fromCursor(cursor);
        cursor.close();
        db.close();
        return c;
    }

    public List<Chamado> buscarPorTermo(String termo) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Chamado> lista = new ArrayList<>();

        // Pesquisa no Título OU no ID
        String wClause = DatabaseHelper.COL_TITULO + " LIKE ? OR " + DatabaseHelper.COL_ID + " LIKE ?";
        String padrao = "%" + termo + "%";
        String[] wArgs = new String[]{padrao, padrao};

        Cursor cursor = db.query(DatabaseHelper.TABLE, null,
                wClause, wArgs, null, null,
                DatabaseHelper.COL_ID + " DESC");

        if (cursor.moveToFirst()) {
            do { lista.add(fromCursor(cursor)); }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    private Chamado fromCursor(Cursor cursor) {
        Chamado c = new Chamado();
        c.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
        c.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TITULO)));
        c.setData(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATA)));
        c.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRICAO)));
        c.setLocal(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOCAL)));
        c.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIPO)));
        c.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STATUS)));
        c.setSolucao(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SOLUCAO)));
        return c;
    }
}