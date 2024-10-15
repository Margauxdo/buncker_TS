package example.controller;

import example.entities.Regle;
import example.interfaces.IRegleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regle")
public class RegleController  {

    @Autowired
    private IRegleService regleService;

    @GetMapping
    public List<Regle> readRegles() {
        List<Regle> regleList = regleService.readAllRegles();
        return new ResponseEntity<>(regleList, HttpStatus.OK).getBody();
    }
    @GetMapping("{id}")
    public ResponseEntity<Regle> readRegleById(@PathVariable int id) {
        Regle regle = regleService.readRegle(id);
        return regle != null ? new ResponseEntity<>(regle, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Regle> createRegle(@RequestBody Regle regle) {
        try{
            Regle regleCreated = regleService.createRegle(regle);
            return new ResponseEntity<>(regleCreated, HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")  // Add the path variable to the mapping
    public ResponseEntity<Regle> updateRegle(@PathVariable int id, @RequestBody Regle regle) {
        try {
            Regle updatedRegle = regleService.updateRegle(id, regle);
            return updatedRegle != null ? new ResponseEntity<>(updatedRegle, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Regle> deleteRegle(@PathVariable int id) {
       try{
           regleService.deleteRegle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       }catch (IllegalArgumentException e){
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
       catch (RuntimeException e) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
}
