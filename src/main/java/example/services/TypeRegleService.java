package example.services;

import example.DTO.TypeRegleDTO;
import example.entity.Regle;
import example.entity.TypeRegle;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.ITypeRegleService;
import example.repositories.RegleRepository;
import example.repositories.TypeRegleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeRegleService implements ITypeRegleService {

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    @Autowired
    private RegleRepository regleRepository;

    @Transactional
    public TypeRegleDTO createTypeRegle(TypeRegleDTO typeRegleDTO) {
        // Crée l'objet TypeRegle à partir du DTO
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle(typeRegleDTO.getNomTypeRegle());

        // Récupère les règles associées en fonction des IDs envoyés
        if (typeRegleDTO.getRegleIds() != null && !typeRegleDTO.getRegleIds().isEmpty()) {
            List<Regle> regles = regleRepository.findAllById(typeRegleDTO.getRegleIds());
            typeRegle.setRegles(regles);
        }

        // Sauvegarde du TypeRegle avec les règles associées
        TypeRegle savedTypeRegle = typeRegleRepository.save(typeRegle);

        return mapToDTO(savedTypeRegle);  // Conversion vers le DTO pour la réponse
    }

    @Override
    public TypeRegleDTO updateTypeRegle(int id, TypeRegleDTO typeRegleDTO) {
        return null;
    }

    @Transactional
    public TypeRegleDTO updateTypeRegle(Integer id, TypeRegleDTO typeRegleDTO) {
        TypeRegle typeRegle = typeRegleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TypeRegle not found with ID: " + id));

        typeRegle.setNomTypeRegle(typeRegleDTO.getNomTypeRegle());

        // Mise à jour des règles associées
        if (typeRegleDTO.getRegleIds() != null && !typeRegleDTO.getRegleIds().isEmpty()) {
            List<Regle> regles = regleRepository.findAllById(typeRegleDTO.getRegleIds());
            typeRegle.setRegles(regles);
        }

        // Enregistrement du TypeRegle mis à jour
        TypeRegle updatedTypeRegle = typeRegleRepository.save(typeRegle);

        return mapToDTO(updatedTypeRegle);  // Retourner le DTO mis à jour
    }

    @Override
    public void deleteTypeRegle(int id) {
        if (!typeRegleRepository.existsById(id)) {
            throw new EntityNotFoundException("TypeRegle avec l'ID " + id + " est introuvable");
        }
        typeRegleRepository.deleteById(id);
    }

    @Override
    public TypeRegleDTO getTypeRegle(int id) {
        return typeRegleRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("TypeRegle avec l'ID " + id + " est introuvable"));
    }

    @Override
    public List<TypeRegleDTO> getTypeRegles() {
        return typeRegleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Méthode pour convertir une entité TypeRegle vers un DTO
    private TypeRegleDTO mapToDTO(TypeRegle typeRegle) {
        TypeRegleDTO typeRegleDTO = new TypeRegleDTO();
        typeRegleDTO.setId(typeRegle.getId());
        typeRegleDTO.setNomTypeRegle(typeRegle.getNomTypeRegle());

        // Associe les IDs des règles au DTO
        List<Integer> regleIds = typeRegle.getRegles().stream()
                .map(Regle::getId)
                .collect(Collectors.toList());
        typeRegleDTO.setRegleIds(regleIds);

        return typeRegleDTO;
    }

    // Méthode pour convertir un DTO vers une entité TypeRegle
    private TypeRegle mapToEntity(TypeRegleDTO typeRegleDTO) {
        TypeRegle typeRegle = TypeRegle.builder()
                .id(typeRegleDTO.getId())
                .nomTypeRegle(typeRegleDTO.getNomTypeRegle())
                .build();

        if (typeRegleDTO.getRegleIds() != null) {
            List<Regle> regles = regleRepository.findAllById(typeRegleDTO.getRegleIds());
            typeRegle.setRegles(regles);
        }

        return typeRegle;
    }
}
