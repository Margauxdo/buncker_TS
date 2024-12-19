package example.controller;

import example.DTO.*;
import example.interfaces.IMouvementService;
import example.repositories.LivreurRepository;
import example.services.LivreurService;
import example.services.RetourSecuriteService;
import example.services.ValiseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
            return "mouvements/mouv_details";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mouvement introuvable : " + e.getMessage());
            return "redirect:/mouvements/list";
        }
    }






    @GetMapping("/create")
    public String createMouvementForm(Model model) {
        List<ValiseDTO> valises = valiseService.getAllValises();
        List<LivreurDTO> livreurs = livreurService.getAllLivreurs();
        List<RetourSecuriteDTO> retourSecurites = retourSecuriteService.getAllRetourSecurites();  // Assurez-vous que cette liste contient des éléments

        model.addAttribute("mouvement", new MouvementDTO());
        model.addAttribute("valises", valises);
        model.addAttribute("livreurs", livreurs);
        model.addAttribute("retourSecurites", retourSecurites);  // Assurez-vous que cette liste est correctement ajoutée
        return "mouvements/mouv_create";
    }



    @PostMapping("/create")
    public String createMouvement(@ModelAttribute("mouvement") @Valid MouvementDTO mouvementDTO,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "mouvements/mouv_create"; // Retourner le formulaire en cas d'erreurs
        }

        // Appeler la méthode du service pour créer le mouvement
        mouvementService.createMouvement(mouvementDTO);
        return "redirect:/mouvements/list";
    }





    @GetMapping("/edit/{id}")
    public String editMouvement(@PathVariable int id, Model model) {
        try {
            MouvementDTO mouvement = mouvementService.getMouvementById(id);
            List<ValiseDTO> valises = valiseService.getAllValises();
            List<LivreurDTO> livreurs = livreurService.getAllLivreurs();
            List<RetourSecuriteDTO> retourSecurites = retourSecuriteService.getAllRetourSecurites();

            model.addAttribute("mouvement", mouvement); // Assurez-vous que l'objet est bien passé ici
            model.addAttribute("valises", valises);
            model.addAttribute("livreurs", livreurs);
            model.addAttribute("allRetourSecurites", retourSecurites);

            return "mouvements/mouv_edit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Mouvement non trouvé : " + e.getMessage());
            return "clients/error";
        }
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
    public String deleteMouvement(@PathVariable int id, Model model) {
        try {
            System.out.println("Appel à la méthode de suppression pour le mouvement ID : " + id);
            mouvementService.deleteMouvement(id); // Suppression du mouvement
            return "redirect:/mouvements/list"; // Rediriger après la suppression
        } catch (Exception e) {
            System.out.println("Erreur de suppression : " + e.getMessage()); // Log des erreurs
            model.addAttribute("errorMessage", e.getMessage());
            return "clients/error"; // Retourner une page d'erreur en cas d'échec
        }
    }



}










