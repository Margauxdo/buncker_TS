package example.controller;

import example.DTO.RetourSecuriteDTO;
import example.interfaces.IRetourSecuriteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/retourSecurite")
public class RetourSecuriteController {

    private final IRetourSecuriteService retourSecuriteService;

    public RetourSecuriteController(IRetourSecuriteService retourSecuriteService) {
        this.retourSecuriteService = retourSecuriteService;
    }

    @GetMapping("/list")
    public String viewAllRetourSecurites(Model model) {
        List<RetourSecuriteDTO> retourSecurites = retourSecuriteService.getAllRetourSecurites();
        model.addAttribute("retourSecurites", retourSecurites);
        return "retourSecurites/RS_list";
    }

    @GetMapping("/view/{id}")
    public String viewRetourSecuriteById(@PathVariable int id, Model model) {
        RetourSecuriteDTO retourSecurite = retourSecuriteService.getRetourSecurite(id);
        model.addAttribute("retourSecurite", retourSecurite);
        return "retourSecurites/RS_details";
    }

    @GetMapping("/create")
    public String createRetourSecuriteForm(Model model) {
        model.addAttribute("retourSecurite", new RetourSecuriteDTO());
        return "retourSecurites/RS_create";
    }

    @PostMapping("/create")
    public String createRetourSecurite(@Valid @ModelAttribute("retourSecurite") RetourSecuriteDTO retourSecuriteDTO) {
        retourSecuriteService.createRetourSecurite(retourSecuriteDTO);
        return "redirect:/retourSecurite/list";
    }

    @GetMapping("/edit/{id}")
    public String editRetourSecuriteForm(@PathVariable int id, Model model) {
        RetourSecuriteDTO retourSecurite = retourSecuriteService.getRetourSecurite(id);
        model.addAttribute("retourSecurite", retourSecurite);
        return "retourSecurites/RS_edit";
    }

    @PostMapping("/edit/{id}")
    public String updateRetourSecurite(@PathVariable int id, @Valid @ModelAttribute("retourSecurite") RetourSecuriteDTO retourSecuriteDTO) {
        retourSecuriteService.updateRetourSecurite(id, retourSecuriteDTO);
        return "redirect:/retourSecurite/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteRetourSecurite(@PathVariable int id) {
        retourSecuriteService.deleteRetourSecurite(id);
        return "redirect:/retourSecurite/list";
    }
}
