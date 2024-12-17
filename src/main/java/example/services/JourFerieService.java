package example.services;

import example.DTO.JourFerieDTO;
import example.entity.JourFerie;
import example.entity.Regle;
import example.interfaces.IJourFerieService;
import example.repositories.JourFerieRepository;
import example.repositories.RegleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JourFerieService implements IJourFerieService {

    @Autowired
    private JourFerieRepository jourFerieRepository;

    @Autowired
    private RegleRepository regleRepository;

    @Override
    public JourFerieDTO getJourFerie(int id) {
        // Récupération de l'entité
        JourFerie jourFerie = jourFerieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("JourFerie introuvable avec l'ID : " + id));
        return mapToDTO(jourFerie);
    }

    @Override
    public List<JourFerieDTO> getJourFeries() {
        // Récupération de toutes les entités
        return jourFerieRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }




    @Override
    @Transactional
    public JourFerieDTO saveJourFerie(JourFerieDTO jourFerieDTO) {
        // Conversion du DTO en entité
        JourFerie jourFerie = mapToEntity(jourFerieDTO);

        // Sauvegarde de l'entité
        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);

        return mapToDTO(savedJourFerie);
    }

    @Override
    public void deleteJourFerie(int id) {
        // Vérification de l'existence de l'entité
        if (!jourFerieRepository.existsById(id)) {
            throw new EntityNotFoundException("JourFerie introuvable avec l'ID : " + id);
        }
        jourFerieRepository.deleteById(id);
    }

    @Override
    public List<Date> getAllDateFerie() {
        return List.of();
    }

    @Override
    public void persistJourFerie(JourFerieDTO jourFerieDTO) {

    }

    @Override
    public boolean existsByDate(Date date) {
        // Vérifie si un jour férié existe déjà pour une date donnée
        return jourFerieRepository.findAll().stream()
                .anyMatch(jourFerie -> jourFerie.getDate().equals(date));
    }

    // Méthode de conversion Entité -> DTO
    private JourFerieDTO mapToDTO(JourFerie jourFerie) {
        return JourFerieDTO.builder()
                .id(jourFerie.getId())
                .date(jourFerie.getDate())
                .build();
    }

    // Méthode de conversion DTO -> Entité
    private JourFerie mapToEntity(JourFerieDTO jourFerieDTO) {
        return JourFerie.builder()
                .id(jourFerieDTO.getId())
                .date(jourFerieDTO.getDate())
                .build();
    }
}
