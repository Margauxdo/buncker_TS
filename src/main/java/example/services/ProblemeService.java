package example.services;

import example.DTO.ProblemeDTO;
import example.DTO.ValiseDTO;
import example.entity.Probleme;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.IProblemeService;
import example.repositories.ProblemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemeService implements IProblemeService {

    private final ProblemeRepository problemeRepository;
    private final ValiseService valiseService;

    public ProblemeService(ProblemeRepository problemeRepository, ValiseService valiseService) {
        this.problemeRepository = problemeRepository;
        this.valiseService = valiseService;
    }

    @Override
    public Probleme createProbleme(Probleme probleme) {
        return null;
    }

    @Override
    public Probleme updateProbleme(int id, Probleme probleme) {
        return null;
    }

    @Override
    public ProblemeDTO createProbleme(ProblemeDTO problemeDTO) {
        // Récupération de la ValiseDTO
        ValiseDTO valiseDTO = valiseService.getValiseById(problemeDTO.getValiseId());
        if (valiseDTO == null) {
            throw new ResourceNotFoundException("Valise not found with ID: " + problemeDTO.getValiseId());
        }

        // Conversion de ValiseDTO en Valise (Assurez-vous d'avoir une méthode utilitaire pour la conversion)
        Valise valise = convertToEntity(valiseDTO);

        // Création de l'entité Probleme
        Probleme probleme = Probleme.builder()
                .descriptionProbleme(problemeDTO.getDescriptionProbleme())
                .detailsProbleme(problemeDTO.getDetailsProbleme())
                .valise(valise) // Utilisation de l'entité Valise
                .build();

        // Sauvegarde de Probleme dans le repository
        Probleme savedProbleme = problemeRepository.save(probleme);

        // Conversion de l'entité sauvegardée en DTO
        return convertToDTO(savedProbleme);
    }

    // Méthode utilitaire pour convertir un ValiseDTO en Valise
    private Valise convertToEntity(ValiseDTO valiseDTO) {
        return Valise.builder()
                .id(valiseDTO.getId())
                .description(valiseDTO.getDescription())
                .numeroValise(valiseDTO.getNumeroValise())
                .build();
    }


    @Override
    public ProblemeDTO updateProbleme(int id, ProblemeDTO problemeDTO) {
        Probleme probleme = problemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with ID: " + id));

        probleme.setDescriptionProbleme(problemeDTO.getDescriptionProbleme());
        probleme.setDetailsProbleme(problemeDTO.getDetailsProbleme());

        Probleme updatedProbleme = problemeRepository.save(probleme);
        return convertToDTO(updatedProbleme);
    }

    @Override
    public void deleteProbleme(int id) {
        if (!problemeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Problem not found with ID: " + id);
        }
        problemeRepository.deleteById(id);
    }

    @Override
    public Probleme getProbleme(int id) {
        return null;
    }

    @Override
    public ProblemeDTO getProblemeById(int id) {
        Probleme probleme = problemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with ID: " + id));
        return convertToDTO(probleme);
    }

    @Override
    public List<ProblemeDTO> getAllProblemes() {
        return problemeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByDescriptionAndDetails(String description, String details) {
        return false;
    }

    private ProblemeDTO convertToDTO(Probleme probleme) {
        return ProblemeDTO.builder()
                .id(probleme.getId())
                .descriptionProbleme(probleme.getDescriptionProbleme())
                .detailsProbleme(probleme.getDetailsProbleme())
                .valiseId(probleme.getValise() != null ? probleme.getValise().getId() : null)
                .build();
    }
}
