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
    @Autowired
    private EntityManager entityManager;

    public JourFerieService(JourFerieRepository jourFerieRepository) {
        this.jourFerieRepository = jourFerieRepository;
    }

    private JourFerieDTO convertToDTO(JourFerie jourFerie) {
        if (jourFerie.getRegles() != null && !jourFerie.getRegles().isEmpty()) {
            return JourFerieDTO.builder()
                    .id(jourFerie.getId())
                    .date(jourFerie.getDate())
                    .regleCodes(jourFerie.getRegles().stream()
                            .map(Regle::getCoderegle)
                            .collect(Collectors.toList()))
                    .build();
        } else {
            return JourFerieDTO.builder()
                    .id(jourFerie.getId())
                    .date(jourFerie.getDate())
                    .build();
        }
    }


    @Transactional // Utilisation d'une transaction pour éviter les erreurs
    protected JourFerie convertToEntity(JourFerieDTO jourFerieDTO) {
        JourFerie jourFerie = new JourFerie();
        jourFerie.setId(jourFerieDTO.getId());
        jourFerie.setDate(jourFerieDTO.getDate());

        if (jourFerieDTO.getRegleIds() != null && !jourFerieDTO.getRegleIds().isEmpty()) {
            List<Regle> regles = jourFerieDTO.getRegleIds().stream()
                    .map(regleId -> {
                        // Recherche de la règle et attachement via EntityManager
                        Regle regle = regleRepository.findById(regleId)
                                .orElseThrow(() -> new IllegalArgumentException("La règle avec l'ID " + regleId + " n'existe pas."));
                        return entityManager.merge(regle); // Merge dans le contexte persistant
                    })
                    .collect(Collectors.toList());
            jourFerie.setRegles(regles);
        }

        return jourFerie;
    }












    @Override
    public JourFerieDTO getJourFerie(int id) {
        // Utilisation de la requête avec JOIN FETCH pour éviter LazyInitializationException
        JourFerie jourFerie = jourFerieRepository.findByIdWithRegles(id)
                .orElseThrow(() -> new EntityNotFoundException("JourFerie not found with ID: " + id));
        return convertToDTO(jourFerie);
    }


    @Override
    public List<JourFerieDTO> getJourFeries() {
        List<JourFerie> jourFeries = jourFerieRepository.findAllWithRegles();
        return jourFeries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional // Assure que cette méthode est exécutée dans une transaction
    public JourFerieDTO saveJourFerie(JourFerieDTO jourFerieDTO) {
        if (jourFerieDTO.getRegleIds() == null || jourFerieDTO.getRegleIds().isEmpty()) {
            throw new IllegalArgumentException("Un JourFerie doit avoir au moins une règle associée.");
        }

        // Convertir le DTO en entité et gérer les associations
        JourFerie jourFerie = convertToEntity(jourFerieDTO);

        // Sauvegarder l'entité dans la base de données
        JourFerie savedJourFerie = jourFerieRepository.save(jourFerie);
        return convertToDTO(savedJourFerie);
    }






    @Override
    public void deleteJourFerie(int id) {
        if (!jourFerieRepository.existsById(id)) {
            throw new EntityNotFoundException("JourFerie not found with id: " + id);
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
        return jourFerieRepository.findAll().stream()
                .anyMatch(jourFerie -> jourFerie.getDate().equals(date));
    }
}
