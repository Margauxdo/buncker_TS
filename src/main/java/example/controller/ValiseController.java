package example.controller;

import example.DTO.ValiseDTO;
import example.interfaces.IValiseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/valise")
public class ValiseController {

    @Autowired
    private IValiseService valiseService;

    @GetMapping("/list")
    public String viewValises(Model model) {
        List<ValiseDTO> valises = valiseService.getAllValises();
        model.addAttribute("valises", valises);
        return "valises/valises_list";
    }

    @GetMapping("/view/{id}")
    public String viewValise(@PathVariable int id, Model model) {
        try {
            ValiseDTO valise = valiseService.getValiseById(id);
            model.addAttribute("valise", valise);
            return "valises/valise_details";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors du chargement de la valise : " + e.getMessage());
            return "valises/error";
        }
    }

    @GetMapping("/create")
    public String createValiseForm(Model model) {
        model.addAttribute("valise", new ValiseDTO());
        return "valises/valise_create";
    }

    @PostMapping("/create")
    public String createValise(@Valid @ModelAttribute("valise") ValiseDTO valiseDTO, Model model) {
        try {
            valiseService.createValise(valiseDTO);
            return "redirect:/valise/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la création de la valise : " + e.getMessage());
            return "valises/error";
        }
    }

    @GetMapping("/edit/{id}")
    public String editValiseForm(@PathVariable int id, Model model) {
        try {
            ValiseDTO valise = valiseService.getValiseById(id);
            model.addAttribute("valise", valise);
            return "valises/valise_edit";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors du chargement de la valise : " + e.getMessage());
            return "valises/error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateValise(@PathVariable int id, @Valid @ModelAttribute("valise") ValiseDTO valiseDTO, Model model) {
        try {
            valiseService.updateValise(id, valiseDTO);
            return "redirect:/valise/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la modification de la valise : " + e.getMessage());
            return "valises/error";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteValise(@PathVariable int id, Model model) {
        try {
            valiseService.deleteValise(id);
            return "redirect:/valise/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression de la valise : " + e.getMessage());
            return "valises/error";
        }
    }
}
