package example.controller;

import example.entity.JourFerie;
import example.interfaces.IJourFerieService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/jourferies")
public class JourFerieController {

    @Autowired
    private IJourFerieService jourFerieService;

    private static final Logger logger = LoggerFactory.getLogger(JourFerieController.class);

    @GetMapping("/list")
    public String viewJFDateList(Model model) {
        // Récupérer tous les jours fériés depuis le service
        List<JourFerie> jourFeries = jourFerieService.getJourFeries();
        model.addAttribute("jourFeries", jourFeries); // Correspondance avec la vue
        return "joursFeries/JF_list";
    }


    @GetMapping("/view/{id}")
    public String viewJFById(@PathVariable int id, Model model) {
        JourFerie jourFerie = jourFerieService.getJourFerie(id);
        model.addAttribute("jourFerie", jourFerie);
        return "joursFeries/JF_details";
    }




    @GetMapping("/create")
    public String createJourFerieForm(Model model) {
        model.addAttribute("jourFerie", new JourFerie());
        return "jourFeries/JF_create"; // Match Thymeleaf template path
    }

    @PostMapping("/create")
    public String createJourFerieThymeleaf(
            @Valid @ModelAttribute("jourFerie") JourFerie jourFerie,
            BindingResult result, // Correct parameter
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Veuillez corriger les erreurs ci-dessous.");
            return "jourFeries/JF_create"; // Assurez-vous que ce chemin est correct
        }
        try {
            boolean exists = jourFerieService.getAllDateFerie().contains(jourFerie.getDate());
            if (exists) {
                model.addAttribute("errorMessage", "Un jour férié avec cette date existe déjà.");
                return "jourFeries/JF_create";
            }
            jourFerieService.persistJourFerie(jourFerie);
            return "redirect:/jourferies/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite.");
            return "jourFeries/JF_create";
        }
    }


}
