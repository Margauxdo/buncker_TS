package example.controller;

import example.entity.Formule;
import example.entity.Regle;
import example.exceptions.RegleNotFoundException;
import example.interfaces.IRegleService;
import example.repositories.FormuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/regles")
@Validated
public class RegleController {

    @Autowired
    private IRegleService regleService;

    @Autowired
    private FormuleRepository formuleRepository;

    @ExceptionHandler(RegleNotFoundException.class)
    public String handleRegleNotFoundException(RegleNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "regles/error";
    }

    /*// API REST: Récupérer tous les regles
    @GetMapping("/api")
    public ResponseEntity<List<Regle>> readReglesApi() {
        List<Regle> regleList = regleService.readAllRegles();
        return ResponseEntity.ok(regleList);
    }

    // API REST: Récupérer une regle par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<Regle> readRegleByIdApi(@PathVariable int id) {
        Regle regle = regleService.readRegle(id);
        if (regle == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(regle);
    }

    // API REST: Créer un client
    @PostMapping("/api")
    public ResponseEntity<?> createRegleApi(@RequestBody @Valid Regle regle) {
        try {
            if (regleService.regleExists(regle.getCoderegle())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Le code de la règle '" + regle.getCoderegle() + "' existe déjà.");
            }
            // Gestion de la relation avec Formule (si elle existe)
            if (regle.getFormule() != null) {
                Formule formule = formuleRepository.findById(regle.getFormule().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Formule non trouvée."));
                regle.setFormule(formule);
            }

            Regle newRegle = regleService.createRegle(regle);
            return ResponseEntity.status(HttpStatus.CREATED).body(newRegle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requête invalide : " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur.");
        }
    }

    // API REST: Modifier une regle
    @PutMapping("/api/{id}")
    public ResponseEntity<Regle> updateRegleApi(@PathVariable int id, @RequestBody @Valid Regle regle) {
        try {
            Regle updatedRegle = regleService.updateRegle(id, regle);
            if (updatedRegle == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
            }
            return ResponseEntity.ok(updatedRegle);
        } catch (RegleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); //400
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500
        }
    }


    // API REST: Supprimer une regle
    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> deleteRegleApi(@PathVariable int id) {
        try {
            regleService.deleteRegle(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    // Vue Thymeleaf pour lister les regles
    @GetMapping("/list")
    public String viewAllRegles(Model model) {
        model.addAttribute("regles", regleService.readAllRegles());
        return "regles/regle_list";
    }
    // Vue Thymeleaf pour voir une regle par ID
    @GetMapping("/view/{id}")
    public String viewRegle(@PathVariable int id, Model model) {
        Regle regle = regleService.readRegle(id);
        if (regle == null) {
            throw new RegleNotFoundException("Regle with ID " + id + " not found.");
        }
        model.addAttribute("regle", regle);
        return "regles/regle_details";
    }

    // Formulaire Thymeleaf pour créer une regle
    @GetMapping("/create")
    public String createRegleForm(Model model) {
        model.addAttribute("formule", new Formule());
        return "regles/regle_create";
    }

    // Création d'une regle via formulaire Thymeleaf
    @PostMapping("/create")
    public String createRegle(@Valid @ModelAttribute("regle") Regle regle) {
        if (regle.getTypeRegle() == null) {
            throw new IllegalArgumentException("TypeRegle must be associated with a Regle");
        }
        regleService.createRegle(regle);
        return "redirect:/regles/list"; // Ensure redirection matches the expectation in the test
    }

    // Formulaire Thymeleaf pour modifier une regle
    @GetMapping("/edit/{id}")
    public String editRegleForm(@PathVariable int id, Model model) {
        Regle regle = regleService.readRegle(id);
        if (regle == null) {
            return "regles/error";
        }
        model.addAttribute("regle", regle);
        return "regles/regle_edit";
    }

    // Modifier une regle via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateRegle(@PathVariable int id, @Valid @ModelAttribute("regle") Regle regle) {
        regleService.updateRegle(id, regle);
        return "redirect:/regles/list"; // Mise à jour pour correspondre à l'URL attendue dans les tests
    }


    // Supprimer une regle via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public String deleteRegle(@PathVariable int id) {
        regleService.deleteRegle(id);
        return "redirect:/regles/list"; // Update to match test expectation
    }







}
