package example.controller;

import example.DTO.*;
import example.entity.Formule;
import example.entity.RegleManuelle;
import example.interfaces.IRegleService;
import example.services.FormuleService;
import example.services.JourFerieService;
import example.services.TypeRegleService;
import example.services.ValiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/regles")
public class RegleController {

    @Autowired
    private IRegleService regleService;
    @Autowired
    private FormuleService formuleService;
    @Autowired
    private ValiseService valiseService;
    @Autowired
    private TypeRegleService typeRegleService;
    @Autowired
    private JourFerieService jourFerieService;

    @GetMapping("/list")
    public String listRegles(Model model) {

        List<RegleDTO> reglesDTO = regleService.getAllRegles();

        model.addAttribute("regles", reglesDTO);

        return "regles/regle_list";
    }

    @GetMapping("/view/{id}")
    public String viewRegle(@PathVariable int id, Model model) {
        RegleDTO regle = regleService.getRegleById(id);
        model.addAttribute("regle", regle);
        return "regles/regle_details";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("regle", new RegleDTO());
        model.addAttribute("valises", valiseService.getAllValises());
        model.addAttribute("typesRegle", typeRegleService.getTypeRegles());
        model.addAttribute("formules", formuleService.getAllFormules());
        model.addAttribute("joursFeries", jourFerieService.getJourFeries());
        return "regles/regle_create"; // Chemin du template
    }

    @PostMapping("/create")
    public String createRegle(@ModelAttribute("regle") RegleDTO regleDTO) {
        // Logique pour sauvegarder la règle manuelle
        regleService.createRegle(regleDTO);
        return "redirect:/regles/list";
    }

    @GetMapping("/edit/{id}")
    public String editRegle(@PathVariable int id, Model model) {
        RegleDTO regle = regleService.getRegleById(id); // Récupère la règle à partir du service
        model.addAttribute("regle", regle);
        model.addAttribute("valises", valiseService.getAllValises()); // Récupère les valises
        model.addAttribute("typesRegle", typeRegleService.getTypeRegles()); // Récupère les types de règle
        model.addAttribute("formules", formuleService.getAllFormules()); // Récupère les formules
        model.addAttribute("joursFeries", jourFerieService.getJourFeries()); // Récupère les jours fériés
        return "regles/regle_edit"; // Retourne la vue pour la modification de la règle
    }

    @PostMapping("/edit/{id}")
    public String updateRegle(@PathVariable int id, @ModelAttribute("regle") RegleDTO regleDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "regles/regle_edit"; // Retourne à la vue de modification si des erreurs sont détectées
        }
        regleService.updateRegle(id, regleDTO); // Met à jour la règle
        return "redirect:/regles/list"; // Redirige vers la liste des règles après la modification
    }


    @PostMapping("/delete/{id}")
    public String deleteRegle(@PathVariable int id) {
        regleService.deleteRegle(id);
        return "redirect:/regles/list";
    }

    @GetMapping("/createManuelle")
    public String createRegleManuelleForm(Model model) {
        model.addAttribute("regleManuelle", new RegleManuelle());
        return "/regles/RM_create"; // Renvoie la vue pour créer une règle manuelle
    }

    // Méthode POST pour enregistrer la règle manuelle
    @PostMapping("/createManuelle")
    public String createRegleManuelle(@ModelAttribute("regleManuelle") RegleManuelle regleManuelle) {
        regleService.saveRegleManuelle(regleManuelle); // Sauvegarde la règle manuelle
        return "redirect:/regles/list";  // Redirige vers la liste des règles
    }

}
