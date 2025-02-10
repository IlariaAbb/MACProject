package it.unicam.model;
import it.unicam.model.utenti.Ruolo;
import it.unicam.model.utenti.UtenteAutenticato;
import it.unicam.model.util.dtos.*;
import jakarta.persistence.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity 
public class Comune { 

	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comune_generator") 
	private Long idComune; 
	private Coordinate coordinata; 
	private String nome; 
	@OneToOne 
	private UtenteAutenticato curatore; 
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<POI> ValidazionePOI = new ArrayList<>(); 
	@OneToMany(cascade = CascadeType.ALL) 
	private List<POI> PendingPOI = new ArrayList<>(); 
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<Itinerario> itinerari = new ArrayList<>(); 
	@OneToMany(cascade = CascadeType.ALL) 
	private List<Itinerario> itinerariPending = new ArrayList<>(); 
	 
	
	public Comune() { 
	} 

 

	public Comune(String nome, Coordinate coordinata, UtenteAutenticato curatore) { 
		if(curatore.getRuolo().equals(Ruolo.CURATORE)) 
		this.curatore = curatore; 
		else throw new IllegalArgumentException("L'utente non è un curatore"); 
		this.nome = nome; 
		this.coordinata = coordinata; 
		POILuogo comune = new POILuogo(coordinata); 
		comune.insertInfoPOI(nome, "Questo è il punto di interesse logico del Comune di "+nome); 
		this.insertPOI(comune); 
	} 
	
	public Long getIdComune() { 
		return idComune; 
	} 
	
	public Coordinate getCoordinata() { 
		return coordinata; 
	} 

	public void insertPOI(POI p) { 
		this.ValidazionePOI.add(p); 
	} 

	
	public void insertPendingPOI(POI p) { 
		this.PendingPOI.add(p); 
	} 

	public List<POIGI> getAllPOI() { 
		List<POIGI> pois = new ArrayList<>(); 
		for (POI p : 
				this.ValidazionePOI) { 
			pois.add(p.getInfoGeneraliPOI()); 
		} 
		return pois; 
	} 

	
	public boolean inComune(Coordinate coordinata) { 
		String apiUrl = String.format(Locale.US, "https://nominatim.openstreetmap.org/reverse?format=json&latitudine=%.6f&longitudine=%.6f&zoom=10&addressdetails=1", 
				coordinata.getLatitudine(), coordinata.getLongitudine()); 
		try { 
			URL url = new URL(apiUrl); 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
			connection.setRequestMethod("GET"); 
			int responseCode = connection.getResponseCode(); 
			if (responseCode == HttpURLConnection.HTTP_OK) { 
				String jsonResponse = new String(connection.getInputStream().readAllBytes()); 
				return jsonResponse.contains("\""+this.nome+"\""); 
			} 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		return false; 
	} 

	public boolean esistonoPOI(Coordinate c) { 
		for (POI p : this.ValidazionePOI) { 
			if (p.getCoordinata().equals(c)) 
				return true; 
		} 
		for (POI p : this.PendingPOI) { 
			if (p.getCoordinata().equals(c)) 
				return true; 
		} 
		return false; 
	} 

	public POIFD viewSelectPOI(Long IdPOI) { 
		if(this.ValidazionePOI.stream().filter(p -> p.getIdPOI().equals(IdPOI)).findFirst().isEmpty()) 
			return null; 
		else 
			return this.ValidazionePOI.stream().filter(p -> p.getIdPOI().equals(IdPOI)).findFirst().get().getInfoDettagliatePOI(); 
	} 

	public ContenutoFD viewContenuto(Long IdPOI, Long idContenuto) { 
		if(this.ValidazionePOI.stream().filter(p -> p.getIdPOI().equals(IdPOI)).findFirst().isEmpty()) 
			return null; 
		else if(this.ValidazionePOI.stream().filter(p -> p.getIdPOI().equals(IdPOI)).findFirst().get().getContenuti().stream().filter(c -> c.getIdContenuto().equals(idContenuto)).findFirst().isEmpty()) 
			return null; 
		else 
			return this.ValidazionePOI.stream().filter(p -> p.getIdPOI().equals(IdPOI)).findFirst().get().getContenuti().stream().filter(c -> c.getIdContenuto().equals(idContenuto)).findFirst().get().getInfoDettagliateContenuto(); 
	} 

	public POI getPOI(Long i) { 
		return this.ValidazionePOI.stream().filter(p -> p.getIdPOI().equals(i)).findFirst().orElse(null); 
	} 

	public void insertItinerarioPending(Itinerario itinerario) { 
		this.itinerariPending.add(itinerario); 
	} 

	public void insertItinerario(Itinerario itinerario) { 
		this.itinerari.add(itinerario); 
	} 

 

	public ContenutoFD viewContenutoPendingPOI(Long IdPOI, Long idContenuto){ 
		if(this.ValidazionePOI.stream().filter(p -> p.getIdPOI().equals(IdPOI)).findFirst().isEmpty()) 
			return null; 
		else if(this.ValidazionePOI.stream().filter(p -> p.getIdPOI().equals(IdPOI)).findFirst().get().getContenutiPending().stream().filter(c -> c.getIdContenuto().equals(idContenuto)).findFirst().isEmpty()) 
			return null; 
		else 
			return this.ValidazionePOI.stream().filter(p -> p.getIdPOI().equals(IdPOI)).findFirst().get().getContenutiPending().stream().filter(c -> c.getIdContenuto().equals(idContenuto)).findFirst().get().getInfoDettagliateContenuto(); 
	} 

	public List<ItinerarioGI> getAllItinerario() { 
		return this.itinerari.stream().map(itinerario -> itinerario.getInfoGeneraliItinerario()).toList(); 
	} 

	public ItinerarioFD selectItinerario(Long id) { 
		if(this.itinerari.stream().filter(itinerario -> itinerario.getId().equals(id)).findFirst().isEmpty()) 
			return null; 
		else 
			return this.itinerari.stream().filter(itinerario -> itinerario.getId().equals(id)).findFirst().get().getInfoDettagliateItinerario(); 
	} 

	public List<POIGI> getAllPOIPending() { 
		return this.PendingPOI.stream().map(poi -> poi.getInfoGeneraliPOI()).toList(); 
	} 

	public POIFD selectPOIPending(Long i) { 
		if(this.PendingPOI.stream().filter(poi -> poi.getIdPOI().equals(i)).findFirst().isEmpty()) 
			return null; 
		else 
			return this.PendingPOI.stream().filter(poi -> poi.getIdPOI().equals(i)).findFirst().get().getInfoDettagliatePOI(); 
	} 

	public void validazioneSelectPOI(Long IdPOI) { 
		this.insertPOI(this.PendingPOI.stream().filter(poi -> poi.getIdPOI().equals(IdPOI)).findFirst().get()); 
		this.PendingPOI.removeIf(poi -> poi.getIdPOI().equals(IdPOI)); 
	} 

	public void deletePendingPOI(Long IdPOI) { 
		this.PendingPOI.removeIf(poi -> poi.getIdPOI().equals(IdPOI)); 
	} 

	public void deletePOI(Long id) { 
		this.itinerari.stream().forEach(itinerario -> itinerario.getPOIs().removeIf(p -> p.getIdPOI().equals(id))); 
		this.itinerariPending.stream().forEach(itinerario -> itinerario.getPOIs().removeIf(p -> p.getIdPOI().equals(id))); 
		this.ValidazionePOI.removeIf(poi -> poi.getIdPOI().equals(id)); 
	} 

	public List<Long> notItinerario(){ 
		return this.itinerari.stream().filter(i -> i.getPOIs().size() < 2).map(i -> i.getId()).toList(); 
	} 

	public void deleteNotItinerario(){ 
		this.itinerari.removeIf(i -> i.getPOIs().size() < 2); 
		this.itinerariPending.removeIf(i -> i.getPOIs().size() < 2); 
	} 

	public void deleteItinerario(Long id) { 
		this.itinerari.removeIf(itinerario -> itinerario.getId().equals(id)); 
	} 

	public void deleteContenuto(Long IdPOI, Long idContenuto) { 
		this.ValidazionePOI.stream().filter(poi -> poi.getIdPOI().equals(IdPOI)).findFirst().get().deleteContenuto(idContenuto); 
	} 

	public List<POIGI> getAllContenutoPendingPOI() { 
		return this.ValidazionePOI.stream().filter(p -> !p.getContenutiPending().isEmpty()).map(p->p.getInfoGeneraliPOI()).toList(); 
	} 

	public ContenutoFD selectContenutoPending(Long IdPOI, Long idContenuto) { 
		return this.ValidazionePOI.stream().filter(p -> p.getIdPOI().equals(IdPOI)).findFirst().get() 
				.getContenutiPending().stream().filter(c -> c.getIdContenuto().equals(idContenuto)).findFirst().get().getInfoDettagliateContenuto(); 
	} 

	public void deleteContenutoPending(Long IdPOI, Long idContenuto) { 
		POI p = this.ValidazionePOI.stream().filter(poi -> poi.getIdPOI().equals(IdPOI)).findFirst().get(); 
		p.deleteContenutoPending(idContenuto); 
	} 

	public void validazioneSelectContenuto(Long IdPOI, Long idContenuto) { 
		this.ValidazionePOI.stream().filter(poi -> poi.getIdPOI().equals(IdPOI)).findFirst().get().validazioneContenuto(idContenuto);
	} 

	public List<ItinerarioGI> getAllItinerarioPending() { 
		List<ItinerarioGI> is = new ArrayList<>(); 
		for(Itinerario i:this.itinerariPending){ 
			is.add(i.getInfoGeneraliItinerario()); 
		} 
		return is; 
	} 

	public ItinerarioFD selectItinerarioPending(Long id) { 
		if(!this.itinerariPending.stream().filter(itinerario -> itinerario.getId().equals(id)).findFirst().isEmpty()) 
			return this.itinerariPending.stream().filter(itinerario -> itinerario.getId().equals(id)).findFirst().get().getInfoDettagliateItinerario(); 
		else 
			return null; 
	} 

	public void validazioneSelectItinerario(Long idItinerario) { 
		this.itinerari.add(this.itinerariPending.stream().filter(itinerario -> itinerario.getId().equals(idItinerario)).findFirst().get()); 
		this.itinerariPending.removeIf(itinerario -> itinerario.getId().equals(idItinerario)); 
	} 

	public void deleteItinerarioPending(Long id) { 
		this.itinerariPending.removeIf(itinerario -> itinerario.getId().equals(id)); 
	} 

	public Itinerario getItinerario(Long idItinerario) { 
		return this.itinerari.stream().filter(i -> i.getId().equals(idItinerario)).findFirst().orElse(null); 
	} 

	public List<POI> eventiPOIConclusi(){ 
		return this.ValidazionePOI.stream().filter(p -> p instanceof POIEvento e && e.getDataChiusura().isBefore(LocalDateTime.now())).toList(); 
	} 

	public List <Itinerario> itinerariConclusi(){ 
		return this.itinerari.stream().filter(i -> i.getDataChiusura() != null && i.getDataChiusura().isBefore(LocalDateTime.now())).toList(); 
	} 

} 