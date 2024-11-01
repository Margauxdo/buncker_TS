package example.controller;

import example.entities.Mouvement;
import example.interfaces.IMouvementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mouvement")
public class MouvementController {

    @Autowired
    private IMouvementService mouvementService;

    @GetMapping
    public List<Mouvement> getAllMouvements() {
        List<Mouvement> mouvements = mouvementService.getAllMouvements();
        return new ResponseEntity<>(mouvements, HttpStatus.OK).getBody();
    }

    @GetMapping("{id}")
    public ResponseEntity<Mouvement> getMouvementById(@PathVariable int id) {
        Mouvement mouvement = mouvementService.getMouvementById(id);
        return mouvement != null ? new ResponseEntity<>(mouvement, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Mouvement> createMouvement(@RequestBody Mouvement mouvement) {
        try {
            // Manual validation
            if (mouvement.getDateHeureMouvement() == null || mouvement.getStatutSortie() == null || mouvement.getStatutSortie().length() < 3) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Mouvement createdMouvement = mouvementService.createMouvement(mouvement);
            return new ResponseEntity<>(createdMouvement, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @PutMapping("/{id}")
    public ResponseEntity<Mouvement> updateMouvement(@PathVariable int id, @RequestBody Mouvement mouvement) {
        try {
            // Vérifier si le Mouvement existe
            if (!mouvementService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Renvoie 404 si l'entité n'est pas trouvée
            }

            // Autres validations et mises à jour
            if (mouvement.getStatutSortie() == null || mouvement.getStatutSortie().length() < 3
                    || mouvement.getDateHeureMouvement() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Renvoie 400 si la validation échoue
            }

            Mouvement updatedMouvement = mouvementService.updateMouvement(id, mouvement);
            return new ResponseEntity<>(updatedMouvement, HttpStatus.OK); // Renvoie 200 si tout est correct
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // En cas d'erreur serveur
        }
    }








    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable int id) {
        try {
            if (!mouvementService.existsById(id)) { // Check if the Mouvement exists
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            mouvementService.deleteMouvement(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
