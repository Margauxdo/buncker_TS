package example.controller;

import example.DTO.RegleDTO;
import example.interfaces.IRegleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/regles")
public class RegleController {

    @Autowired
    private IRegleService regleService;

    @GetMapping("/list")
    public String listRegles(Model model) {
        model.addAttribute("regles", regleService.getAllRegles());

//

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
        return "regles/regle_create";
    }

    @PostMapping("/create")
    public String createRegle(@ModelAttribute RegleDTO regleDTO) {
        regleService.createRegle(regleDTO);
        return "redirect:/regles/list";
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
