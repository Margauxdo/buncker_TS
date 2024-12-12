package example.controller;

import example.DTO.*;
import example.interfaces.IMouvementService;
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
        model.addAttribute("valises", valiseService.getAllValises());
        return "mouvements/mouv_details";
    }


    @GetMapping("/create")
    public String createMouvementForm(Model model) {
        MouvementDTO mouvementDTO = new MouvementDTO();

        List<LivreurDTO> allLivreurs = livreurService.getAllLivreurs();
        if (allLivreurs.isEmpty()) {
            throw new IllegalStateException("Aucun livreur disponible");
        }

        List<ValiseDTO> valisesDTO = valiseService.getAllValises();

        model.addAttribute("mouvement", mouvementDTO);
        model.addAttribute("valises", valisesDTO);
        model.addAttribute("allLivreurs", allLivreurs);
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

    @GetMapping("/edit/{id}")
    public String updateMouvement(@PathVariable int id, Model model) {

        try {
            MouvementDTO mouvementDTO = mouvementService.getMouvementById(id);
            List<ValiseDTO> valisesDTO = valiseService.getAllValises();
            List<LivreurDTO> allLivreursDTO = livreurService.getAllLivreurs();
            List<RetourSecuriteDTO> allRetourSecuritesDTO = retourSecuriteService.getAllRetourSecurites();

            model.addAttribute("mouvement", mouvementDTO);
            model.addAttribute("valises", valisesDTO);
            model.addAttribute("allLivreurs", allLivreursDTO);
            model.addAttribute("allRetourSecurites", allRetourSecuritesDTO);

            return "mouvements/mouv_edit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Mouvement avec l'ID " + id + " non trouvé.");
            return "mouvements/error";
        }

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
