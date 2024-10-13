package example.services;

import example.entities.TypeValise;
import example.interfaces.ITypeValiseService;
import example.repositories.TypeValiseRepository;
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
        if (id != typeValise.getId()) {
            throw new IllegalArgumentException("L'ID du type de valise ne correspond pas");
        }
        if (typeValiseRepository.existsById(id)) {
            return typeValiseRepository.save(typeValise);
        } else {
            throw new RuntimeException("Suitcase type not found");
        }
    }


    @Override
    public void deleteTypeValise(int id) {
        if (!typeValiseRepository.existsById(id)) {
            throw new RuntimeException("Le type de valise n'existe pas");
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
