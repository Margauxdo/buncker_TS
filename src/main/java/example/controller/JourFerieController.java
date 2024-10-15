package example.controller;

import example.entities.JourFerie;
import example.interfaces.IJourFerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api-jourferie")
public class JourFerieController {

    @Autowired
    private IJourFerieService jourFerieService;

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
            JourFerie jourFerie = jourFerieService.getJourFerie(id);
            return jourFerie != null ? new ResponseEntity<>(jourFerie, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
