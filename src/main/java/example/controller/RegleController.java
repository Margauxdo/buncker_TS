package example.controller;

import example.DTO.*;
import example.entity.Formule;
import example.interfaces.IRegleService;
import example.services.*;
import jakarta.validation.Valid;
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
    @Autowired
    private ClientService clientService;

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
        model.addAttribute("typesRegle", typeRegleService.getTypeRegles());
        model.addAttribute("formules", formuleService.getAllFormules());
        model.addAttribute("joursFeries", jourFerieService.getJourFeries());
        model.addAttribute("clients", clientService.getAllClients());
        return "regles/regle_create";
    }




    @PostMapping("/create")
    public String createRegle(@ModelAttribute("regle") @Valid RegleDTO regleDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("typesRegle", typeRegleService.getTypeRegles());
            model.addAttribute("formules", formuleService.getAllFormules());
            model.addAttribute("joursFeries", jourFerieService.getJourFeries());
            model.addAttribute("clients", clientService.getAllClients());
            return "regles/regle_create";
        }

        regleService.createRegle(regleDTO);
        return "redirect:/regles/list";
    }



    @GetMapping("/edit/{id}")
    public String editRegle(@PathVariable int id, Model model) {
        RegleDTO regle = regleService.getRegleById(id); // Récupère la règle
        model.addAttribute("regle", regle);
        model.addAttribute("valises", valiseService.getAllValises()); // Liste des valises
        model.addAttribute("typesRegle", typeRegleService.getTypeRegles()); // Liste des types de règles
        model.addAttribute("formules", formuleService.getAllFormules()); // Liste des formules
        model.addAttribute("joursFeries", jourFerieService.getJourFeries()); // Liste des jours fériés
        return "regles/regle_edit";
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




}
