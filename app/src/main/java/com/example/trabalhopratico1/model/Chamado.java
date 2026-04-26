package com.example.trabalhopratico1.model;


public class Chamado {
    private int id;
    private String titulo;
    private String data;
    private String descricao;
    private String local;
    private String tipo;
    private String status;
    private String solucao;

    public Chamado() {}

    public Chamado(String titulo, String data, String descricao,
                   String local, String tipo, String status, String solucao) {
        this.titulo = titulo;
        this.data = data;
        this.descricao = descricao;
        this.local = local;
        this.tipo = tipo;
        this.status = status;
        this.solucao = solucao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSolucao() { return solucao; }
    public void setSolucao(String solucao) { this.solucao = solucao; }
}