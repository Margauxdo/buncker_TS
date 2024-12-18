package example.services;

import example.DTO.TypeRegleDTO;
import example.entity.TypeRegle;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.ITypeRegleService;
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

    @Transactional
    public TypeRegleDTO createTypeRegle(TypeRegleDTO typeRegleDTO) {
        if (typeRegleDTO.getNomTypeRegle() == null || typeRegleDTO.getNomTypeRegle().isEmpty()) {
            throw new IllegalArgumentException("Le nom du type de règle est obligatoire.");
        }
        TypeRegle typeRegle = new TypeRegle();
        typeRegle.setNomTypeRegle(typeRegleDTO.getNomTypeRegle());
        typeRegle.setDescription(typeRegleDTO.getDescription());


        // Enregistrement dans la base de données
        TypeRegle savedTypeRegle = typeRegleRepository.save(typeRegle);
        return mapToDTO(savedTypeRegle);  // Retourner le DTO après la sauvegarde
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
        typeRegle.setDescription(typeRegleDTO.getDescription());

        // Enregistrement du TypeRegle mis à jour
        TypeRegle updatedTypeRegle = typeRegleRepository.save(typeRegle);

        return mapToDTO(updatedTypeRegle);  // Retourner le DTO mis à jour
    }

    @Override
    public void deleteTypeRegle(Integer id) {

    }

    @Override
    public TypeRegleDTO getTypeRegle(Integer id) {
        return null;
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
        typeRegleDTO.setDescription(typeRegle.getDescription());

        return typeRegleDTO;
    }
}
