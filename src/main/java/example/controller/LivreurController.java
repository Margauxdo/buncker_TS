package example.controller;

import example.DTO.LivreurDTO;
import example.DTO.MouvementDTO;
import example.entity.Mouvement;
import example.interfaces.ILivreurService;
import example.repositories.MouvementRepository;
import example.services.MouvementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/livreurs")
public class LivreurController {

    @Autowired
    private ILivreurService livreurService;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private MouvementService mouvementService;

    @GetMapping("/list")
    public String listLivreurs(Model model) {
        model.addAttribute("livreurs", livreurService.getAllLivreurs());
        return "livreurs/livreur_list";
    }

    @GetMapping("/view/{id}")
    public String viewLivreurById(@PathVariable int id, Model model) {
        model.addAttribute("livreur", livreurService.getLivreurById(id)); // Retourne un DTO complet
        return "livreurs/livreur_details";
    }


    @GetMapping("/create")
    public String createLivreurForm(Model model) {
        model.addAttribute("livreur", new LivreurDTO());
        List<Mouvement> mouvements = mouvementService.getAllMouvementsWithRetourSecurites();
        model.addAttribute("mouvements", mouvements);
        return "livreurs/livreur_create";
    }



    @PostMapping("/create")
    public String createLivreur(@Valid @ModelAttribute("livreur") LivreurDTO livreurDTO,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "livreurs/livreur_create";
        }
        livreurService.createLivreur(livreurDTO);
        return "redirect:/livreurs/list";
    }

    @GetMapping("/edit/{id}")
    public String editLivreurForm(@PathVariable int id, Model model) {
        LivreurDTO livreur = livreurService.getLivreurById(id);
        List<MouvementDTO> mouvements = mouvementService.getAllMouvements(); // Fetch all movements
        model.addAttribute("livreur", livreur);
        model.addAttribute("mouvements", mouvements);
        return "livreurs/livreur_edit";
    }


    @PostMapping("/edit/{id}")
    public String updateLivreur(@PathVariable int id, @Valid @ModelAttribute("livreur") LivreurDTO livreurDTO,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "livreurs/livreur_edit";
        }
        try {
            livreurService.updateLivreur(id, livreurDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour du livreur.");
            return "livreurs/error";
        }
        return "redirect:/livreurs/list";
    }


    @PostMapping("/delete/{id}")
    public String deleteLivreur(@PathVariable int id, Model model) {
        try {
            livreurService.deleteLivreur(id);
            return "redirect:/livreurs/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite lors de la suppression.");
            return "livreurs/error";
        }
    }

}
