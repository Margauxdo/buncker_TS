package example.services;

import example.entities.RetourSecurite;
import example.interfaces.IRetourSecuriteService;
import example.repositories.RetourSecuriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetourSecuriteService implements IRetourSecuriteService {

    @Autowired
    private RetourSecuriteRepository retourSecuriteRepository;

    public RetourSecuriteService(RetourSecuriteRepository retourSecuriteRepository) {
        this.retourSecuriteRepository = retourSecuriteRepository;
    }

    @Override
    public RetourSecurite createRetourSecurite(RetourSecurite retourSecurite) {
        return retourSecuriteRepository.save(retourSecurite);
    }

    @Override
    public RetourSecurite updateRetourSecurite(int id, RetourSecurite retourSecurite) {
        if (retourSecurite.getId() == id) {
            retourSecurite.setId(id);
            return retourSecuriteRepository.save(retourSecurite);
        }else{
            throw new RuntimeException("Safety return does not exist");
        }
    }

    @Override
    public void deleteRetourSecurite(int id) {
        retourSecuriteRepository.deleteById(id);
    }

    @Override
    public RetourSecurite getRetourSecurite(int id) {
        return retourSecuriteRepository.findById(id).orElse(null);
    }

    @Override
    public List<RetourSecurite> getAllRetourSecurites() {
        return retourSecuriteRepository.findAll();
    }
}
