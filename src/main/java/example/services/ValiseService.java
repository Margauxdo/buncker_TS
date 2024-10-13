package example.services;

import example.entities.Valise;
import example.interfaces.IValiseService;
import example.repositories.ValiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValiseService implements IValiseService {

    @Autowired
    private ValiseRepository valiseRepository;

    public ValiseService(ValiseRepository valiseRepository) {
        this.valiseRepository = valiseRepository;
    }


    @Override
    public Valise createValise(Valise valise) {
        return valiseRepository .save(valise);
    }

    @Override
    public Valise updateValise(int id, Valise valise) {
        if (valise.getId() != id) {
            throw new IllegalArgumentException("L'ID de la valise ne correspond pas");
        }
        if (valiseRepository.existsById(id)) {
            valise.setId(id);
            return valiseRepository.save(valise);
        } else {
            throw new RuntimeException("suitcase not found");
        }
    }


    @Override
    public void deleteValise(int id) {
        if (!valiseRepository.existsById(id)) {
            throw new RuntimeException("La valise n'existe pas");
        }
        valiseRepository.deleteById(id);
    }


    @Override
    public Valise getValiseById(int id) {
        return valiseRepository.findById(id).orElse(null);
    }

    @Override
    public List<Valise> getAllValises() {
        return valiseRepository.findAll();
    }
}
