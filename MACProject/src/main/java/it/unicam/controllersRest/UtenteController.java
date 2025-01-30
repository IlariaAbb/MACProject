package it.unicam.controllersRest;

import it.unicam.model.preferiti.GestionePreferiti;
import it.unicam.model.utenti.GestioneRuolo;
import it.unicam.model.utenti.GestioneUtentiAutenticati;
import it.unicam.model.controllersGRASP.RegistrazioneController;
import it.unicam.model.utenti.Ruolo;
import it.unicam.model.util.dtos.UtenteAutenticatoGI;
import it.unicam.repositories.ComuneRepository;
import it.unicam.repositories.UtenteAutenticatoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/utente") 
public class UtenteController { 
	@Autowired 
	private UtenteAutenticatoRepository utenteAutenticatoRepository; 
	@Autowired 
	private GestionePreferiti gestionePreferiti; 
	@Autowired 
	private GestioneRuolo gestioneRuolo; 
	@Autowired 
	private GestioneUtentiAutenticati gestioneUtentiAutenticati; 
	@Autowired 
	private RegistrazioneController registrazioneController; 
	@Autowired 
	private PasswordEncoder PasswordEncoder; 
	@Autowired 
	ComuneRepository comuneRepository; 
	

	@PostMapping("/atourist/addPOIAiPreferiti") 
	public ResponseEntity<Object> addPOIAiPreferiti(Authentication authentication, @RequestParam("IdPOI") Long IdPOI, @RequestParam("idComune") Long idComune) { 
		if(comuneRepository.findById(idComune).get().getPOI(IdPOI) == null){ 
			return new ResponseEntity<>("POI non presente nel comune", HttpStatus.BAD_REQUEST); 
		} 
		Long id = this.utenteAutenticatoRepository.findByUsername(authentication.getName()).getId(); 
		if (this.gestionePreferiti.addPOIAiPreferiti(id, IdPOI, idComune)) { 
			return new ResponseEntity<>("ok", HttpStatus.OK); 
		}else { 
			return new ResponseEntity<>("POI già presente tra i preferiti", HttpStatus.BAD_REQUEST); 
		} 
	} 

	@PostMapping("/atourist/addItinerarioAiPreferiti") 
	public ResponseEntity<Object> addItinerarioAiPreferiti(Authentication authentication, @RequestParam("idItinerario") Long idItinerario, @RequestParam("idComune") Long idComune) { 
		if(comuneRepository.findById(idComune).get().getItinerario(idItinerario) == null){ 
			return new ResponseEntity<>("Itinerario non presente nel comune", HttpStatus.BAD_REQUEST); 
		} 
		Long id = this.utenteAutenticatoRepository.findByUsername(authentication.getName()).getId(); 
		if (this.gestionePreferiti.addItinerarioAiPreferiti(id, idItinerario, idComune)) { 
			return new ResponseEntity<>("ok", HttpStatus.OK); 
		}else { 
			return new ResponseEntity<>("Itinerario già presente tra i preferiti o inesistente", HttpStatus.BAD_REQUEST); 
		} 
	} 

	@GetMapping("/atourist/viewPOIPreferiti") 
	public ResponseEntity<Object> viewPOIPreferiti(Authentication authentication) { 
		Long id = this.utenteAutenticatoRepository.findByUsername(authentication.getName()).getId(); 
		return new ResponseEntity<>(this.gestionePreferiti.getAllPOIPreferiti(id), HttpStatus.OK); 
	} 

	@GetMapping("/atourist/viewItinerariPreferiti") 
	public ResponseEntity<Object> viewItinerariPreferiti(Authentication authentication) { 
		Long id = this.utenteAutenticatoRepository.findByUsername(authentication.getName()).getId(); 
		return new ResponseEntity<>(this.gestionePreferiti.getAllItinerariPreferiti(id), HttpStatus.OK); 
	} 

	@PostMapping("/atourist/richiestaCambioRuolo") 
	public ResponseEntity<Object> richiestaCambioRuolo(Authentication authentication) { 
		Long id = this.utenteAutenticatoRepository.findByUsername(authentication.getName()).getId(); 
		if(this.gestioneRuolo.viewRichiesteCambioRuolo().stream().filter(x -> x.getId().equals(id)).count() > 0){ 
			return new ResponseEntity<>("Richiesta già inviata", HttpStatus.BAD_REQUEST); 
		} 
		this.gestioneRuolo.richiestaCambioRuolo(id); 
		return new ResponseEntity<>("ok", HttpStatus.OK); 
	} 

	@GetMapping("/gestore/viewRichiesteCambioRuolo") 
	public ResponseEntity<Object> viewRichiesteCambioRuolo() { 
		return new ResponseEntity<>(this.gestioneRuolo.viewRichiesteCambioRuolo(), HttpStatus.OK); 
	} 
 
	@DeleteMapping("/gestore/disapprovazioneRichiesta") 
	public ResponseEntity<Object> disapprovazioneRichiesta(@RequestParam("id") Long id) { 
		this.gestioneRuolo.disapprovazioneRichiesta(id); 
		return new ResponseEntity<>("ok", HttpStatus.OK); 
	} 

	@PutMapping("/gestore/approvazioneRichiesta") 
	public ResponseEntity<Object> approvazioneRichiesta(@RequestParam("id") Long id) { 
		this.gestioneRuolo.approvazioneRichiesta(id); 
		return new ResponseEntity<>("ok", HttpStatus.OK); 
	} 

	@GetMapping("/gestore/viewAllUtenti") 
	public ResponseEntity<Object> viewAllUtenti() { 
		return new ResponseEntity<>(this.gestioneUtentiAutenticati.viewAllUtenti(), HttpStatus.OK); 
	} 

	@PutMapping ("/gestore/cambioRuolo") 
	public ResponseEntity<Object> cambioRuolo(@RequestParam("id") Long id, @RequestParam("Ruolo") Ruolo ruolo) { 
		if(ruolo.equals(Ruolo.GESTORE) || ruolo.equals(Ruolo.CURATORE)){ 
			return new ResponseEntity<>("Ruolo non assegnabile", HttpStatus.BAD_REQUEST); 
		} 
		this.gestioneUtentiAutenticati.cambiaRuolo(id, ruolo); 
		return new ResponseEntity<>("ok", HttpStatus.OK); 
	} 

	@PostMapping("/registrazioneUtente") 
	public ResponseEntity<Object> registrazioneUtente(@Valid @RequestBody UtenteAutenticatoGI utente) { 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
		if (!(auth instanceof AnonymousAuthenticationToken)) { 
			return new ResponseEntity<>("Utente già autenticato o ruolo non disponibile alla registrazione", HttpStatus.BAD_REQUEST); 
		} 
		if (this.gestioneUtentiAutenticati.contieneUtenti(utente.getEmail(), utente.getUsername())) { 
			return new ResponseEntity<>("Utentenome e/o email già utilizzate", HttpStatus.BAD_REQUEST); 
		} 
		this.registrazioneController.registrazioneUtente(utente.getEmail(), utente.getUsername(), PasswordEncoder.encode(utente.password()), utente.getRuolo()); 
		return new ResponseEntity<>("ok", HttpStatus.OK); 
	} 

	@GetMapping("/gestore/viewRegistrazioneUtente") 
	public ResponseEntity<Object> viewRegistrazioneUtente() { 
		return new ResponseEntity<>(this.gestioneUtentiAutenticati.viewRegistrazioneUtenti(), HttpStatus.OK); 
	} 

	@DeleteMapping("/gestore/registrazioneRespinta") 
	public ResponseEntity<Object> registrazioneRespinta(@RequestParam("id") Long id) { 
		this.registrazioneController.registrazioneRespinta(id); 
		return new ResponseEntity<>("ok", HttpStatus.OK); 
	} 

	@PutMapping("/gestore/approvazioneRegistrazione") 
	public ResponseEntity<Object> approvazioneRegistrazione(@RequestParam("id") Long id) { 
		this.registrazioneController.approvazioneRegistrazione(id); 
		return new ResponseEntity<>("ok", HttpStatus.OK); 
	} 

} 

 