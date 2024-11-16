package example.services;

import example.entities.Valise;
import example.exceptions.ResourceNotFoundException;
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
            throw new IllegalArgumentException("Suitcase ID does not match");
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
            throw new ResourceNotFoundException("The suitcase does not exist");
        }
        valiseRepository.deleteById(id);
    }




    @Override
    public Valise getValiseById(int id) {
        Valise valise = valiseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Valise not found"));
        if (valise.getMouvementList() != null) {
            valise.getMouvementList().size();
        }
        return valise;
    }



    @Override
    public List<Valise> getAllValises() {
        return valiseRepository.findAll();
    }
}
