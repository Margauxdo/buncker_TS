package example.controller;

import example.DTO.JourFerieDTO;
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
        List<JourFerieDTO> jourFeries = jourFerieService.getJourFeries();
        model.addAttribute("jourFeries", jourFeries);
        return "joursFeries/JF_list";
    }

    @GetMapping("/view/{id}")
    public String viewJFById(@PathVariable int id, Model model) {
        JourFerieDTO jourFerie = jourFerieService.getJourFerie(id);
        model.addAttribute("jourFerie", jourFerie);
        return "joursFeries/JF_details";
    }

    @GetMapping("/create")
    public String createJourFerieForm(Model model) {
        model.addAttribute("jourFerie", new JourFerieDTO());
        return "joursFeries/JF_create";
    }

    @PostMapping("/create")
    public String createJourFerieThymeleaf(
            @Valid @ModelAttribute("jourFerie") JourFerieDTO jourFerieDTO,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Veuillez corriger les erreurs ci-dessous.");
            return "joursFeries/JF_create";
        }
        try {
            if (jourFerieService.existsByDate(jourFerieDTO.getDate())) {
                model.addAttribute("errorMessage", "Un jour férié avec cette date existe déjà.");
                return "joursFeries/JF_create";
            }
            jourFerieService.saveJourFerie(jourFerieDTO);
            return "redirect:/jourferies/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite.");
            return "joursFeries/JF_create";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteJourFerie(@PathVariable int id, Model model) {
        try {
            jourFerieService.deleteJourFerie(id);
            return "redirect:/jourferies/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
            return "joursFeries/JF_list";
        }
    }
}
