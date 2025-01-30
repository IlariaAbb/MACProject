package it.unicam.model.util.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ContestGI {

    private Long idContest;
    @NotNull
    @NotBlank
    private String nome;
    @NotNull
    @NotBlank
    private String obj;
    @NotNull
    private boolean suInvito;
    @NotNull
    private boolean eChiuso;

    public ContestGI(Long idContest, String nome, String obj, boolean suInvito, boolean eChiuso) {
        this.idContest = idContest;
        this.nome = nome;
        this.obj = obj;
        this.suInvito = suInvito;
        this.eChiuso = eChiuso;
    }

    public ContestGI() {
    }

    public boolean eSuInvito() {
        return suInvito;
    }

    public Long getIdContest() {
        return idContest;
    }

    public String getNome() {
        return nome;
    }

    public String getObj() {
        return obj;
    }

    public boolean eChiuso() {
        return eChiuso;
    }

    public void eSuInvito(boolean suInvito) {
        this.suInvito = suInvito;
    }


    @Override
    public String toString() {
        return "Contest " +
                "Id = " + idContest +
                " - nome=" + nome +
                " - obj = " + obj +
                " - eChiuso = " + eChiuso;
    }
}
