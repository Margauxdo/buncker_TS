package example.controller;

import example.DTO.MouvementDTO;
import example.interfaces.IMouvementService;
import example.services.LivreurService;
import example.services.ValiseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mouvements")
public class MouvementController {

    @Autowired
    private IMouvementService mouvementService;
    @Autowired
    private ValiseService valiseService;
    @Autowired
    private LivreurService livreurService;

    @GetMapping("/list")
    public String listAllMouvements(Model model) {
        List<MouvementDTO> mouvements = mouvementService.getAllMouvements();
        model.addAttribute("mouvements", mouvements);
        return "mouvements/mouv_list";
    }

    @GetMapping("/view/{id}")
    public String viewMouvementDetails(@PathVariable int id, Model model) {
        MouvementDTO mouvement = mouvementService.getMouvementById(id);
        model.addAttribute("mouvement", mouvement);
        return "mouvements/mouv_details";
    }

    @GetMapping("/create")
    public String createMouvementForm(Model model) {
        model.addAttribute("mouvement", new MouvementDTO());
        return "mouvements/mouv_create";
    }

    @PostMapping("/create")
    public String createMouvementThymeleaf(
            @Valid @ModelAttribute("mouvement") MouvementDTO mouvementDTO,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Veuillez corriger les erreurs.");
            model.addAttribute("valises", valiseService.getAllValises());
            model.addAttribute("allLivreurs", livreurService.getAllLivreurs());
            return "mouvements/mouv_create";
        }

        mouvementService.createMouvement(mouvementDTO);
        return "redirect:/mouvements/list";
    }


    @PostMapping("/edit/{id}")
    public String updateMouvement(@PathVariable int id, @ModelAttribute("mouvement") MouvementDTO mouvementDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "mouvements/mouv_edit";
        }
        mouvementService.updateMouvement(id, mouvementDTO);
        return "redirect:/mouvements/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteMouvement(@PathVariable int id, ConcurrentModel concurrentModel) {
        mouvementService.deleteMouvement(id);
        return "redirect:/mouvements/list";
    }


}
