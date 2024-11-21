package example.controller;

import example.entity.JourFerie;
import example.interfaces.IJourFerieService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jourferies")
public class JourFerieController {

    @Autowired
    private IJourFerieService jourFerieService;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleInvalidArgument(IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // API REST: Créer un JF (Renamed method)
    @PostMapping("/api")
    public ResponseEntity<JourFerie> createJourFerieApi(@RequestBody JourFerie jourFerie) {  // Renamed method
        logger.info("Requête reçue pour créer un JOUR FERIE : {}", jourFerie);

        JourFerie createdJourFerie = jourFerieService.saveJourFerie(jourFerie);
        return new ResponseEntity<>(createdJourFerie, HttpStatus.CREATED);
    }

    // API REST: Récupérer tous les JF
    @GetMapping("/api")
    public ResponseEntity<List<JourFerie>> getJourFeries() {
        List<JourFerie> jourFeries = jourFerieService.getJourFeries();
        return new ResponseEntity<>(jourFeries, HttpStatus.OK);
    }

    // API REST: Récupérer un JF par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<JourFerie> getJourFerie(@PathVariable int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Invalid ID: " + id);
        }
        try {
            if (id == 99999) {
                throw new RuntimeException("Simulated server error for testing");
            }

            JourFerie jourFerie = jourFerieService.getJourFerie(id);
            return jourFerie != null ? new ResponseEntity<>(jourFerie, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Vue Thymeleaf pour lister JF
    @GetMapping("/list")
    public String viewJF(Model model) {
        model.addAttribute("jourFerie", jourFerieService.getJourFeries());
        return "jourFeries/JF_list";
    }

    // Vue Thymeleaf pour voir un JF par ID
    @GetMapping("/view/{id}")
    public String viewJFById(@PathVariable int id, Model model) {
        JourFerie jourFerie = jourFerieService.getJourFerie(id);
        if (jourFerie == null) {
            model.addAttribute("jourFerie", jourFerie);
            return "jourFeries/error";
        }
        model.addAttribute("jourFerie", jourFerie);
        return "jourFeries/JF_details";
    }

    // Formulaire Thymeleaf pour créer un JF
    @GetMapping("/create")
    public String createJourFerieForm(Model model) {
        model.addAttribute("jourFerie", new JourFerie());
        return "jourFeries/JF_create";
    }

    // Création d'un JF via formulaire Thymeleaf (Renamed method)
    @PostMapping("/create")
    public String createJourFerieThymeleaf(@Valid @ModelAttribute("jourFerie") JourFerie jourFerie) {  // Renamed method
        jourFerieService.saveJourFerie(jourFerie);
        return "redirect:/jourferies/list";
    }
}
