package example.controller;

import example.entities.Client;
import example.entities.Regle;
import example.entities.TypeValise;
import example.entities.Valise;
import example.interfaces.IValiseService;
import example.exceptions.ResourceNotFoundException;
import example.repositories.ClientRepository;
import example.repositories.RegleRepository;
import example.repositories.TypeValiseRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/valise")
public class ValiseController {

    @Autowired
    private IValiseService valiseService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RegleRepository regleRepository;
    @Autowired
    private TypeValiseRepository typeValiseRepository;

    @GetMapping
    public ResponseEntity<List<Valise>> getAllValises() {
        List<Valise> valises = valiseService.getAllValises();
        return new ResponseEntity<>(valises, HttpStatus.OK);
    }



    @GetMapping("{id}")
    public ResponseEntity<Valise> getValiseById(@PathVariable int id) {
        Valise valise = valiseService.getValiseById(id);
        return valise != null ? new ResponseEntity<>(valise, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Valise> createValise(@RequestBody Valise valise) {
        try {
            // Vérification du client
            if (valise.getClient() == null || valise.getClient().getId() == 0) {
                throw new IllegalArgumentException("Client ID is required");
            }

            Client client = clientRepository.findById(valise.getClient().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + valise.getClient().getId()));
            valise.setClient(client);

            // Vérification de la règle
            if (valise.getRegleSortie() != null && valise.getRegleSortie().getId() != 0) {
                Regle regle = regleRepository.findById(valise.getRegleSortie().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Regle not found with ID: " + valise.getRegleSortie().getId()));
                valise.setRegleSortie(regle);
            }

            // Vérification du type de valise
            if (valise.getTypevalise() != null && valise.getTypevalise().getId() != 0) {
                TypeValise typeValise = typeValiseRepository.findById(valise.getTypevalise().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("TypeValise not found with ID: " + valise.getTypevalise().getId()));
                valise.setTypevalise(typeValise);
            }

            Valise newValise = valiseService.createValise(valise);
            return new ResponseEntity<>(newValise, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






    @PutMapping("{id}")
    public ResponseEntity<Valise> updateValise(@RequestBody Valise valise, @PathVariable int id) {
        try {
            Valise updatedValise = valiseService.updateValise(id, valise);
            return updatedValise != null ? new ResponseEntity<>(updatedValise, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteValise(@PathVariable int id) {
        try {
            valiseService.deleteValise(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
