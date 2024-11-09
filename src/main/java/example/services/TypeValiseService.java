package example.services;

import example.entities.TypeValise;
import example.interfaces.ITypeValiseService;
import example.repositories.TypeValiseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeValiseService implements ITypeValiseService {

    private final TypeValiseRepository typeValiseRepository;

    public TypeValiseService(TypeValiseRepository typeValiseRepository) {
        this.typeValiseRepository = typeValiseRepository;
    }

    @Override
    public TypeValise createTypeValise(TypeValise typeValise) {

        return typeValiseRepository.save(typeValise);
    }

    @Override
    public TypeValise updateTypeValise(int id, TypeValise typeValise) {
        if (!typeValiseRepository.existsById(id)) {
            throw new EntityNotFoundException("The suitcase type does not exist");
        }
        if (id != typeValise.getId()) {
            throw new IllegalArgumentException("Suitcase type ID does not match");
        }
        return typeValiseRepository.save(typeValise);
    }




    @Override
    public void deleteTypeValise(int id) {
        if (!typeValiseRepository.existsById(id)) {
            throw new RuntimeException("The suitcase type does not exist");
        }
        typeValiseRepository.deleteById(id);
    }




    @Override
    public TypeValise getTypeValise(int id) {

        return typeValiseRepository.findById(id).orElse(null);
    }

    @Override
    public List<TypeValise> getTypeValises() {

        return typeValiseRepository.findAll();
    }
}
