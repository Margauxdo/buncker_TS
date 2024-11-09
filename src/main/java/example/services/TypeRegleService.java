package example.services;

import example.entities.TypeRegle;
import example.interfaces.ITypeRegleService;
import example.repositories.TypeRegleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeRegleService implements ITypeRegleService {

    @Autowired
    private TypeRegleRepository typeRegleRepository;

    public TypeRegleService(TypeRegleRepository typeRegleRepository){
        this.typeRegleRepository = typeRegleRepository;
    }

    @Override
    public TypeRegle createTypeRegle(TypeRegle typeRegle) {
        return typeRegleRepository.save(typeRegle);
    }

    @Override
    public TypeRegle updateTypeRegle(int id, TypeRegle typeRegle) {
        if (typeRegle.getId() != id) {
            throw new IllegalArgumentException("Rule type ID does not match");
        }

        if (!typeRegleRepository.existsById(id)) {
            throw new EntityNotFoundException("The rule type does not exist");
        }
        return typeRegleRepository.save(typeRegle);
    }


    @Override
    public void deleteTypeRegle(int id) {
        if (!typeRegleRepository.existsById(id)) {
            throw new EntityNotFoundException("The rule type does not exist");
        }
        typeRegleRepository.deleteById(id);
    }


    @Override
    public TypeRegle getTypeRegle(int id) {
        return typeRegleRepository.findById(id).orElse(null);
    }

    @Override
    public List<TypeRegle> getTypeRegles() {
        return typeRegleRepository.findAll();
    }
}

