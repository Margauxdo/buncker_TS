package example.controller;

import example.DTO.TypeRegleDTO;
import example.interfaces.ITypeRegleService;
import example.repositories.RegleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/typeRegle")
public class TypeRegleController {

    @Autowired
    private ITypeRegleService typeRegleService;

    @Autowired
    private RegleRepository regleRepository;

    @GetMapping("/list")
    public String viewAllTypeRegles(Model model) {
        List<TypeRegleDTO> typeRegles = typeRegleService.getTypeRegles();
        model.addAttribute("typeRegles", typeRegles);
        return "typeRegles/TR_list";
    }

    @GetMapping("/view/{id}")
    public String viewTypeRegleById(@PathVariable int id, Model model) {
        try {
            TypeRegleDTO typeRegle = typeRegleService.getTypeRegle(id);
            model.addAttribute("typeRegle", typeRegle);
            return "typeRegles/TR_details";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "typeRegles/error";
        }
    }

    @GetMapping("/create")
    public String createTypeRegleForm(Model model) {
        model.addAttribute("typeRegle", new TypeRegleDTO());
        model.addAttribute("regles", regleRepository.findAll());  // Assurez-vous que toutes les règles sont disponibles
        return "typeRegles/TR_create";
    }

    @PostMapping("/create")
    public String createTypeRegle(@Valid @ModelAttribute("typeRegle") TypeRegleDTO typeRegleDTO, Model model) {
        try {
            typeRegleService.createTypeRegle(typeRegleDTO);
            return "redirect:/typeRegle/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "typeRegles/TR_create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editTypeRegleForm(@PathVariable int id, Model model) {
        try {
            TypeRegleDTO typeRegle = typeRegleService.getTypeRegle(id);
            model.addAttribute("typeRegle", typeRegle);
            model.addAttribute("regles", regleRepository.findAll());
            return "typeRegles/TR_edit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "typeRegles/error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateTypeRegle(@PathVariable int id, @Valid @ModelAttribute("typeRegle") TypeRegleDTO typeRegleDTO, Model model) {
        try {
            typeRegleService.updateTypeRegle(id, typeRegleDTO);
            return "redirect:/typeRegle/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "typeRegles/TR_edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteTypeRegle(@PathVariable int id, Model model) {
        try {
            typeRegleService.deleteTypeRegle(id);
            return "redirect:/typeRegle/list";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "typeRegles/error";
        }
    }
}
