package example.controller;

import example.entities.Mouvement;
import example.interfaces.IMouvementService;
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
        try{
            Mouvement createdMouvement = mouvementService.createMouvement(mouvement);
            return new ResponseEntity<>(createdMouvement, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Mouvement> updateMouvement(@PathVariable int id, @RequestBody Mouvement mouvement) {
        Mouvement updatedMouvement = mouvementService.updateMouvement(id, mouvement);
        return updatedMouvement != null ? new ResponseEntity<>(updatedMouvement, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Mouvement> deleteMouvement(@PathVariable int id) {
        mouvementService.deleteMouvement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
