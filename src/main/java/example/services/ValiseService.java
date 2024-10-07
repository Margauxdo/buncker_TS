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


    @Override
    public Valise createValise(Valise valise) {
        return valiseRepository .save(valise);
    }

    @Override
    public Valise updateValise(int id, Valise valise) {
        if (valiseRepository.existsById(id)) {
            valise.setId(id);
            return valiseRepository.save(valise);
        }else{
            throw new RuntimeException("suitcase not found");
        }
    }

    @Override
    public void deleteValise(int id) {
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
