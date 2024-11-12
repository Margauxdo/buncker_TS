package example.controller;

import example.entities.Formule;
import example.exceptions.FormuleNotFoundException;
import example.interfaces.IFormuleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/formules")
public class FormuleController {

    @Autowired
    private IFormuleService formuleService;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);


    @ExceptionHandler(FormuleNotFoundException.class)
    public ResponseEntity<String> handleFormuleNotFoundException(FormuleNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>("Invalid data for Formula creation", HttpStatus.BAD_REQUEST);
    }


    @GetMapping
    public List<Formule> getAllFormules() {
        List<Formule> formules = formuleService.getAllFormules();
        return new ResponseEntity<>(formules, HttpStatus.OK).getBody();
    }

    @GetMapping("{id}")
    public ResponseEntity<Formule> getFormuleById(@PathVariable int id) {
        Formule formule = formuleService.getFormuleById(id);
        return formule != null ? new ResponseEntity<>(formule, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Formule> createFormule(@Valid @RequestBody Formule formule) {
        try {
            Formule createdFormule = formuleService.createFormule(formule);
            return new ResponseEntity<>(createdFormule, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Formule> updateFormule(@PathVariable int id, @RequestBody Formule formule) {
        try {
            Formule updatedFormule = formuleService.updateFormule(id, formule);
            return updatedFormule != null ? new ResponseEntity<>(updatedFormule, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Formule> deleteFormule(@PathVariable int id) {
        try {
            formuleService.deleteFormule(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
