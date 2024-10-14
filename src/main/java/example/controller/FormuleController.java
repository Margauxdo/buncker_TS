package example.controller;

import example.entities.Formule;
import example.interfaces.IFormuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formule")
public class FormuleController {

    @Autowired
    private IFormuleService formuleService;

    //recup les formules
    @GetMapping
    public List<Formule> getAllFormules() {
        List<Formule> formules = formuleService.getAllFormules();
        return new ResponseEntity<>(formules, HttpStatus.OK).getBody();
    }

    //Recup client par son id
    @GetMapping("{id}")
    public ResponseEntity<Formule> getFormuleById(@PathVariable int id) {
        Formule formule = formuleService.getFormuleById(id);
        return formule != null ? new ResponseEntity<>(formule, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }

    //cree une nouvelle formule
    @PostMapping
    public ResponseEntity<Formule> createFormule(@RequestBody Formule formule) {
        try {
            Formule createdFormule = formuleService.createFormule(formule);
            return new ResponseEntity<>(createdFormule, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) { // Gérer le conflit
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Mettre a jour une formule existente
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


    //Supprimer une formule
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
