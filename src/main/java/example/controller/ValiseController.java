package example.controller;

import example.entity.Valise;
import example.interfaces.IValiseService;
import example.exceptions.ResourceNotFoundException;
import example.repositories.ClientRepository;
import example.repositories.RegleRepository;
import example.repositories.TypeValiseRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static example.services.ValiseService.logger;

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
        if (valise == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(valise, HttpStatus.OK);
    }



    @PostMapping
    public ResponseEntity<Valise> createValise(@RequestBody Valise valise) {
        try {
            Valise createdValise = valiseService.createValise(valise);
            return new ResponseEntity<>(createdValise, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Argument invalide : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            logger.error("Ressource non trouvée : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            logger.error("Erreur interne du serveur : {}", e.getMessage());
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
