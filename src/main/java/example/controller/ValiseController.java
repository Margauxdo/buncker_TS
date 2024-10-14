package example.controller;


import example.entities.Valise;
import example.interfaces.IValiseService;
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
        try{
            Valise newValise = valiseService.createValise(valise);
            return new ResponseEntity<>(newValise, HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Valise> updateValise(@RequestBody Valise valise, @PathVariable int id) {
        Valise updatedValise = valiseService.updateValise(id, valise);
        return updatedValise != null ? new ResponseEntity<>(updatedValise, HttpStatus.OK):
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Valise> deleteValise(@PathVariable int id) {
        valiseService.deleteValise(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
