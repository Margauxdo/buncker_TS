package example.controller;

import example.DTO.LivreurDTO;
import example.DTO.MouvementDTO;
import example.entity.Mouvement;
import example.interfaces.ILivreurService;
import example.repositories.MouvementRepository;
import example.services.LivreurService;
import example.services.MouvementService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/livreurs")
public class LivreurController {

    private static final Logger logger = LoggerFactory.getLogger(LivreurController.class);

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

        // Ajouter les mouvements
        List<MouvementDTO> mouvements = mouvementService.getAllMouvements(); // Vérifiez cette méthode
        model.addAttribute("mouvements", mouvements);

        return "livreurs/livreur_create";
    }


    @PostMapping("/create")
    public String createLivreur(@Valid @ModelAttribute("livreur") LivreurDTO livreurDTO,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<MouvementDTO> mouvements = mouvementService.getAllMouvements();
            model.addAttribute("mouvements", mouvements); // Rechargez la liste des mouvements en cas d'erreur
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
        logger.info("Données pour modification chargées: {}", livreur);
        return "livreurs/livreur_edit";
    }


    @PostMapping("/edit/{id}")
    public String updateLivreur(@PathVariable int id, @Valid @ModelAttribute("livreur") LivreurDTO livreurDTO,
                                BindingResult result, Model model) {
        logger.info("Soumission du formulaire de modification pour le livreur avec ID: {}", id);
        if (result.hasErrors()) {
            logger.warn("Erreurs de validation détectées: {}", result.getAllErrors());
            List<MouvementDTO> mouvements = mouvementService.getAllMouvements();
            model.addAttribute("mouvements", mouvements);
            return "livreurs/livreur_edit";
        }
        try {
            logger.info("Mise à jour du livreur avec les données: {}", livreurDTO);
            livreurService.updateLivreur(id, livreurDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du livreur avec ID: {}", id, e);
            model.addAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour du livreur.");
            return "livreurs/error";
        }
        logger.info("Livreur mis à jour avec succès, redirection vers la liste.");
        return "redirect:/livreurs/list";
    }


    @PostMapping("/delete/{id}")
    public String deleteLivreur(@PathVariable int id, Model model) {
        try {
            livreurService.deleteLivreur(id);
            return "redirect:/livreurs/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression du livreur : " + e.getMessage());
            return "livreurs/error";
        }
    }


}
