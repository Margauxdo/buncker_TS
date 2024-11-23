package example.controller;

import example.entity.JourFerie;
import example.interfaces.IJourFerieService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jourferies")
public class JourFerieController {

    @Autowired
    private IJourFerieService jourFerieService;

    private static final Logger logger = LoggerFactory.getLogger(JourFerieController.class);

    // API REST: Créer un jour férié
    @PostMapping("/api")
    public ResponseEntity<JourFerie> createJourFerieApi(@RequestBody JourFerie jourFerie) {
        logger.info("Requête reçue pour créer un JOUR FERIE : {}", jourFerie);
        //JourFerie createdJourFerie = jourFerieService.saveJourFerie(jourFerie);
        //return new ResponseEntity<>(createdJourFerie, HttpStatus.CREATED);
        //TODO temporaire a changer
        return  new ResponseEntity<>(jourFerie, HttpStatus.CREATED);
    }



    // API REST: Récupérer tous les jours fériés
    @GetMapping("/api")
    public ResponseEntity<List<JourFerie>> getJourFeriesApi() {
        List<JourFerie> jourFeries = jourFerieService.getJourFeries();
        return new ResponseEntity<>(jourFeries, HttpStatus.OK);
    }

    // Vue Thymeleaf pour lister les jours fériés
    @GetMapping("/list")
    public String viewJF(Model model) {
        model.addAttribute("jourFerie", jourFerieService.getJourFeries());
        return "jourFeries/JF_list";
    }

    // Vue Thymeleaf pour voir un jour férié par ID
    @GetMapping("/view/{id}")
    public String viewJFById(@PathVariable int id, Model model) {
        JourFerie jourFerie = jourFerieService.getJourFerie(id);
        if (jourFerie == null) {
            model.addAttribute("errorMessage", "Jour férié non trouvé pour l'ID " + id);
            return "jourFeries/error";
        }
        model.addAttribute("jourFerie", jourFerie);
        return "jourFeries/JF_details";
    }

    // Formulaire Thymeleaf pour créer un jour férié
    @GetMapping("/create")
    public String createJourFerieForm(Model model) {
        model.addAttribute("jourFerie", new JourFerie());
        return "jourFeries/JF_create";
    }

    // Création d'un jour férié via formulaire Thymeleaf
    @PostMapping("/create")
    public String createJourFerieThymeleaf(@Valid @ModelAttribute("jourFerie") JourFerie jourFerie) {
        /*jourFerieService.saveJourFerie(jourFerie);*/
        //TODO a corriger la methode
        return "redirect:/jourferies/list";
    }
}
