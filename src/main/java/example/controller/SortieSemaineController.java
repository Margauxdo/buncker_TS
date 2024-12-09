package example.controller;

import example.DTO.SortieSemaineDTO;
import example.entity.SortieSemaine;
import example.interfaces.ISortieSemaineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sortieSemaine")
public class SortieSemaineController {

    @Autowired
    private ISortieSemaineService sortieSemaineService;

    // Vue Thymeleaf pour lister les SortieSemaine
    @GetMapping("/list")
    public String viewSortieSemaine(Model model) {
        List<SortieSemaineDTO> sortieSemaines = sortieSemaineService.getAllSortieSemaine();
        model.addAttribute("sortieSemaine", sortieSemaines);
        return "sortieSemaines/SS_list";
    }

    // Vue Thymeleaf pour voir une SortieSemaine par ID
    @GetMapping("/view/{id}")
    public String viewSortieSemaineById(@PathVariable int id, Model model) {
        SortieSemaineDTO sortieSemaine = sortieSemaineService.getSortieSemaine(id);
        if (sortieSemaine == null) {
            model.addAttribute("errormessage", "SortieSemaine avec l'ID " + id + " non trouvée");
            return "sortieSemaines/error";
        }
        model.addAttribute("sortieSemaine", sortieSemaine);
        return "sortieSemaines/SS_details";
    }

    // Formulaire Thymeleaf pour créer une SortieSemaine
    @GetMapping("/create")
    public String createSortieSemaineForm(Model model) {
        model.addAttribute("sortieSemaine", new SortieSemaineDTO());
        return "sortieSemaines/SS_create";
    }

    @PostMapping("/create")
    public String createSortieSemaine(@ModelAttribute("sortieSemaine") SortieSemaineDTO sortieSemaineDTO) {
        sortieSemaineService.createSortieSemaine(sortieSemaineDTO);
        return "redirect:/sortieSemaine/list";
    }

    // Formulaire Thymeleaf pour modifier une SortieSemaine
    @GetMapping("/edit/{id}")
    public String editSortieSemaineForm(@PathVariable int id, Model model) {
        SortieSemaineDTO sortieSemaine = sortieSemaineService.getSortieSemaine(id);
        if (sortieSemaine == null) {
            return "sortieSemaines/error";
        }
        model.addAttribute("sortieSemaine", sortieSemaine);
        return "sortieSemaines/SS_edit";
    }

    @PostMapping("/edit/{id}")
    public String updateSortieSemaine(@PathVariable int id, @ModelAttribute("sortieSemaine") SortieSemaine sortieSemaineDTO) {
        sortieSemaineService.updateSortieSemaine(id, sortieSemaineDTO);
        return "redirect:/sortieSemaine/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteSortieSemaine(@PathVariable int id) {
        sortieSemaineService.deleteSortieSemaine(id);
        return "redirect:/sortieSemaine/list";
    }
}
