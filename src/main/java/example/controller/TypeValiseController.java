package example.controller;

import example.DTO.TypeValiseDTO;
import example.interfaces.ITypeValiseService;
import example.interfaces.IValiseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/typeValise")
public class TypeValiseController {

    @Autowired
    private ITypeValiseService typeValiseService;

    @Autowired
    private IValiseService valiseService;

    // Vue Thymeleaf pour lister les TV
    @GetMapping("/list")
    public String viewTypeValises(Model model) {
        List<TypeValiseDTO> typeValises = typeValiseService.getTypeValises();
        model.addAttribute("typeValises", typeValises);
        return "typeValises/TV_list";
    }

    // Vue Thymeleaf pour voir un TV par ID
    @GetMapping("/view/{id}")
    public String viewTypeValiseById(@PathVariable int id, Model model) {
        try {
            TypeValiseDTO typeValise = typeValiseService.getTypeValise(id);
            model.addAttribute("typeValise", typeValise);
            return "typeValises/TV_details";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "typeValises/error";
        }
    }

    // Formulaire Thymeleaf pour créer une TV
    @GetMapping("/create")
    public String createTypeValiseForm(Model model) {
        model.addAttribute("typeValise", new TypeValiseDTO());
        model.addAttribute("valises", valiseService.getAllValises()); // Vérifiez que cette méthode renvoie les valises correctement
        return "typeValises/TV_create";
    }


    @PostMapping("/create")
    public String createTypeValise(@Valid @ModelAttribute("typeValise") TypeValiseDTO typeValiseDTO, Model model) {
        try {
            typeValiseService.createTypeValise(typeValiseDTO);
            return "redirect:/typeValise/list";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("valises", valiseService.getAllValises());
            return "typeValises/TV_create";
        }
    }

    // Formulaire Thymeleaf pour modifier un TV
    @GetMapping("/edit/{id}")
    public String editTypeValiseForm(@PathVariable int id, Model model) {
        try {
            TypeValiseDTO typeValise = typeValiseService.getTypeValise(id);
            model.addAttribute("typeValise", typeValise);
            model.addAttribute("valises", valiseService.getAllValises());
            return "typeValises/TV_edit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "typeValises/error";
        }
    }


    @PostMapping("/edit/{id}")
    public String editTypeValise(@PathVariable int id, @Valid @ModelAttribute("typeValise") TypeValiseDTO typeValiseDTO, Model model) {
        try {
            typeValiseService.updateTypeValise(id, typeValiseDTO);
            return "redirect:/typeValise/list";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("valises", valiseService.getAllValises());
            return "typeValises/TV_edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteTypeValise(@PathVariable int id, Model model) {
        try {
            typeValiseService.deleteTypeValise(id);
            return "redirect:/typeValise/list";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "typeValises/error";
        }
    }
}
