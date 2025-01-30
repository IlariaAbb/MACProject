package it.unicam.model;

import it.unicam.model.util.dtos.ContenutoFD;
import it.unicam.model.util.dtos.ContenutoGI;
import jakarta.persistence.*;

@Entity
public class Contenuto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_generator")
    private Long idContenuto;
    private String nome;
    private String descrizione;
    @Lob
    private byte[] file;


    public Contenuto(String Nome, String Descrizione, byte[] file){
        if (Nome == null || Descrizione == null || file == null){
            throw new NullPointerException();
        }
        this.nome = Nome;
        this.descrizione = Descrizione;
        this.file = file;
    }

    public Contenuto() {
    }


    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public byte[] getFile() {
        return file;
    }

    public Long getIdContenuto() {
        return this.idContenuto;
    }


    public ContenutoGI getInfoGeneraliContenuto(){
        return new ContenutoGI(this.getIdContenuto(), this.getNome(), this.getDescrizione());
    }

    public ContenutoFD getInfoDettagliateContenuto(){
        return new ContenutoFD(this.getIdContenuto(), this.getNome(), this.getDescrizione(), this.getFile());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != this.getClass()) return false;
        Contenuto c = (Contenuto) obj;
        return c.getIdContenuto().equals(this.getIdContenuto());
    }
}
