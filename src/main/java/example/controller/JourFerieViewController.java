package example.controller;

import example.entities.JourFerie;
import example.services.JourFerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/jourferies")
public class JourFerieViewController {

    @Autowired
    private JourFerieService jourFerieService;

    @GetMapping
    public String listJourFeries(Model model) {
        List<JourFerie> jourFeries = jourFerieService.getJourFeries();
        model.addAttribute("jourFeries", jourFeries);
        return "jourferies/listJF";
    }

    // Afficher le formulaire de création de jour férié
    @GetMapping("/createJF")
    public String showCreateForm(Model model) {
        model.addAttribute("jourFerie", new JourFerie());
        return "jourferies/createJF";
    }

    // Créer un nouveau jour férié
    @PostMapping("/createJF")
    public String createJourFerie(@ModelAttribute JourFerie jourFerie) {
        jourFerieService.saveJourFerie(jourFerie);
        return "redirect:/jourferies";
    }

    // Afficher le formulaire de modification de jour férié
    @GetMapping("/editJF/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        JourFerie jourFerie = jourFerieService.getJourFerie(id);
        model.addAttribute("jourFerie", jourFerie);
        return "jourferies/editJF";
    }

    // Mettre à jour un jour férié
    @PostMapping("/editJF/{id}")
    public String updateJourFerie(@PathVariable int id, @ModelAttribute JourFerie jourFerie) {
        jourFerie.setId(id);
        jourFerieService.saveJourFerie(jourFerie);
        return "redirect:/jourferies";
    }

    // Supprimer un jour férié
    @GetMapping("/deleteJF/{id}")
    public String deleteJourFerie(@PathVariable int id) {
        jourFerieService.saveJourFerie(jourFerieService.getJourFerie(id));
        return "redirect:/jourferies";
    }
}

