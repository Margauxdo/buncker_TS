package example.controller;

import example.entities.Probleme;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.IProblemeService;
import example.repositories.ProblemeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pb")
public class ProblemeController {

    @Autowired
    private IProblemeService problemeService;
    @Autowired
    private ProblemeRepository problemeRepository;

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
    public ResponseEntity<Probleme> createProbleme(@Valid @RequestBody Probleme probleme) {
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
            if (updatedProbleme == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(updatedProbleme);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Gérer l'exception de manière cohérente
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }







    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProbleme(@PathVariable int id) {
        try {
            Optional<Probleme> existingProbleme = problemeRepository.findById(id);
            if (existingProbleme.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            problemeService.deleteProbleme(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            // Si une RuntimeException est levée, on retourne INTERNAL_SERVER_ERROR
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
