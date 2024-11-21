package example.controller;

import example.entity.RegleManuelle;
import example.exceptions.ConflictException;
import example.interfaces.IRegleManuelleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regle-manuelle")
public class RegleManuelleController {

    private final IRegleManuelleService regleManuelleService;

    public RegleManuelleController(IRegleManuelleService regleManuelleService) {
        this.regleManuelleService = regleManuelleService;
    }

    @GetMapping
    public List<RegleManuelle> getRegleManuelles() {
        return regleManuelleService.getRegleManuelles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegleManuelle> getRegleManuelleById(@PathVariable int id) {
        RegleManuelle regleManuelle = regleManuelleService.getRegleManuelle(id);
        if (regleManuelle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(regleManuelle, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RegleManuelle> createRegleManuelle(@RequestBody RegleManuelle regleManuelle) {
        if (regleManuelle == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            RegleManuelle createdRegleManuelle = regleManuelleService.createRegleManuelle(regleManuelle);
            return new ResponseEntity<>(createdRegleManuelle, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Constraint violations
        } catch (IllegalArgumentException e) { // Handle specific exception
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ConflictException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @PutMapping("/{id}")
    public ResponseEntity<RegleManuelle> updateRegleManuelle(@PathVariable int id, @RequestBody RegleManuelle regleManuelle) {
        if (regleManuelle == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            RegleManuelle updatedRegleManuelle = regleManuelleService.updateRegleManuelle(id, regleManuelle);
            if (updatedRegleManuelle == null) {
                throw new EntityNotFoundException("Manual ruler not found with ID " + id);
            }
            return new ResponseEntity<>(updatedRegleManuelle, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegleManuelle(@PathVariable int id) {
        try {
            regleManuelleService.deleteRegleManuelle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
