package example.controller;

import example.entity.JourFerie;
import example.interfaces.IJourFerieService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/jourferies")
public class JourFerieController {

    @Autowired
    private IJourFerieService jourFerieService;

    private static final Logger logger = LoggerFactory.getLogger(JourFerieController.class);

    // Vue Thymeleaf pour lister les jours fériés
    @GetMapping("/list")
    public String viewJFDateList(Model model) {
        try {
            List<Date> datesFerie = jourFerieService.getAllDateFerie();
            model.addAttribute("datesFerie", datesFerie);
            return "jourFeries/JF_dates"; // View to display the dates
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des jours fériés", e);
            model.addAttribute("errorMessage", "Une erreur s'est produite lors de la récupération des jours fériés.");
            return "jourFeries/error";
        }
    }

    // Vue Thymeleaf pour voir un jour férié par ID
    @GetMapping("/view/{id}")
    public String viewJFById(@PathVariable int id, Model model) {
        try {
            JourFerie jourFerie = jourFerieService.getJourFerie(id);
            if (jourFerie == null) {
                model.addAttribute("errorMessage", "Jour férié non trouvé pour l'ID " + id);
                return "jourFeries/error";
            }
            model.addAttribute("jourFerie", jourFerie);
            return "jourFeries/JF_details"; // View for holiday details
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du jour férié par ID", e);
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite.");
            return "jourFeries/error";
        }
    }

    // Formulaire Thymeleaf pour créer un jour férié
    @GetMapping("/create")
    public String createJourFerieForm(Model model) {
        model.addAttribute("jourFerie", new JourFerie());
        return "jourFeries/JF_create";
    }

    @PostMapping("/create")
    public String createJourFerieThymeleaf(@Valid @ModelAttribute("jourFerie") JourFerie jourFerie, Model model) {
        try {
            // Vérifier si une date identique existe déjà
            List<Date> existingDates = jourFerieService.getAllDateFerie();
            if (existingDates.contains(jourFerie.getDate())) {
                model.addAttribute("errorMessage", "Un jour férié avec cette date existe déjà.");
                return "jourFeries/JF_create";
            }

            // Ajouter le jour férié
            jourFerieService.persistJourFerie(jourFerie);
            return "redirect:/jourferies/list";
        } catch (Exception e) {
            logger.error("Erreur lors de la création du jour férié", e);
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite.");
            return "jourFeries/JF_create";
        }
    }
}





