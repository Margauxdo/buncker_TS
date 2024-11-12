package example.controller;

import example.entities.Formule;
import example.services.FormuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/formules")
public class FormuleViewController {

    @Autowired
    private FormuleService formuleService;

    // Afficher toutes les formules
    @GetMapping
    public String listFormules(Model model) {
        List<Formule> formules = formuleService.getAllFormules();
        model.addAttribute("formules", formules);
        return "formules/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("formule", new Formule());
        return "formules/create";
    }

    @PostMapping("/create")
    public String createFormule(@ModelAttribute("formule") Formule formule) {
        formuleService.createFormule(formule);
        return "redirect:/formules";
    }


    // Afficher le formulaire de modification
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Formule formule = formuleService.getFormuleById(id);
        model.addAttribute("formule", formule);
        return "formules/edit";
    }

    // Enregistrer les modifications
    @PostMapping("/edit/{id}")
    public String updateFormule(@PathVariable int id, @ModelAttribute("formule") Formule formule) {
        formuleService.updateFormule(id, formule);
        return "redirect:/formules";
    }

    // Supprimer une formule
    @GetMapping("/delete/{id}")
    public String deleteFormule(@PathVariable("id") int id) {
        formuleService.deleteFormule(id);
        return "redirect:/formules";
    }
}
