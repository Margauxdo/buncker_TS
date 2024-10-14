package example.controller;

import example.entities.Livreur;
import example.interfaces.ILivreurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livreur")
public class LivreurController {

    @Autowired
    private ILivreurService livreurService;

    @GetMapping
    public List<Livreur> getAllLivreurs() {
        List<Livreur> livreurs = livreurService.getAllLivreurs();
        return new ResponseEntity<>(livreurs, HttpStatus.OK).getBody();
    }

    @GetMapping("{id}")
    public ResponseEntity<Livreur> getLivreurById(@PathVariable("id") int id) {
        Livreur livreur = livreurService.getLivreurById(id);
        return livreur != null ? new ResponseEntity<>(livreur, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping
    public ResponseEntity<Livreur> createLivreur(@RequestBody Livreur livreur) {
        try{
            Livreur createdLivreur = livreurService.createLivreur(livreur);
            return new ResponseEntity<>(createdLivreur, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Livreur> updateLivreur(@PathVariable int id, @RequestBody Livreur livreur) {
        try {
            Livreur updatedLivreur = livreurService.updateLivreur(id, livreur);
            return updatedLivreur != null ? new ResponseEntity<>(updatedLivreur, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Livreur> deleteLivreur(@PathVariable int id) {
       try {
           livreurService.deleteLivreur(id);
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (RuntimeException e) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
}
