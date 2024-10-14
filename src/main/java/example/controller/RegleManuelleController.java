package example.controller;

import example.entities.RegleManuelle;
import example.interfaces.IRegleManuelleService;
import example.repositories.RegleManuelleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regleManuelle")
public class RegleManuelleController {

    @Autowired
    private IRegleManuelleService regleManuelleService;
    @Autowired
    private RegleManuelleRepository regleManuelleRepository;

    @GetMapping
    public List<RegleManuelle> getRegleManuelles() {
        List<RegleManuelle> regleManuelles = regleManuelleService.getRegleManuelles();
        return new ResponseEntity<>(regleManuelles, HttpStatus.OK).getBody();
    }

    @GetMapping("{id}")
    public ResponseEntity<RegleManuelle> getRegleManuelleById(@PathVariable("id") int id) {
        RegleManuelle regleManuelle = regleManuelleService.getRegleManuelle(id);
        return regleManuelle != null ? new ResponseEntity<>(regleManuelle, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<RegleManuelle> createRegleManuelle(@RequestBody RegleManuelle regleManuelle) {
        try {
            RegleManuelle createdRM = regleManuelleService.createRegleManuelle(regleManuelle);
            return new ResponseEntity<>(createdRM, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

        @PutMapping
        public ResponseEntity<RegleManuelle> updateRegleManuelle(@PathVariable int id, @RequestBody RegleManuelle regleManuelle) {
            try {
                RegleManuelle updatedRM = regleManuelleService.updateRegleManuelle(id, regleManuelle);

                return updatedRM != null ? new ResponseEntity<>(updatedRM, HttpStatus.OK) :
                        new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        @DeleteMapping("{id}")
        public ResponseEntity<RegleManuelle> deleteRegleManuelle(@PathVariable int id) {
        try{
            regleManuelleService.deleteRegleManuelle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }

    }




