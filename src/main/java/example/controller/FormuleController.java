package example.controller;

import example.DTO.FormuleDTO;
import example.DTO.RegleDTO;
import example.entity.Regle;
import example.exceptions.FormuleNotFoundException;
import example.interfaces.IFormuleService;
import example.services.RegleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;



@Controller
@RequestMapping("/formules")
public class FormuleController {

    private static final Logger logger = LoggerFactory.getLogger(FormuleController.class); // Ajout du logger


    @Autowired
    private IFormuleService formuleService;
    @Autowired
    private RegleService regleService;

    @GetMapping("/list")
    public String viewFormuleList(Model model) {

        List<FormuleDTO> formules = formuleService.getAllFormules();

        model.addAttribute("formules", formules);
        return "formules/formule_list";
    }

    @GetMapping("/view/{id}")
    public String viewFormuleById(@PathVariable int id, Model model) {
        model.addAttribute("formule", formuleService.getFormuleById(id));
        return "formules/formule_detail";
    }

    @GetMapping("/create")
    public String createFormuleForm(Model model) {
        model.addAttribute("formule", new FormuleDTO());
        model.addAttribute("regles", regleService.getAllRegles());
        return "formules/formule_create";
    }

    @PostMapping("/create")
    public String createFormule(@Valid @ModelAttribute("formule") FormuleDTO formuleDTO, Model model) {
        try {
            formuleService.createFormule(formuleDTO);
            return "redirect:/formules/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la création de la formule : " + e.getMessage());
            model.addAttribute("formule", formuleDTO);
            model.addAttribute("regles", regleService.getAllRegles()); // Récupérer la liste des règles
            return "formules/formule_create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editFormuleForm(@PathVariable int id, Model model) {
        // Récupérer la formule à modifier
        FormuleDTO formuleDTO = formuleService.getFormuleById(id);
        model.addAttribute("formule", formuleDTO);

        // Ajouter la liste des règles au modèle
        List<RegleDTO> regles = regleService.getAllRegles();
        model.addAttribute("regles", regles);

        return "formules/formule_edit";
    }




    @PostMapping("/edit/{id}")
    public String updateFormule(@PathVariable int id, @Valid @ModelAttribute("formule") FormuleDTO formuleDTO, Model model) {
        logger.info("Formule reçue pour mise à jour : {}", formuleDTO);
        formuleService.updateFormule(id, formuleDTO);
        return "redirect:/formules/list";
    }



    @PostMapping("/delete/{id}")
    public String deleteFormule(@PathVariable int id) {
        formuleService.deleteFormule(id);
        return "redirect:/formules/list";
    }
}
