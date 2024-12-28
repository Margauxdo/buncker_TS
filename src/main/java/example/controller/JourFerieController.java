package example.controller;

import example.DTO.JourFerieDTO;
import example.services.JourFerieService;
import example.services.RegleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/jourferies")
public class JourFerieController {

    @Autowired
    private JourFerieService jourFerieService;

    private static final Logger logger = LoggerFactory.getLogger(JourFerieController.class);
    @Autowired
    private RegleService regleService;

    @GetMapping("/list")
    public String viewJFDateList( Model model) {
        List<JourFerieDTO> jourFeries = jourFerieService.getJourFeries();
        model.addAttribute("jourFeries", jourFeries);
        return "joursFeries/JF_list";
    }


    @GetMapping("/create")
    public String createJourFerieForm(Model model) {
        model.addAttribute("jourFerie", new JourFerieDTO());
        return "joursFeries/JF_create";
    }


    @PostMapping("/create")
    public String createJourFerie(
            @Valid @ModelAttribute("jourFerie") JourFerieDTO jourFerieDTO,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "joursFeries/JF_create";
        }
        jourFerieService.saveJourFerie(jourFerieDTO);
        return "redirect:/jourferies/list";
    }


    @PostMapping("/delete/{id}")
    public String deleteJourFerie(@PathVariable int id) {
        jourFerieService.deleteJourFerie(id);
        return "redirect:/jourferies/list";
    }

}
