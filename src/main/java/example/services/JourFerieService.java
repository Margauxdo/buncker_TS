package example.services;

import example.DTO.JourFerieDTO;
import example.entity.JourFerie;
import example.entity.Regle;
import example.interfaces.IJourFerieService;
import example.repositories.JourFerieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JourFerieService implements IJourFerieService {

    @Autowired
    private JourFerieRepository jourFerieRepository;

    public JourFerieService(JourFerieRepository jourFerieRepository) {
        this.jourFerieRepository = jourFerieRepository;
    }

    private JourFerieDTO convertToDTO(JourFerie jourFerie) {
        return JourFerieDTO.builder()
                .id(jourFerie.getId())
                .date(jourFerie.getDate())
                .regleIds(jourFerie.getRegles() != null
                        ? jourFerie.getRegles().stream().map(Regle::getId).collect(Collectors.toList())
                        : List.of())
                .regles(jourFerie.getRegles())
                .build();
    }


    private JourFerie convertToEntity(JourFerieDTO jourFerieDTO) {
        JourFerie jourFerie = new JourFerie();
        jourFerie.setId(jourFerieDTO.getId());
        jourFerie.setDate(jourFerieDTO.getDate());

        // Exemple : associer les règles par leurs IDs
        List<Regle> regles = jourFerieDTO.getRegleIds().stream()
                .map(regleId -> {
                    Regle regle = new Regle();
                    regle.setId(regleId);
                    regle.setJourFerie(jourFerie); // Association bidirectionnelle
                    return regle;
                })
                .collect(Collectors.toList());
        jourFerie.setRegles(regles);
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
    public JourFerieDTO saveJourFerie(JourFerieDTO jourFerieDTO) {
        if (jourFerieDTO.getRegleIds() == null || jourFerieDTO.getRegleIds().isEmpty()) {
            throw new IllegalArgumentException("A JourFerie must have at least one associated Regle");
        }
        JourFerie jourFerie = convertToEntity(jourFerieDTO);
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
