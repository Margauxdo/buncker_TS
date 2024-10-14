package example.controller;

import example.entities.SortieSemaine;
import example.interfaces.ISortieSemaineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sortieSemaine")
public class SortieSemaineController {

    @Autowired
    private ISortieSemaineService sortieSemaineService;

    @GetMapping
    public List<SortieSemaine> getAllSortieSemaine() {
        List<SortieSemaine> sortieSemaines = sortieSemaineService.getAllSortieSemaine();
        return new ResponseEntity<List<SortieSemaine>>(sortieSemaines, HttpStatus.OK).getBody();
    }

    @GetMapping("{id}")
    public ResponseEntity<SortieSemaine> getSortieSemaine(@PathVariable int id) {
        SortieSemaine sortieSemaine = sortieSemaineService.getSortieSemaine(id);
        return sortieSemaine != null ? new ResponseEntity<>(sortieSemaine, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<SortieSemaine> createSortieSemaine(@RequestBody SortieSemaine sortieSemaine) {
        try{
            SortieSemaine createdSortie = sortieSemaineService.createSortieSemaine(sortieSemaine);
            return new ResponseEntity<>(createdSortie, HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<SortieSemaine> updateSortieSemaine(@PathVariable int id, @RequestBody SortieSemaine sortieSemaine) {
        try {
            SortieSemaine updatedSortie = sortieSemaineService.updateSortieSemaine(id, sortieSemaine);
            return updatedSortie != null ? new ResponseEntity<>(updatedSortie, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<SortieSemaine> deleteSortieSemaine(@PathVariable int id) {
        try{
            sortieSemaineService.deleteSortieSemaine(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}


