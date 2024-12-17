package example.controller;

import example.DTO.RegleManuelleDTO;
import example.interfaces.IRegleManuelleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reglemanuelle")
public class RegleManuelleController {

    private final IRegleManuelleService regleManuelleService;

    public RegleManuelleController(IRegleManuelleService regleManuelleService) {
        this.regleManuelleService = regleManuelleService;
    }

    @GetMapping("/list")
    public String listRegleManuelles(Model model) {
        List<RegleManuelleDTO> regleManuelles = regleManuelleService.getRegleManuelles();
        model.addAttribute("regleManuelles", regleManuelles);
        return "reglesManuelles/RM_list";
    }

    @GetMapping("/view/{id}")
    public String viewRegleManuelleById(@PathVariable int id, Model model) {
        try {
            RegleManuelleDTO regleManuelle = regleManuelleService.getRegleManuelle(id);
            model.addAttribute("regleManuelle", regleManuelle);
            return "reglesManuelles/RM_details";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Règle manuelle introuvable.");
            return "reglesManuelles/error";
        }
    }

    @GetMapping("/create")
    public String createRegleManuelleForm(Model model) {
        model.addAttribute("regleManuelle", new RegleManuelleDTO());
        return "reglesManuelles/RM_create";
    }

    @PostMapping("/create")
    public String createRegleManuelle(@Valid @ModelAttribute("regleManuelle") RegleManuelleDTO regleManuelleDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "reglesManuelles/RM_create";
        }
        regleManuelleService.createRegleManuelle(regleManuelleDTO);
        return "redirect:/reglemanuelle/list";
    }

    @GetMapping("/edit/{id}")
    public String editRegleManuelleForm(@PathVariable int id, Model model) {
        try {
            RegleManuelleDTO regleManuelle = regleManuelleService.getRegleManuelle(id);
            model.addAttribute("regleManuelle", regleManuelle);
            return "reglesManuelles/RM_edit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Règle manuelle introuvable.");
            return "reglesManuelles/error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updatedRegleManuelle(@PathVariable int id, @Valid @ModelAttribute("regleManuelle") RegleManuelleDTO regleManuelleDTO) {
        regleManuelleService.updateRegleManuelle(id, regleManuelleDTO);
        return "redirect:/reglemanuelle/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteRegleManuelle(@PathVariable int id) {
        regleManuelleService.deleteRegleManuelle(id);
        return "redirect:/reglemanuelle/list";
    }
}
