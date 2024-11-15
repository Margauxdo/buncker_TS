package example.controller;

import example.entities.Livreur;
import example.exceptions.ConflictException;
import example.interfaces.ILivreurService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livreurs")
public class LivreurController {

    @Autowired
    private ILivreurService livreurService;

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Livreur>> getAllLivreurs() {
        List<Livreur> livreurs = livreurService.getAllLivreurs();
        return ResponseEntity.ok(livreurs);
    }

    @GetMapping("{id}")
    public ResponseEntity<Livreur> getLivreurById(@PathVariable("id") int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Invalid ID: " + id);
        }
        Livreur livreur = livreurService.getLivreurById(id);
        return livreur != null ? ResponseEntity.ok(livreur) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Livreur> createLivreur(@RequestBody Livreur livreur) {
        if (livreur.getNomLivreur() == null || livreur.getNomLivreur().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            Livreur createdLivreur = livreurService.createLivreur(livreur);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLivreur);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Livreur> updateLivreur(@PathVariable int id, @RequestBody Livreur livreur) {
        if (livreur.getNomLivreur() == null || livreur.getNomLivreur().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        livreur.setId(id);
        Livreur updatedLivreur = livreurService.updateLivreur(id, livreur);
        return updatedLivreur != null ? ResponseEntity.ok(updatedLivreur) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteLivreur(@PathVariable int id) {
        try {
            livreurService.deleteLivreur(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
