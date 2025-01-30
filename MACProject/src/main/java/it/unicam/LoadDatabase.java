package it.unicam;

import it.unicam.model.Comune;
import it.unicam.model.Coordinate;
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
    PasswordEncoder PasswordEncoder;

     @Bean
     CommandLineRunner initDatabase(UtenteAutenticatoRepository repository, GestioneUtentiAutenticati gestioneUtentiAutenticati, ComuneRepository comuneRepository) {
         return args -> {
             UtenteAutenticato u1 = new UtenteAutenticato("Lisa", PasswordEncoder.encode("pass"), "lisa@gmail.it", Ruolo.TURISTAUTENTICATO);
             UtenteAutenticato u2 = new UtenteAutenticato("NegozioXY", PasswordEncoder.encode("pass"), "negozioxy@gmail.it", Ruolo.CONTRIBUTOR);
             UtenteAutenticato u3 = new UtenteAutenticato("ProLoco", PasswordEncoder.encode("pass"), "proloco@gmail.it", Ruolo.CONTRIBUTORAUTORIZZATO);
             UtenteAutenticato u4 = new UtenteAutenticato("Andrea", PasswordEncoder.encode("pass"), "andrea@gmail.it", Ruolo.GESTORE);
             UtenteAutenticato u5 = new UtenteAutenticato("Ilaria", PasswordEncoder.encode("pass"), "ilaria@gmail.it", Ruolo.CURATORE);
             UtenteAutenticato u6 = new UtenteAutenticato("Daniele", PasswordEncoder.encode("pass"), "daniele@gmail.it", Ruolo.ANIMATORE);
             repository.save(u1);
             repository.save(u2);
             repository.save(u3);
             repository.save(u4);
             repository.save(u5);
             repository.save(u6);
             gestioneUtentiAutenticati.addUtente(u1);
             gestioneUtentiAutenticati.addUtente(u2);
             gestioneUtentiAutenticati.addUtente(u3);
             gestioneUtentiAutenticati.addUtente(u4);
             gestioneUtentiAutenticati.addUtente(u5);
             gestioneUtentiAutenticati.addUtente(u6);
             comuneRepository.save(new Comune("Camerino",new Coordinate(43.1351,13.0683), u5));
         };
     }

}
