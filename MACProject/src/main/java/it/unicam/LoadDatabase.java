package it.unicam;

import it.unicam.model.*;
import it.unicam.model.utenti.GestioneUtentiAutenticati;
import it.unicam.model.utenti.Ruolo;
import it.unicam.model.utenti.UtenteAutenticato;
import it.unicam.repositories.ComuneRepository;
import it.unicam.repositories.UtenteAutenticatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class LoadDatabase {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(UtenteAutenticatoRepository userRepo,
                                   GestioneUtentiAutenticati gestioneUtenti,
                                   ComuneRepository comuneRepo) {
        return args -> {

            UtenteAutenticato u1 = new UtenteAutenticato("Lisa", passwordEncoder.encode("pass"), "lisa@gmail.it", Ruolo.TURISTAUTENTICATO);
            UtenteAutenticato u2 = new UtenteAutenticato("NegozioXY", passwordEncoder.encode("pass"), "negozioxy@gmail.it", Ruolo.CONTRIBUTOR);
            UtenteAutenticato u3 = new UtenteAutenticato("ProLoco", passwordEncoder.encode("pass"), "proloco@gmail.it", Ruolo.CONTRIBUTORAUTORIZZATO);
            UtenteAutenticato u4 = new UtenteAutenticato("Andrea", passwordEncoder.encode("pass"), "andrea@gmail.it", Ruolo.GESTORE);
            UtenteAutenticato u5 = new UtenteAutenticato("Ilaria", passwordEncoder.encode("pass"), "ilaria@gmail.it", Ruolo.CURATORE);
            UtenteAutenticato u6 = new UtenteAutenticato("Daniele", passwordEncoder.encode("pass"), "daniele@gmail.it", Ruolo.ANIMATORE);

            userRepo.save(u1);
            userRepo.save(u2);
            userRepo.save(u3);
            userRepo.save(u4);
            userRepo.save(u5);
            userRepo.save(u6);

            gestioneUtenti.addUtente(u1);
            gestioneUtenti.addUtente(u2);
            gestioneUtenti.addUtente(u3);
            gestioneUtenti.addUtente(u4);
            gestioneUtenti.addUtente(u5);
            gestioneUtenti.addUtente(u6);

            Comune comune = new Comune("Camerino", new Coordinate(43.1351,13.0683), u5);

            // Costruttore del Comune inserisce già un POI logico corrispondente al Comune (validato).
            // POI addizionale di test
            POILuogo testPOI = new POILuogo(new Coordinate(43.1352,13.0684));
            testPOI.insertInfoPOI("POI_Valido", "POI di test già validato");
            comune.insertPOI(testPOI);
            // Per testing si utilizza un contenuto vuoto, in produzione si dovrà implementare un sistema di upload
            Contenuto contenutoValido = new Contenuto(
                    "ContenutoValido",
                    "Descrizione di test",
                    new byte[0] // Nessun file
            );
            testPOI.addContenuto(contenutoValido);
            comuneRepo.save(comune);

            System.out.println("Database inizializzato con utenti, comune e un POI+Contenuto validati!");
        };
    }
}
