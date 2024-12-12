package example.controller;

import example.DTO.*;
import example.entity.Formule;
import example.interfaces.IRegleService;
import example.services.FormuleService;
import example.services.JourFerieService;
import example.services.TypeRegleService;
import example.services.ValiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String createRegleForm(Model model) {
        model.addAttribute("regle", new RegleDTO());

        List<FormuleDTO> formules = formuleService.getAllFormules();
        List<ValiseDTO> valises = valiseService.getAllValises();
        List<TypeRegleDTO> typesRegle = typeRegleService.getTypeRegles();
        List<JourFerieDTO> jourFerie = jourFerieService.getJourFeries();

        model.addAttribute("formules", formules != null ? formules : new ArrayList<>());
        model.addAttribute("valises", valises != null ? valises : new ArrayList<>());
        model.addAttribute("typesRegle", typesRegle != null ? typesRegle : new ArrayList<>());
        model.addAttribute("jourFerie", jourFerie != null ? jourFerie : new ArrayList<>());

        return "regles/regle_create";
    }


    @PostMapping("/create")
    public String createRegle(@ModelAttribute RegleDTO regleDTO) {
        regleService.createRegle(regleDTO);
        return "redirect:/regles/list";
    }

    @GetMapping("/edit/{id}")
    public String editRegle(@PathVariable int id, Model model) {
        RegleDTO regle = regleService.getRegleById(id);
        model.addAttribute("regle", new RegleDTO());

        List<FormuleDTO> formules = formuleService.getAllFormules();
        List<ValiseDTO> valises = valiseService.getAllValises();
        List<TypeRegleDTO> typesRegle = typeRegleService.getTypeRegles();
        List<JourFerieDTO> jourFerie = jourFerieService.getJourFeries();

        model.addAttribute("formules", formules != null ? formules : new ArrayList<>());
        model.addAttribute("valises", valises != null ? valises : new ArrayList<>());
        model.addAttribute("typesRegle", typesRegle != null ? typesRegle : new ArrayList<>());
        model.addAttribute("jourFerie", jourFerie != null ? jourFerie : new ArrayList<>());


        return "regles/regle_edit";
    }

    @PostMapping("/edit/{id}")
    public String updateRegle(@PathVariable int id, @ModelAttribute RegleDTO regleDTO) {
        regleService.updateRegle(id, regleDTO);
        return "redirect:/regles/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteRegle(@PathVariable int id) {
        regleService.deleteRegle(id);
        return "redirect:/regles/list";
    }
}
