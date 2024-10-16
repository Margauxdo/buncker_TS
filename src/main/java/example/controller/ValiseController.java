package example.controller;

import example.entities.Valise;
import example.interfaces.IValiseService;
import example.exceptions.ResourceNotFoundException; // Ajoutez cet import
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

    @GetMapping
    public List<Valise> getAllValises() {
        List<Valise> valises = valiseService.getAllValises();
        return new ResponseEntity<>(valises, HttpStatus.OK).getBody();
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
            Valise newValise = valiseService.createValise(valise);
            return new ResponseEntity<>(newValise, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) { // Exception définie ici
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Valise> updateValise(@RequestBody Valise valise, @PathVariable int id) {
        try {
            Valise updatedValise = valiseService.updateValise(id, valise);
            return updatedValise != null ? new ResponseEntity<>(updatedValise, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Valise> deleteValise(@PathVariable int id) {
        try{
            valiseService.deleteValise(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
