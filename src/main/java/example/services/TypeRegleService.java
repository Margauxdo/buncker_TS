package example.services;

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
import java.util.Optional;

@Service
public class TypeRegleService implements ITypeRegleService {

    private final TypeRegleRepository typeRegleRepository;
    private final RegleRepository regleRepository;

    @Autowired
    public TypeRegleService(TypeRegleRepository typeRegleRepository, RegleRepository regleRepository) {
        this.typeRegleRepository = typeRegleRepository;
        this.regleRepository = regleRepository;
    }


    @Override
    public Optional<TypeRegle> getTypeRegle(int id) {
        return typeRegleRepository.findById(id);
    }


        @Override
        public TypeRegle createTypeRegle(String nomTypeRegle, Long regleId) {
            Regle regle = regleRepository.findById(Math.toIntExact(regleId))
                    .orElseThrow(() -> new EntityNotFoundException("La règle avec l'ID " + regleId + " est introuvable"));

            TypeRegle typeRegle = new TypeRegle();
            typeRegle.setNomTypeRegle(nomTypeRegle);
            typeRegle.setRegle(regle);

            return typeRegleRepository.save(typeRegle);
        }

        @Override
        public TypeRegle createTypeRegle(TypeRegle typeRegle) {
            if (typeRegle == null) {
                throw new IllegalArgumentException("TypeRegle ne peut pas être null");
            }

            // Validation de la Regle associée
            if (typeRegle.getRegle() == null) {
                throw new IllegalArgumentException("La Regle associée ne peut pas être null");
            }

            return typeRegleRepository.save(typeRegle);
        }

        @Override
        @Transactional
        public void deleteTypeRegle(int id) {
            if (!typeRegleRepository.existsById(id)) {
                throw new EntityNotFoundException("TypeRegle avec l'ID " + id + " est introuvable");
            }
            typeRegleRepository.deleteById(id);
        }

        @Override
        public List<TypeRegle> getTypeRegles() {
            return typeRegleRepository.findAll();
        }

    @Override
    public TypeRegle updateTypeRegle(int id, TypeRegle typeRegle) {
        if (typeRegle == null || typeRegle.getRegle() == null) {
            throw new IllegalArgumentException("TypeRegle ou Regle associé est null");
        }

        if (id != typeRegle.getId()) {
            throw new IllegalArgumentException("TypeRegle ID mismatch");
        }

        if (!typeRegleRepository.existsById(id)) {
            throw new EntityNotFoundException("TypeRegle avec l'ID " + id + " est introuvable");
        }

        TypeRegle existingTypeRegle = typeRegleRepository.findById(id).orElseThrow();
        existingTypeRegle.setNomTypeRegle(typeRegle.getNomTypeRegle());
        existingTypeRegle.setRegle(typeRegle.getRegle());

        return typeRegleRepository.save(existingTypeRegle);
    }



}

