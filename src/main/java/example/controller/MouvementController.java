package example.controller;

import example.entity.Mouvement;
import example.entity.Valise;
import example.interfaces.IMouvementService;
import example.repositories.MouvementRepository;
import example.services.LivreurService;
import example.services.ValiseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    @Autowired
    private MouvementRepository mouvementRepository;


    @GetMapping("/view/{id}")
    public String viewMouvementDetails(@PathVariable Long id, Model model) {
        Mouvement mouvement = mouvementService.findByIdWithValise(id)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement not found"));

        // Initialize lazy collection if necessary
        Hibernate.initialize(mouvement.getLivreurs());

        model.addAttribute("mouvement", mouvement);
        return "mouvements/mouv_details";
    }





    @GetMapping("/create")
    public String createMouvementForm(Model model) {
        model.addAttribute("mouvement", new Mouvement());
        model.addAttribute("valises", valiseService.getAllValises());
        model.addAttribute("allLivreurs", livreurService.getAllLivreurs());
        return "mouvements/mouv_create";
    }


    @PostMapping("/create")
    public String createMouvementThymeleaf(
            @Valid @ModelAttribute("mouvement") Mouvement mouvement,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Veuillez corriger les erreurs.");
            model.addAttribute("valises", valiseService.getAllValises());
            return "mouvements/mouv_create";
        }
        mouvementService.createMouvement(mouvement);
        return "redirect:/mouvements/list";
    }


    @GetMapping("/edit/{id}")
    public String editMouvementForm(@PathVariable int id, Model model) {
        Mouvement mouvement = mouvementService.getMouvementById(id);
        if (mouvement == null) {
            model.addAttribute("errorMessage", "Mouvement avec l'ID " + id + " non trouvé.");
            return "mouvements/error";
        }
        model.addAttribute("mouvement", mouvement);
        return "mouvements/mouv_edit";
    }


    @PostMapping("/edit/{id}")
    public String updateMouvement(
            @PathVariable int id,
            @Valid @ModelAttribute("mouvement") Mouvement mouvement,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("valises", valiseService.getAllValises());
            return "mouvements/mouv_edit";
        }

        try {
            Mouvement existingMouvement = mouvementService.getMouvementById(id);
            if (existingMouvement == null) {
                model.addAttribute("errorMessage", "Mouvement avec l'ID " + id + " non trouvé.");
                return "mouvements/error";
            }

            existingMouvement.setStatutSortie(mouvement.getStatutSortie());
            existingMouvement.setDateSortiePrevue(mouvement.getDateSortiePrevue());
            existingMouvement.setDateRetourPrevue(mouvement.getDateRetourPrevue());

            if (mouvement.getValise() != null && mouvement.getValise().getId() > 0) {
                Valise valise = valiseService.getValiseById(mouvement.getValise().getId());
                existingMouvement.setValise(valise);
            } else {
                model.addAttribute("errorMessage", "Valise non définie ou invalide pour ce mouvement.");
                return "mouvements/mouv_edit";
            }


            mouvementService.updateMouvement(id, existingMouvement);
            return "redirect:/mouvements/list";

        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Erreur : Mouvement ou entités associées introuvables.");
            return "mouvements/error";
        }
    }




    @PostMapping("/delete/{id}")
    public String deleteMouvement(@PathVariable int id, Model model) {
        try {
            mouvementService.deleteMouvement(id);
            return "redirect:/mouvements/list";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Impossible de supprimer le mouvement avec l'ID " + id);
            return "mouvements/error";
        }
    }

    @GetMapping("/list")
    public String listAllMouvements(Model model) {
        List<Mouvement> mouvements = mouvementService.getAllMouvements();
        model.addAttribute("mouvements", mouvements);
        return "mouvements/mouv_list";
    }





}
