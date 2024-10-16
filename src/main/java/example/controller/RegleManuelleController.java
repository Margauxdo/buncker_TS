package example.controller;

import example.entities.RegleManuelle;
import example.exceptions.ConflictException;
import example.interfaces.IRegleManuelleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regle-manuelle")
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
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Handle invalid input here
        } catch (ConflictException e) { // Custom exception for conflict
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
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedRegleManuelle, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RegleManuelle> deleteRegleManuelle(@PathVariable int id) {
        try {
            regleManuelleService.deleteRegleManuelle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
