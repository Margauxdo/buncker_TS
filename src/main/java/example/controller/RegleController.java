package example.controller;

import example.entities.Regle;
import example.exceptions.RegleNotFoundException;
import example.interfaces.IRegleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/regles")
@Validated
public class RegleController {

    @Autowired
    private IRegleService regleService;

    @GetMapping
    public ResponseEntity<List<Regle>> readRegles() {
        List<Regle> regleList = regleService.readAllRegles();
        return ResponseEntity.ok(regleList);
    }

    @GetMapping("{id}")
    public ResponseEntity<Regle> readRegleById(@PathVariable int id) {
        Regle regle = regleService.readRegle(id);
        if (regle == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(regle);
    }

    @PostMapping
    public ResponseEntity<?> createRegle(@RequestBody @Valid Regle regle) {
        try {
            if (regleService.regleExists(regle.getCoderegle())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Le code de la règle '" + regle.getCoderegle() + "' existe déjà.");
            }
            Regle newRegle = regleService.createRegle(regle);
            return ResponseEntity.status(HttpStatus.CREATED).body(newRegle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requête invalide : " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur.");
        }
    }




    @PutMapping("{id}")
    public ResponseEntity<Regle> updateRegle(@PathVariable int id, @RequestBody @Valid Regle regle) {
        try {
            Regle updatedRegle = regleService.updateRegle(id, regle);
            if (updatedRegle == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
            }
            return ResponseEntity.ok(updatedRegle);
        } catch (RegleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); //400
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRegle(@PathVariable int id) {
        try {
            regleService.deleteRegle(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
