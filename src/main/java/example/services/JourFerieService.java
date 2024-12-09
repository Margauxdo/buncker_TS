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
                .build();
    }

    private JourFerie convertToEntity(JourFerieDTO jourFerieDTO) {
        JourFerie jourFerie = new JourFerie();
        jourFerie.setId(jourFerieDTO.getId());
        jourFerie.setDate(jourFerieDTO.getDate());
        // Les règles peuvent être ajoutées via un service ou un gestionnaire si nécessaire
        return jourFerie;
    }

    @Override
    public JourFerieDTO getJourFerie(int id) {
        JourFerie jourFerie = jourFerieRepository.findByIdWithRegles(id)
                .orElseThrow(() -> new EntityNotFoundException("JourFerie not found with ID: " + id));
        return convertToDTO(jourFerie);
    }

    @Override
    public List<JourFerieDTO> getJourFeries() {
        return jourFerieRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public JourFerieDTO saveJourFerie(JourFerieDTO jourFerieDTO) {
        JourFerie jourFerie = convertToEntity(jourFerieDTO);
        if (jourFerie.getRegles().isEmpty()) {
            throw new IllegalStateException("A JourFerie must have at least one associated Regle");
        }
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
