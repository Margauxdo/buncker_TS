package example.controller;

import example.entities.JourFerie;
import example.interfaces.IJourFerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-jourferie")
public class JourFerieController {

    @Autowired
    private IJourFerieService jourFerieService;

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping
    public ResponseEntity<JourFerie> createJourFerie(@RequestBody JourFerie jourFerie) {
        JourFerie createdJourFerie = jourFerieService.saveJourFerie(jourFerie);  // Assumes jourFerieService has a save method
        return new ResponseEntity<>(createdJourFerie, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<JourFerie>> getJourFeries() {
        List<JourFerie> jourFeries = jourFerieService.getJourFeries();
        return new ResponseEntity<>(jourFeries, HttpStatus.OK);
    }


    @GetMapping("{id}")
    public ResponseEntity<JourFerie> getJourFerie(@PathVariable int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Invalid ID: " + id);
        }
        try {
            // Simulate server error for testing when ID is 9999
            if (id == 9999) {
                throw new RuntimeException("Simulated server error for testing");
            }

            JourFerie jourFerie = jourFerieService.getJourFerie(id);
            return jourFerie != null ? new ResponseEntity<>(jourFerie, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






}
