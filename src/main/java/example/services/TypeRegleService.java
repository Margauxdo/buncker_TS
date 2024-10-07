package example.services;

import example.entities.TypeRegle;
import example.interfaces.ITypeRegleService;
import example.repositories.TypeRegleRepository;
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
        if(typeRegleRepository.findById(id).isPresent()){
            return typeRegleRepository.save(typeRegle);
        }else{
            throw new RuntimeException("Ryle type does not exist");
        }
    }

    @Override
    public void deleteTypeRegle(int id) {
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
