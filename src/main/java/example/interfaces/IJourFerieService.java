package example.interfaces;

import example.DTO.JourFerieDTO;

import java.util.Date;
import java.util.List;

public interface IJourFerieService {
    JourFerieDTO getJourFerie(int id); // Récupère un jour férié par ID
    List<JourFerieDTO> getJourFeries(); // Récupère la liste des jours fériés
    JourFerieDTO saveJourFerie(JourFerieDTO jourFerieDTO); // Sauvegarde un jour férié
    void deleteJourFerie(int id); // Supprime un jour férié par ID
    List<Date> getAllDateFerie(); // Récupère toutes les dates des jours fériés
    void persistJourFerie(JourFerieDTO jourFerieDTO); // Persist un jour férié
    boolean existsByDate(Date date); // Vérifie si un jour férié existe pour une date donnée
}
