package example.controller;

import example.entity.Formule;
import example.exceptions.FormuleNotFoundException;
import example.interfaces.IFormuleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/formules")
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
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "formules/error";
    }


/*
    // API REST: Récupérer tous les formules
    @GetMapping("/api")
    public List<Formule> getAllFormulesApi() {
        List<Formule> formules = formuleService.getAllFormules();
        return new ResponseEntity<>(formules, HttpStatus.OK).getBody();
    }

    // API REST: Récupérer une formule par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<Formule> getFormuleByIdApi(@PathVariable int id) {
        Formule formule = formuleService.getFormuleById(id);
        return formule != null ? new ResponseEntity<>(formule, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // API REST: Créer une formule
    @PostMapping("/api")
    public ResponseEntity<Formule> createFormuleApi(@Valid @RequestBody Formule formule) {


        try {
            Formule createdFormule = formuleService.createFormule(formule);
            return new ResponseEntity<>(createdFormule, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
        logger.error("Erreur inattendue lors de la création de la formule : ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


}

    // API REST: Modifier une formule
    @PutMapping("/api/{id}")
    public ResponseEntity<Formule> updateFormuleApi(@PathVariable int id, @RequestBody Formule formule) {
        try {
            Formule updatedFormule = formuleService.updateFormule(id, formule);
            return updatedFormule != null ? new ResponseEntity<>(updatedFormule, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // API REST: Supprimer une formule
    @DeleteMapping("/api/{id}")
    public ResponseEntity<Formule> deleteFormuleApi(@PathVariable int id) {
        try {
            formuleService.deleteFormule(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

    // Vue Thymeleaf pour lister les formules
    @GetMapping("/formules/list")
    public String viewFormuleList(Model model) {
        model.addAttribute("formules", formuleService.getAllFormules());
        return "formules/formule_list";
    }
    // Vue Thymeleaf pour voir une formule par ID
    @GetMapping("/formules/view/{id}")
    public String viewFormuleById(@PathVariable int id, Model model) {
        Formule formule = formuleService.getFormuleById(id);
        if (formule == null) {
            throw new EntityNotFoundException("Formule avec l'Id " + id + " n'existe pas !");
        }
        model.addAttribute("formule", formule);
        return "formules/formule_detail";
    }


    // Formulaire Thymeleaf pour créer une formule
    @GetMapping("/formules/create")
    public String createFormuleForm(Model model) {
        model.addAttribute("formule", new Formule());
        return "formules/formule_create";
    }

    // Création d'une formule via formulaire Thymeleaf
    @PostMapping("/formules/create")
    public String createFormule(@Valid @ModelAttribute("formule") Formule formule,Model model){
        try {
            formuleService.createFormule(formule);
            return "redirect:/formules/list";
        } catch (IllegalArgumentException e) {
            // Ajouter le message d'erreur au modèle
            model.addAttribute("errorMessage", e.getMessage());
            return "formules/formule_create";
        }
    }

    // Formulaire Thymeleaf pour modifier une formule
    @GetMapping("/formules/edit/{id}")
    public String editFormuleForm(@PathVariable int id, Model model) {
        Formule formule = formuleService.getFormuleById(id);
        if (formule == null) {
            return "formules/error";
        }
        model.addAttribute("formule", formule);
        return "formules/formule_edit";
    }

    // Modifier une formule via formulaire Thymeleaf
    @PostMapping("/formules/edit/{id}")
    public String updateFormule(@PathVariable int id, @Valid @ModelAttribute("formule") Formule formule) {
        formuleService.updateFormule(id, formule);
        return "redirect:/formules/formules_list";
    }

    // Supprimer une formule via un formulaire Thymeleaf sécurisé
    @PostMapping("/formules/delete/{id}")
    public String deleteFormule(@PathVariable int id) {
        System.out.println("Deleting Formule with ID: " + id);
        formuleService.deleteFormule(id);
        return "redirect:/formules/formules_list";
    }





}
