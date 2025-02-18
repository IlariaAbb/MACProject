package it.unicam.model.util.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


public class UpdateItinerarioFD extends ItinerarioFD {

    @NotNull
    private Long idOriginalItinerario;

    public UpdateItinerarioFD(){
        super(null, null, null, null, null, null);
    }

    public Long getIdOriginalItinerario() {
        return idOriginalItinerario;
    }

    public void setIdOriginalItinerario(Long idOriginalItinerario) {
        this.idOriginalItinerario = idOriginalItinerario;
    }

    @Override
    public String toString() {
        return "UpdateItinerarioFD{" +
                "idOriginalItinerario=" + idOriginalItinerario +
                ", nome=" + this.getNome() +
                '}';
    }
}
