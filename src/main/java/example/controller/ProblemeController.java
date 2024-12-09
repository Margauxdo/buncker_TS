package example.controller;

import example.DTO.ProblemeDTO;
import example.interfaces.IProblemeService;
import example.services.ValiseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pb")
public class ProblemeController {

    @Autowired
    private IProblemeService problemeService;

    @Autowired
    private ValiseService valiseService;

    @GetMapping("/list")
    public String viewAllProblemes(Model model) {
        model.addAttribute("problemes", problemeService.getAllProblemes());
        return "problemes/pb_list";
    }

    @GetMapping("/view/{id}")
    public String viewProbleme(@PathVariable int id, Model model) {
        try {
            ProblemeDTO probleme = problemeService.getProblemeById(id);
            model.addAttribute("probleme", probleme);
            return "problemes/pb_details";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Problème non trouvé avec l'ID: " + id);
            return "problemes/error";
        }
    }

    @GetMapping("/create")
    public String createProblemeForm(Model model) {
        model.addAttribute("probleme", new ProblemeDTO());
        model.addAttribute("valises", valiseService.getAllValises());
        return "problemes/pb_create";
    }

    @PostMapping("/create")
    public String createProbleme(@Valid @ModelAttribute("probleme") ProblemeDTO problemeDTO, Model model) {
        try {
            problemeService.createProbleme(problemeDTO);
            return "redirect:/pb/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la création du problème.");
            return "problemes/error";
        }
    }

    @GetMapping("/edit/{id}")
    public String updatedProblemeForm(@PathVariable int id, Model model) {
        ProblemeDTO probleme = problemeService.getProblemeById(id);
        if (probleme == null) {
            model.addAttribute("errorMessage", "Problème non trouvé.");
            return "problemes/error";
        }
        model.addAttribute("probleme", probleme);
        model.addAttribute("valises", valiseService.getAllValises());
        return "problemes/pb_edit";
    }


    @PostMapping("/edit/{id}")
    public String updateProbleme(@PathVariable int id, @Valid @ModelAttribute("probleme") ProblemeDTO problemeDTO, Model model) {
        try {
            problemeService.updateProbleme(id, problemeDTO);
            return "redirect:/pb/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la mise à jour du problème.");
            return "problemes/error";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteProbleme(@PathVariable int id, Model model) {
        try {
            problemeService.deleteProbleme(id);
            return "redirect:/pb/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression du problème.");
            return "problemes/error";
        }
    }
}
