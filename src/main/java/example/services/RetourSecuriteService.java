package example.services;

import example.entity.RetourSecurite;
import example.interfaces.IRetourSecuriteService;
import example.repositories.RetourSecuriteRepository;
import jakarta.persistence.EntityNotFoundException;
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
        if (retourSecurite.getNumero() == null) {
            throw new IllegalArgumentException("Invalid data: 'numero' cannot be null.");
        }

        RetourSecurite existingRetourSecurite = retourSecuriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Back Security not found with id: " + id));

        existingRetourSecurite.setNumero(retourSecurite.getNumero());
        return retourSecuriteRepository.save(existingRetourSecurite);
    }


    @Override
    public void deleteRetourSecurite(int id) {
        RetourSecurite retourSecurite = retourSecuriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Back Security not found with id: " + id));
        retourSecuriteRepository.delete(retourSecurite);
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
