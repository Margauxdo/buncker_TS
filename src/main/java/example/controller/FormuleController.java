package example.controller;

import example.DTO.FormuleDTO;
import example.exceptions.FormuleNotFoundException;
import example.interfaces.IFormuleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/formules")
public class FormuleController {

    @Autowired
    private IFormuleService formuleService;

    @GetMapping("/list")
    public String viewFormuleList(Model model) {
        model.addAttribute("formules", formuleService.getAllFormules());
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
        return "formules/formule_create";
    }

    @PostMapping("/create")
    public String createFormule(@Valid @ModelAttribute("formule") FormuleDTO formuleDTO, Model model) {
        formuleService.createFormule(formuleDTO);
        return "redirect:/formules/list";
    }

    @GetMapping("/edit/{id}")
    public String editFormuleForm(@PathVariable int id, Model model) {
        model.addAttribute("formule", formuleService.getFormuleById(id));
        return "formules/formule_edit";
    }

    @PostMapping("/edit/{id}")
    public String updateFormule(@PathVariable int id, @Valid @ModelAttribute("formule") FormuleDTO formuleDTO, Model model) {
        formuleService.updateFormule(id, formuleDTO);
        return "redirect:/formules/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteFormule(@PathVariable int id) {
        formuleService.deleteFormule(id);
        return "redirect:/formules/list";
    }
}
