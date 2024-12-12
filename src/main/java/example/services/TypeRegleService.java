package example.services;

import example.DTO.TypeRegleDTO;
import example.entity.Regle;
import example.entity.TypeRegle;
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

    @Override
    public TypeRegleDTO createTypeRegle(TypeRegleDTO typeRegleDTO) {
        Regle regle = regleRepository.findById(typeRegleDTO.getRegle().getId())
                .orElseThrow(() -> new EntityNotFoundException("La règle associée avec l'ID " + typeRegleDTO.getRegle().getId() + " est introuvable"));

        TypeRegle typeRegle = TypeRegle.builder()
                .nomTypeRegle(typeRegleDTO.getNomTypeRegle())
                .regle(regle)
                .build();

        TypeRegle savedTypeRegle = typeRegleRepository.save(typeRegle);

        return mapToDTO(savedTypeRegle);
    }

    @Override
    @Transactional
    public TypeRegleDTO updateTypeRegle(int id, TypeRegleDTO typeRegleDTO) {
        TypeRegle existingTypeRegle = typeRegleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TypeRegle avec l'ID " + id + " est introuvable"));

        Regle regle = regleRepository.findById(typeRegleDTO.getRegle().getId())
                .orElseThrow(() -> new EntityNotFoundException("La règle associée avec l'ID " + typeRegleDTO.getRegle().getId() + " est introuvable"));

        existingTypeRegle.setNomTypeRegle(typeRegleDTO.getNomTypeRegle());
        existingTypeRegle.setRegle(regle);

        TypeRegle updatedTypeRegle = typeRegleRepository.save(existingTypeRegle);
        return mapToDTO(updatedTypeRegle);
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

    private TypeRegleDTO mapToDTO(TypeRegle typeRegle) {
        return TypeRegleDTO.builder()
                .id(typeRegle.getId())
                .nomTypeRegle(typeRegle.getNomTypeRegle())
                .regle(typeRegle.getRegle())
                .build();
    }
}
