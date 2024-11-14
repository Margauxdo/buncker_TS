package example.controller;

import example.entities.Probleme;
import example.interfaces.IProblemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pb")
public class ProblemeController {

    @Autowired
    private IProblemeService problemeService;

    @GetMapping
    public List<Probleme> getAllProblemes() {
        List<Probleme> problemes = problemeService.getAllProblemes();
        return new ResponseEntity<>(problemes, HttpStatus.OK).getBody();
    }

    @GetMapping("{id}")
    public ResponseEntity<Probleme> getProblemById(@PathVariable int id) {
        Probleme probleme = problemeService.getProblemeById(id);
        return probleme != null ? new ResponseEntity<>(probleme, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Probleme> createProbleme(@RequestBody Probleme probleme) {
        try {
            Probleme newProbleme = problemeService.createProbleme(probleme);
            return new ResponseEntity<>(newProbleme, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @PutMapping("{id}")
    public ResponseEntity<Probleme> updateProbleme(@PathVariable int id, @RequestBody Probleme probleme) {
        try {
            Probleme updatedProbleme = problemeService.updateProbleme(id, probleme);
            return updatedProbleme != null ? new ResponseEntity<>(updatedProbleme, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProbleme(@PathVariable int id) {
        try {
            problemeService.deleteProbleme(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
