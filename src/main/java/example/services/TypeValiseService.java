package example.services;

import example.entity.TypeValise;
import example.entity.Valise;
import example.interfaces.ITypeValiseService;
import example.repositories.TypeValiseRepository;
import example.repositories.ValiseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeValiseService implements ITypeValiseService {

    private final TypeValiseRepository typeValiseRepository;
    private final ValiseRepository valiseRepository;

    @Autowired
    public TypeValiseService(TypeValiseRepository typeValiseRepository, ValiseRepository valiseRepository) {
        this.typeValiseRepository = typeValiseRepository;
        this.valiseRepository = valiseRepository;
    }

    @Override
    public TypeValise createTypeValise(TypeValise typeValise) {
        if (typeValise == null || typeValise.getValise() == null) {
            throw new IllegalArgumentException("TypeValise or its associated Valise cannot be null");
        }

        Valise managedValise = valiseRepository.findById(typeValise.getValise().getId())
                .orElseThrow(() -> new EntityNotFoundException("Valise not found with ID: " + typeValise.getValise().getId()));

        typeValise.setValise(managedValise);
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
