package model;

public class Cliente {
    private int id;
    private String nome;
    private String telefone;
    private String nomePet;
    private String raca;

    public Cliente(int id, String nome, String telefone, String nomePet, String raca) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.nomePet = nomePet;
        this.raca = raca;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getNomePet() {
        return nomePet;
    }

    public String getRaca() {
        return raca;
    }

    // Se quiser setters também, adicione aqui

    public String getPet() {
    return nomePet;
}}

