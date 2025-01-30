package it.unicam.model.util.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ContenutoFD {
    private Long id;
    @NotNull
    @NotBlank
    private String nome;
    @NotNull
    @NotBlank
    private String descrizione;
    private byte[] file;


    public ContenutoFD(){

    }
    public ContenutoFD(Long id, String nome, String descrizione, byte[] file){
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getNome() {
        return nome;
    }

    public byte[] getFile() {
        return this.file;
    }

    @Override
    public String toString() {
        return "Id = " + this.id + " Nome= " + nome +
                "\nDescrizione= " + descrizione;
    }

    public void addFile(byte[] file) {
        this.file = file;
    }
}
