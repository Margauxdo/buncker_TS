package example.controller;

import example.DTO.TypeRegleDTO;
import example.interfaces.ITypeRegleService;
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

    @GetMapping("/list")
    public String viewAllTypeRegles(Model model) {
        List<TypeRegleDTO> typeRegles = typeRegleService.getTypeRegles();
        model.addAttribute("typeRegles", typeRegles);
        return "typeRegles/TR_list";
    }

    @GetMapping("/view/{id}")
    public String viewTypeRegleById(@PathVariable int id, Model model) {
        TypeRegleDTO typeRegle = typeRegleService.getTypeRegle(id);
        model.addAttribute("typeRegle", typeRegle);
        return "typeRegles/TR_details";
    }

    @GetMapping("/create")
    public String createTypeRegleForm(Model model) {
        model.addAttribute("typeRegle", new TypeRegleDTO());
        return "typeRegles/TR_create";
    }

    @PostMapping("/create")
    public String createTypeRegle(@Valid @ModelAttribute("typeRegle") TypeRegleDTO typeRegleDTO, Model model) {
        if (typeRegleDTO.getNomTypeRegle() == null || typeRegleDTO.getNomTypeRegle().isEmpty()) {
            model.addAttribute("errorMessage", "Le nom du type de règle ne peut pas être vide.");
            return "typeRegles/TR_create";
        }
        typeRegleService.createTypeRegle(typeRegleDTO);
        return "redirect:/typeRegle/list";
    }
    @PostMapping("/delete/{id}")
    public String deleteTypeRegle(@PathVariable int id, Model model) {
        try {
            typeRegleService.deleteTypeRegle(id);
            return "redirect:/typeRegle/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression du type regle : " + e.getMessage());
            return "typeRegles/error";
        }
    }

}
