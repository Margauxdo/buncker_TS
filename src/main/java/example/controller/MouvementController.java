package example.controller;

import example.DTO.*;
import example.entity.Livreur;
import example.entity.Mouvement;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.IMouvementService;
import example.repositories.LivreurRepository;
import example.services.LivreurService;
import example.services.RetourSecuriteService;
import example.services.ValiseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
    private RetourSecuriteService retourSecuriteService;
    @Autowired
    private LivreurRepository livreurRepository;

    @GetMapping("/list")
    public String listAllMouvements(Model model) {
        List<MouvementDTO> mouvements = mouvementService.getAllMouvements();
        model.addAttribute("mouvements", mouvements);
        return "mouvements/mouv_list";
    }

    @GetMapping("/view/{id}")
    public String viewMouvementDetails(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            MouvementDTO mouvement = mouvementService.getMouvementById(id);
            model.addAttribute("mouvement", mouvement);
            model.addAttribute("valises", valiseService.getAllValises());
            return "mouvements/mouv_details";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mouvement not found: " + e.getMessage());
            return "redirect:/mouvements/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unexpected error: " + e.getMessage());
            return "redirect:/mouvements/list";
        }
    }




    @GetMapping("/create")
    public String createMouvementForm(Model model) {
        // Récupérer les listes des valises et livreurs
        List<ValiseDTO> valises = valiseService.getAllValises();
        List<LivreurDTO> livreurs = livreurService.getAllLivreurs();

        // Ajouter les listes et l'objet mouvement au modèle
        model.addAttribute("valises", valises);
        model.addAttribute("livreurs", livreurs);
        model.addAttribute("mouvement", new MouvementDTO());
        return "mouvements/mouv_create";
    }





    /*@PostMapping("/create")
    public String createMouvement(@ModelAttribute @Valid MouvementDTO mouvementDTO, BindingResult result, Model model) {
       try {
           mouvementService.createMouvement(mouvementDTO);
           return "redirect:/mouvements/list";
       }catch (IllegalArgumentException e){
           model.addAttribute("errorMessage", e.getMessage());
           model.addAttribute("mouvement", mouvementDTO);
           return "clients/error";
       } catch (Exception e) {

           model.addAttribute("errorMessage", "Erreur inattendue : " + e.getMessage());
           return "clients/error";
       }
    }*/
    @PostMapping("/create")
    public String createMouvement(@ModelAttribute("mouvement") @Valid MouvementDTO mouvementDTO,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "mouvements/mouv_create";
        }
        mouvementService.createMouvement(mouvementDTO);
        return "redirect:/mouvements/list";
    }







    @GetMapping("/edit/{id}")
    public String editMouvement(@PathVariable int id, Model model) {
        MouvementDTO mouvement = mouvementService.getMouvementById(id);
        List<ValiseDTO> valises = valiseService.getAllValises();
        List<LivreurDTO> livreurs = livreurService.getAllLivreurs();
        List<RetourSecuriteDTO> retourSecurites = retourSecuriteService.getAllRetourSecurites();

        model.addAttribute("mouvement", mouvement);
        model.addAttribute("valises", valises);
        model.addAttribute("livreurs", livreurs);

        model.addAttribute("allRetourSecurites", retourSecurites);

        return "mouvements/mouv_edit";
    }

    @PostMapping("/edit/{id}")
    public String updateMouvement(@PathVariable int id, @ModelAttribute("mouvement") MouvementDTO mouvementDTO, BindingResult result, Model model) {
        try {
            mouvementService.updateMouvement(id, mouvementDTO);
            return "redirect:/mouvements/list";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("mouvement", mouvementDTO);
            return "clients/error";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteMouvement(@PathVariable int id,Model model) {
        try {
            mouvementService.deleteMouvement(id);
            return "redirect:/mouvements/list";
        }catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "clients/error";
        }


    }


}
