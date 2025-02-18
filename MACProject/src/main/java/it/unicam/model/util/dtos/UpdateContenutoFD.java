package it.unicam.model.util.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateContenutoFD extends ContenutoFD {

    @NotNull
    private Long idOriginalContenuto;

    public Long getIdOriginalContenuto() {
        return idOriginalContenuto;
    }

    public void setIdOriginalContenuto(Long idOriginalContenuto) {
        this.idOriginalContenuto = idOriginalContenuto;
    }

    @Override
    public String toString() {
        return "UpdateContenutoFD{" +
                "idOriginalContenuto=" + idOriginalContenuto +
                ", nome=" + this.getNome() +
                '}';
    }
}
