package it.unicam.model.util.dtos;

import it.unicam.model.Coordinate;
import it.unicam.model.Tipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class POIFD { 
	private Long id; 
	@NotNull 
	@NotBlank 
	private String nome; 
	@NotNull 
	@NotBlank 
	private String descrizione; 
	@NotNull 
	private Tipo tipo; 
	@NotNull 
	private Coordinate coordinate; 
	private LocalTime[] oraApertura; 
	private LocalTime[] oraChiusura; 
	private LocalDateTime dataApertura; 
	private LocalDateTime dataChiusura; 
	private List<ContenutoGI> contenutiGI; 
	private List<ContenutoGI> contenutiPendingGI; 

	public POIFD(){ 
	} 

	public POIFD(Long id, String nome, String descrizione, Coordinate coordinata, Tipo tipo, List<ContenutoGI> contenutiGI, List<ContenutoGI> contenutiPendingGI) { 
		this.id = id; 
		this.nome = nome; 
		this.descrizione = descrizione; 
		this.coordinate = coordinata; 
		this.tipo = tipo; 
		this.contenutiGI = contenutiGI; 
		this.contenutiPendingGI = contenutiPendingGI; 
		this.oraApertura = null; 
		this.oraChiusura = null; 
		this.dataApertura = null; 
		this.dataChiusura = null; 
	} 

	public POIFD(Long id, String nome, String descrizione, Coordinate coordinata, Tipo tipo, LocalTime[] oraApertura, LocalTime[] oraChiusura, List<ContenutoGI> contenutiGI, List<ContenutoGI> contenutiPendingGI) { 
		this.id = id; 
		this.nome = nome; 
		this.descrizione = descrizione; 
		this.coordinate = coordinata; 
		this.tipo = tipo; 
		this.contenutiGI = contenutiGI; 
		this.oraApertura = oraApertura; 
		this.oraChiusura = oraChiusura; 
		this.contenutiPendingGI = contenutiPendingGI; 
		this.dataApertura = null; 
		this.dataChiusura = null; 
	} 

	public POIFD(Long id, String nome, String descrizione, Coordinate coordinata, Tipo tipo, LocalDateTime dataApertura, LocalDateTime dataChiusura, List<ContenutoGI> contenutiGI, List<ContenutoGI> contenutiPendingGI) { 
		this.id = id; 
		this.nome = nome; 
		this.descrizione = descrizione; 
		this.coordinate = coordinata; 
		this.tipo = tipo; 
		this.contenutiGI = contenutiGI; 
		this.contenutiPendingGI = contenutiPendingGI; 
		this.oraApertura = null; 
		this.oraChiusura = null; 
		this.dataApertura = dataApertura; 
		this.dataChiusura = dataChiusura; 
	} 

	public Long getId() { 
		return id; 
	} 

	public Tipo getTipo() { 
		return tipo; 
	} 

	public LocalTime[] getOraApertura() { 
		return oraApertura; 
	} 

	public LocalTime[] getOraChiusura() { 
		return oraChiusura; 
	} 

	public LocalDateTime getDataApertura() { 
		return dataApertura; 
	} 

	public LocalDateTime getDataChiusura() { 
		return dataChiusura; 
	} 

	public String getNome() { 
		return nome; 
	} 

	public String getDescrizione() { 
		return descrizione; 
	} 

	public Coordinate getCoordinate() { 
		return coordinate; 
	} 

	public List<ContenutoGI> getContenutiGI() { 
		return contenutiGI; 
	} 

	public List<ContenutoGI> getContenutiPendingGI() { 
		return contenutiPendingGI; 
	} 

	@Override 
	public String toString() { 
		List<String> cont = this.getContenutiGI().stream().map(c -> c.toString()).toList(); 
		return switch(tipo){ 
		case LUOGO -> "Id: "+this.id+" Nome: "+this.nome+"\nDescrizione: "+this.descrizione+"\nTipologia: "+this.tipo+"\nCoordinate: lat " 
				+this.coordinate.getLatitudine()+" lon "+this.coordinate.getLongitudine()+"\n"+"Contenuti:\n" 
				+ cont.stream().collect(Collectors.joining("\n")); 
		case LUOGOCONORA -> "Id: "+this.id+" Nome: "+this.nome+"\nDescrizione: "+this.descrizione+"\nTipologia: "+this.tipo+"\nCoordinate: lat " 
			+this.coordinate.getLatitudine()+" lon "+this.coordinate.getLongitudine()+"\nOrari:"+ 
			"\nLunedì: "+this.oraApertura[0]+" - "+this.oraChiusura[0]+"\nMartedì: "+this.oraApertura[1]+" - "+this.oraChiusura[1]+ 
			"\nMercoledì: "+this.oraApertura[2]+" - "+this.oraChiusura[2]+"\nGiovedì: "+this.oraApertura[3]+" - "+this.oraChiusura[3]+ 
			"\nVenerdì: "+this.oraApertura[4]+" - "+this.oraChiusura[4]+"\nSabato: "+this.oraApertura[5]+" - "+this.oraChiusura[5]+ 
			"\nDomenica: "+this.oraApertura[6]+" - "+this.oraChiusura[6]+ 
			"\nContenuti:\n" + cont.stream().collect(Collectors.joining("\n")); 
		case EVENTO -> "Id: "+this.id+" Nome: "+this.nome+"\nDescrizione: "+this.descrizione+"\nTipologia: "+this.tipo+"\nCoordinate: lat " 
			+this.coordinate.getLatitudine()+" lon "+this.coordinate.getLongitudine()+"\n"+ 
			"Inizio evento: "+this.dataApertura.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + " - chiusura evento: " 
			+this.dataChiusura.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))+ 
			"\nContenuti:\n" + cont.stream().collect(Collectors.joining("\n")); 
		}; 
	} 

} 
