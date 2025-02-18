package it.unicam.model.util.dtos;

import it.unicam.model.Coordinate;
import it.unicam.model.Tipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


public class UpdatePOIFD extends POIFD {

    @NotNull
    private Long idOriginalPOI;

    public UpdatePOIFD() {
        super();
    }

    public Long getIdOriginalPOI() {
        return idOriginalPOI;
    }

    public void setIdOriginalPOI(Long idOriginalPOI) {
        this.idOriginalPOI = idOriginalPOI;
    }

    @Override
    public String toString() {
        return "UpdatePOIFD{idOriginalPOI=" + idOriginalPOI +
                ", nome='" + this.getNome() + '\'' +
                ", descr='" + this.getDescrizione() + '\'' +
                '}';
    }
}
