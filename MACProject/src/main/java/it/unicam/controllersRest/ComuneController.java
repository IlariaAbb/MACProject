package it.unicam.controllersRest;

import it.unicam.model.*;
import it.unicam.model.controllersGRASP.*;
import it.unicam.model.preferiti.GestionePreferiti;
import it.unicam.model.utenti.Ruolo;
import it.unicam.model.utenti.UtenteAutenticato;
import it.unicam.model.utenti.GestioneUtentiAutenticati;
import it.unicam.model.util.dtos.ComuneGI;
import it.unicam.model.util.dtos.ContenutoFD;
import it.unicam.model.util.dtos.ItinerarioFD;
import it.unicam.model.util.dtos.POIFD;
import it.unicam.repositories.*;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

	@RestController 
	@RequestMapping("/comune") 
	public class ComuneController { 
		@Autowired 
		private ComuneRepository comuneRepository; 
		@Autowired 
		private ContenutoRepository contenutoRepository; 
		@Autowired 
		private POIRepository poiRepository; 
		@Autowired 
		private ItinerarioRepository itinerarioRepository; 
		@Autowired 
		private POIController poiController; 
		@Autowired 
		private ItinerarioController itinerarioController; 
		@Autowired 
		private ContenutoController contenutoController; 
		@Autowired 
		private ViewController viewController; 
		@Autowired 
		private GestionePreferiti gestionePreferiti; 
		@Autowired 
		private UtenteAutenticatoRepository utenteAutenticatoRepository; 
		@Autowired 
		private GestioneUtentiAutenticati gestioneUtentiAutenticati; 
		@Autowired 
		private PasswordEncoder PasswordEncoder; 

		@PostMapping("/gestore/addComune") 
		public ResponseEntity<Object> addComune(@Valid @RequestBody ComuneGI c){ 
			UtenteAutenticato u = new UtenteAutenticato(c.getCuratore().getUsername(), PasswordEncoder.encode(c.getCuratore().password()), c.getCuratore().getEmail(), Ruolo.CURATORE); 
			if(this.gestioneUtentiAutenticati.contieneUtenti(u.getEmail(), u.getUsername())) 
				return new ResponseEntity<>("Errore: Curatore già presente", HttpStatus.BAD_REQUEST); 
			if(this.comuneRepository.findByNome(c.getNome()) != null) 
				return new ResponseEntity<>("Errore: Comune già presente", HttpStatus.BAD_REQUEST); 
			this.gestioneUtentiAutenticati.addUtente(u); 
			this.utenteAutenticatoRepository.save(u); 
			Comune comune = new Comune(c.getNome(), c.getCoordinate(), u); 
			comuneRepository.save(comune); 
			return new ResponseEntity<>("ok", HttpStatus.OK); 
		} 
 
		@GetMapping("/getAllPOI") 
		public ResponseEntity<Object> getAllPOI(@RequestParam("idComune") Long id){ 
			if(this.comuneRepository.findById(id).isEmpty()) 
				return new ResponseEntity<>("Errore: Comune non trovato", HttpStatus.BAD_REQUEST); 
			else 
				return new ResponseEntity<>(this.comuneRepository.findById(id).get().getAllPOI(), HttpStatus.OK); 
		} 

		@GetMapping("/viewSelectPOI") 
		public ResponseEntity<Object> viewSelectPOI(@RequestParam("idComune") Long idComune, @RequestParam("idPOI") Long idPOI){ 
			if(viewController.viewSelectPOI(idComune, idPOI) == null) 
				return new ResponseEntity<>("Errore: POI non trovato", HttpStatus.BAD_REQUEST); 
			else 
				return new ResponseEntity<>(viewController.viewSelectPOI(idComune, idPOI), HttpStatus.OK); 
		} 

		@GetMapping("/viewContenuto") 
		public ResponseEntity<Object> viewContenuto(@RequestParam("idComune") Long idComune, @RequestParam("idPOI") Long poiID, @RequestParam("idContenuto") Long idContenuto){ 
			if (viewController.viewContenuto(idComune, poiID, idContenuto) == null) 
				return new ResponseEntity<>("Errore: Contenuto non trovato", HttpStatus.BAD_REQUEST); 
			else 
				return new ResponseEntity<>(viewController.viewContenuto(idComune, poiID, idContenuto), HttpStatus.OK); 
		} 

		@PostMapping("/acontributor/insertPOI") 
		public ResponseEntity<Object> insertPOI(@RequestParam("idComune") Long idComune, @Valid @RequestPart("poi") POIFD p) { 
			POIFactory pf; 
			switch (p.getTipo()){ 
				case Tipo.LUOGO -> pf = new POILuogoFactory(); 
				case Tipo.EVENTO -> pf = new POIEventoFactory(); 
				case Tipo.LUOGOCONORA -> pf = new POILuogoConOraFactory(); 
				default -> { 
					return new ResponseEntity<>("Errore: Tipo errato", HttpStatus.BAD_REQUEST); 
				} 
			} 
			if(!poiController.selectPunto(idComune, p.getCoordinate())) 
				return new ResponseEntity<>("Errore: Punto già presente o esterno al comune", HttpStatus.BAD_REQUEST); 
			poiController.insertPOI(idComune, pf, p); 
			return new ResponseEntity<>("ok", HttpStatus.OK); 
		} 

		@PostMapping("/contributor/insertPendingPOI") 
		public ResponseEntity<Object> insertPendingPOI(@RequestParam("idComune") Long idComune, @Valid @RequestPart("poi") POIFD p) { 
			POIFactory pf; 
			switch (p.getTipo()){ 
				case Tipo.LUOGO -> pf = new POILuogoFactory(); 
				case Tipo.EVENTO -> pf = new POIEventoFactory(); 
				case Tipo.LUOGOCONORA -> pf = new POILuogoConOraFactory(); 
				default -> { 
					return new ResponseEntity<>("Errore: Tipo errato", HttpStatus.BAD_REQUEST); 
				} 
			} 
			if(!poiController.selectPunto(idComune, p.getCoordinate())) 
				return new ResponseEntity<>("Errore: Punto già presente o esterno al comune", HttpStatus.BAD_REQUEST); 
			poiController.insertPendingPOI(idComune, pf, p); 
			return new ResponseEntity<>("ok", HttpStatus.OK); 
		} 
 
		@PostMapping("/acontributor/creaItinerario") 
		public ResponseEntity<Object> creaItinerario(@RequestParam("idComune") Long idComune, @Valid @RequestPart("itinerario") ItinerarioFD i, @RequestParam("pois") Long [] pois) { 
			if(pois.length < 2) 
				return new ResponseEntity<>("Errore: Itinerario deve contenere almeno 2 POI", HttpStatus.BAD_REQUEST); 
			for (Long poi : pois) { 
				if (viewController.viewSelectPOI(idComune, poi) == null) 
					return new ResponseEntity<>("Errore: POI inserito non trovato", HttpStatus.BAD_REQUEST); 
			} 
			itinerarioController.creaItinerario(idComune, i, pois); 
			return new ResponseEntity<>("ok", HttpStatus.OK); 
		} 

		@PostMapping("/contributor/creaItinerarioPending") 
		public ResponseEntity<Object> creaItinerarioPending(@RequestParam("idComune") Long idComune, @Valid @RequestPart("itinerario") ItinerarioFD i, @RequestParam("pois") Long [] pois) { 
			if(pois.length < 2) 
				return new ResponseEntity<>("Errore: Itinerario deve contenere almeno 2 POI", HttpStatus.BAD_REQUEST); 
			for (Long poi : pois) { 
				if (viewController.viewSelectPOI(idComune, poi) == null) 
					return new ResponseEntity<>("Errore: POI inserito non trovato", HttpStatus.BAD_REQUEST); 
			} 
			itinerarioController.creaItinerarioPending(idComune, i, pois); 
			return new ResponseEntity<>("ok", HttpStatus.OK); 
		} 

		@GetMapping("/getAllItinerario") 
		public ResponseEntity<Object> getAllItinerario(@RequestParam("idComune") Long id){ 
			if (this.comuneRepository.findById(id).isEmpty()) 
				return new ResponseEntity<>("Errore: Comune non trovato", HttpStatus.BAD_REQUEST); 
			else 
				return new ResponseEntity<>(this.comuneRepository.findById(id).get().getAllItinerario(), HttpStatus.OK); 
		} 

		@GetMapping("/viewItinerario") 
		public ResponseEntity<Object> viewItinerario(@RequestParam("idComune") Long idComune, @RequestParam("idItinerario") Long idItinerario){ 
			if(viewController.selectItinerario(idComune, idItinerario) == null) 
				return new ResponseEntity<>("Errore: Itinerario non trovato", HttpStatus.BAD_REQUEST); 
			else 
				return new ResponseEntity<>(viewController.selectItinerario(idComune, idItinerario), HttpStatus.OK); 
		} 

		@GetMapping("/curatore/getAllPOIPending") 
		public ResponseEntity<Object> getAllPOIPending(Authentication authentication){ 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if (c == null) 
				return new ResponseEntity<>("Errore: Comune non trovato", HttpStatus.BAD_REQUEST); 
			else 
				return new ResponseEntity<>(c.getAllPOIPending(), HttpStatus.OK); 
		} 

		@GetMapping("/curatore/viewPendingPOI") 
		public ResponseEntity<Object> viewPendingPOI(Authentication authentication, @RequestParam("id") Long id){ 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if(c == null) 
				return new ResponseEntity<>("Errore: Comune non trovato", HttpStatus.BAD_REQUEST); 
			POIFD p = viewController.selectPOIPending(c.getIdComune(), id); 
			if(p == null) 
				return new ResponseEntity<>("Errore: POI non trovato tra i POI in pending", HttpStatus.BAD_REQUEST); 
			else 
				return new ResponseEntity<>(p, HttpStatus.OK); 
		} 

		@PutMapping("/curatore/validazioneSelectPOI") 
		public ResponseEntity<Object> validazioneSelectPOI(Authentication authentication, @RequestParam("id") Long id) { 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if(c == null) 
				return new ResponseEntity<>("Errore: Comune non trovato", HttpStatus.BAD_REQUEST); 
			POIFD p = viewController.selectPOIPending(c.getIdComune(), id); 
			if(p == null) 
				return new ResponseEntity<>("Errore: POI non trovato tra i POI in pending", HttpStatus.BAD_REQUEST); 
			else{ 
				c.validazioneSelectPOI(id); 
				this.comuneRepository.save(c); 
				return new ResponseEntity<>("ok", HttpStatus.OK); 
			} 
		} 

		@DeleteMapping("/curatore/deletePOIPending") 
		public ResponseEntity<Object> deletePOIPending(Authentication authentication, @RequestParam("id") Long id) { 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if(c == null) 
				return new ResponseEntity<>("Errore: Comune non trovato", HttpStatus.BAD_REQUEST); 
			POIFD p = viewController.selectPOIPending(c.getIdComune(), id); 
			if(p == null) 
				return new ResponseEntity<>("Errore: POI non trovato tra i POI in pending", HttpStatus.BAD_REQUEST); 
			else{ 
				c.deletePendingPOI(id); 
				this.comuneRepository.save(c); 
				this.poiRepository.deleteById(id); 
				return new ResponseEntity<>("ok", HttpStatus.OK); 
			} 
		} 

		@GetMapping("/curatore/viewContenutoPending") 
		public ResponseEntity<Object> viewContenutoPending(Authentication authentication, @RequestParam("idPOI") Long idPOI, @RequestParam("idContenuto") Long idContenuto){ 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if(viewController.viewContenutoPendingPOI(c.getIdComune(), idPOI, idContenuto) == null) 
				return new ResponseEntity<>("Errore: Contenuto non trovato tra i contenuti in pending", HttpStatus.BAD_REQUEST); 
			else 
				return new ResponseEntity<>(viewController.viewContenutoPendingPOI(c.getIdComune(), idPOI, idContenuto), HttpStatus.OK); 
		} 

		@PostMapping("/acontributor/insertContenutoAPOI") 
		public ResponseEntity<Object> insertContenutoAPOI(@RequestParam("idComune") Long idComune, @RequestParam("idPOI") Long id, @Valid @RequestPart("contenuto") ContenutoFD c, @RequestPart("file") MultipartFile file) { 
			try { 
				c.addFile(file.getBytes()); 
			} catch (IOException e) { 
				throw new RuntimeException(e); 
			} 
			if(viewController.viewSelectPOI(idComune, id) == null) 
				return new ResponseEntity<>("Errore: POI non trovato", HttpStatus.BAD_REQUEST); 
			contenutoController.insertContenutoPOI(idComune, id, c); 
			return new ResponseEntity<>("ok", HttpStatus.OK); 
		} 

		@PostMapping("/contributor/insertContenutoPendingAPOI") 
		public ResponseEntity<Object> insertContenutoPendingAPOI(@RequestParam("idComune") Long idComune, @RequestParam("idPOI") Long id, @Valid @RequestPart("contenuto") ContenutoFD c, @RequestPart("file") MultipartFile file) { 
			try { 
				c.addFile(file.getBytes()); 
			} catch (IOException e) { 
				throw new RuntimeException(e); 
			} 
			if(viewController.viewSelectPOI(idComune, id) == null) 
				return new ResponseEntity<>("Errore: POI non trovato", HttpStatus.BAD_REQUEST); 
				contenutoController.insertContenutoPendingPOI(idComune, id, c); 
				return new ResponseEntity<>("ok", HttpStatus.OK); 
		}
		
		@DeleteMapping("/curatore/deletePOI") 
		public ResponseEntity<Object> deletePOI(Authentication authentication, @RequestParam("id") Long id) { 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if (viewController.viewSelectPOI(c.getIdComune(), id) == null) 
				return new ResponseEntity<>("Errore: POI non trovato", HttpStatus.BAD_REQUEST); 
			this.gestionePreferiti.deletePOI(id); 
			c.deletePOI(id); 
			for(Long i : c.notItinerario()){ 
				this.gestionePreferiti.deleteItinerario(i); 
			} 
			c.deleteNotItinerario(); 
			this.comuneRepository.save(c); 
			this.itinerarioRepository.findAll().forEach(i -> { 
				if(i.getPOIs().size() < 2) 
					this.itinerarioRepository.deleteById(i.getId()); 
			}); 
			return new ResponseEntity<>("ok", HttpStatus.OK); 
		} 

		@DeleteMapping("/curatore/deleteItinerario") 
		public ResponseEntity<Object> deleteItinerario(Authentication authentication, @RequestParam("id") Long id) { 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if (viewController.selectItinerario(c.getIdComune(), id) == null) 
				return new ResponseEntity<>("Errore: Itinerario non trovato", HttpStatus.BAD_REQUEST); 
			c.deleteItinerario(id); 
		this.gestionePreferiti.deleteItinerario(id); 
		this.comuneRepository.save(c); 
		return new ResponseEntity<>("ok", HttpStatus.OK); 
		} 

		@DeleteMapping("/curatore/deleteContenuto") 
		public ResponseEntity<Object> deleteContenuto(Authentication authentication, @RequestParam("idPOI") Long idPOI, @RequestParam("id") Long id) { 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if (viewController.viewContenuto(c.getIdComune(), idPOI, id) == null) 
				return new ResponseEntity<>("Errore: Contenuto non trovato", HttpStatus.BAD_REQUEST); 
			c.deleteContenuto(idPOI, id); 
			this.comuneRepository.save(c); 
				return new ResponseEntity<>("ok", HttpStatus.OK); 
		} 

		@GetMapping("/curatore/getAllContenutoPendingPOI") 
		public ResponseEntity<Object> getAllContenutoPendingPOI(Authentication authentication){ 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			return new ResponseEntity<>(c.getAllContenutoPendingPOI(), HttpStatus.OK); 
		} 

		@GetMapping("/curatore/viewPendingContenuto") 
		public ResponseEntity<Object> viewPendingContenuto(Authentication authentication, @RequestParam("idPOI") Long idPOI, @RequestParam("id") Long id){ 
			Comune comune = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			ContenutoFD c = viewController.selectContenutoPending(comune.getIdComune(), idPOI, id); 
			if(c == null) 
				return new ResponseEntity<>("Errore: Contenuto non trovato tra i contenuti in pending", HttpStatus.BAD_REQUEST); 
			else 
				return new ResponseEntity<>(c, HttpStatus.OK); 
		} 

		@DeleteMapping("/curatore/deleteContenutoPending") 
		public ResponseEntity<Object> deleteContenutoPending(Authentication authentication, @RequestParam("idPOI") Long idPOI, @RequestParam("id") Long id) { 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if(viewController.selectContenutoPending(c.getIdComune(), idPOI, id) == null) 
				return new ResponseEntity<>("Errore: Contenuto non trovato tra i contenuti in pending", HttpStatus.BAD_REQUEST); 
			else { 
				c.deleteContenutoPending(idPOI, id); 
				this.comuneRepository.save(c); 
				this.contenutoRepository.deleteById(id); 
				return new ResponseEntity<>("ok", HttpStatus.OK); 
			} 
		} 
		
		 

		@PutMapping("/curatore/validazioneSelectContenuto") 
		public ResponseEntity<Object> validazioneSelectContenuto(Authentication authentication, @RequestParam("idPOI") Long idPOI, @RequestParam("id") Long id) { 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if(viewController.selectContenutoPending(c.getIdComune(), idPOI, id) == null) 
				return new ResponseEntity<>("Errore: Contenuto non trovato tra i contenuti in pending", HttpStatus.BAD_REQUEST); 
			else { 
				c.validazioneSelectContenuto(idPOI, id); 
				this.comuneRepository.save(c); 
				return new ResponseEntity<>("ok", HttpStatus.OK); 
			} 
		} 

		@GetMapping("/curatore/getAllItinerarioPending") 
		public ResponseEntity<Object> getAllItinerarioPending(Authentication authentication){ 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			return new ResponseEntity<>(c.getAllItinerarioPending(), HttpStatus.OK); 
		} 

		@GetMapping("/curatore/viewItinerarioPending") 
		public ResponseEntity<Object> viewItinerarioPending(Authentication authentication, @RequestParam("id") Long id){ 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			ItinerarioFD i = viewController.selectItinerarioPending(c.getIdComune(), id); 
			if(i == null) 
				return new ResponseEntity<>("Errore: Itinerario non trovato tra gli itinerari in pending", HttpStatus.BAD_REQUEST); 
			else 
				return new ResponseEntity<>(i, HttpStatus.OK); 
		} 

		@PutMapping("/curatore/validazioneSelectItinerario") 
		public ResponseEntity<Object> validazioneSelectItinerario(Authentication authentication, @RequestParam("id") Long id) { 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if(this.comuneRepository.findById(c.getIdComune()).get().selectItinerarioPending(id) == null) 
				return new ResponseEntity<>("Errore: Itinerario non trovato tra gli itinerari in pending", HttpStatus.BAD_REQUEST); 
			else { 
				c.validazioneSelectItinerario(id); 
				this.comuneRepository.save(c); 
				return new ResponseEntity<>("ok", HttpStatus.OK); 
			} 
		} 
		
		 

		@DeleteMapping("/curatore/deleteItinerarioPending") 
		public ResponseEntity<Object> deleteItinerarioPending(Authentication authentication, @RequestParam("id") Long id) { 
			Comune c = this.comuneRepository.findByCuratore(this.utenteAutenticatoRepository.findByUsername(authentication.getName())); 
			if(this.comuneRepository.findById(c.getIdComune()).get().selectItinerarioPending(id) == null) 
				return new ResponseEntity<>("Errore: Itinerario non trovato tra gli itinerari in pending", HttpStatus.BAD_REQUEST); 
			else { 
				c.deleteItinerarioPending(id); 
				this.comuneRepository.save(c); 
				this.itinerarioRepository.deleteById(id); 
				return new ResponseEntity<>("ok", HttpStatus.OK); 
			} 
		} 

		@RequestMapping(value = "/map", method = RequestMethod.GET) 
		public ModelAndView getMap(@PathParam("idComune") Long id){ 
			ModelAndView modelAndView = new ModelAndView(); 
			modelAndView.getModelMap().addAttribute("latitudine", this.comuneRepository.findById(id).get().getCoordinata().getLatitudine()); 
			modelAndView.getModelMap().addAttribute("longitudine",this.comuneRepository.findById(id).get().getCoordinata().getLongitudine()); 
			modelAndView.setViewName("OpenStreetMap"); 
			return modelAndView; 
		} 

} 