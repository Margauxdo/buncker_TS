package example.controller;

import example.entity.Livreur;
import example.exceptions.ConflictException;
import example.interfaces.ILivreurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/livreurs")
public class LivreurController {

    @Autowired
    private ILivreurService livreurService;

    // Vue Thymeleaf pour lister les livreurs
    @GetMapping("/list")
    public String listLivreurs(Model model) {
        try {
            model.addAttribute("livreurs", livreurService.getAllLivreurs());
            return "livreurs/livreur_list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur s'est produite lors de la récupération des livreurs.");
            return "livreurs/error";
        }
    }

    // Vue Thymeleaf pour voir un livreur par ID
    @GetMapping("/view/{id}")
    public String viewLivreurById(@PathVariable int id, Model model) {
        try {
            Livreur livreur = livreurService.getLivreurById(id);
            if (livreur == null) {
                model.addAttribute("errorMessage", "Livreur avec l'ID " + id + " non trouvé.");
                return "livreurs/error";
            }
            model.addAttribute("livreur", livreur);
            return "livreurs/livreur_details";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite.");
            return "livreurs/error";
        }
    }

    // Formulaire Thymeleaf pour créer un livreur
    @GetMapping("/create")
    public String createLivreurForm(Model model) {
        model.addAttribute("livreur", new Livreur());
        return "livreurs/livreur_create";
    }

    // Création d'un livreur via formulaire Thymeleaf
    @PostMapping("/create")
    public String createLivreur(@Valid @ModelAttribute("livreur") Livreur livreur, Model model) {
        try {
            livreurService.createLivreur(livreur);
            return "redirect:/livreurs/list";
        } catch (ConflictException e) {
            model.addAttribute("errorMessage", "Un conflit s'est produit lors de la création du livreur.");
            return "livreurs/livreur_create";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite.");
            return "livreurs/livreur_create";
        }
    }

    // Formulaire Thymeleaf pour modifier un livreur
    @GetMapping("/edit/{id}")
    public String editLivreurForm(@PathVariable int id, Model model) {
        try {
            Livreur livreur = livreurService.getLivreurById(id);
            if (livreur == null) {
                model.addAttribute("errorMessage", "Livreur avec l'ID " + id + " non trouvé.");
                return "livreurs/error";
            }
            model.addAttribute("livreur", livreur);
            return "livreurs/livreur_edit";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite.");
            return "livreurs/error";
        }
    }

    // Modifier un livreur via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateLivreur(@PathVariable int id, @Valid @ModelAttribute("livreur") Livreur livreur, Model model) {
        try {
            livreurService.updateLivreur(id, livreur);
            return "redirect:/livreurs/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite lors de la mise à jour.");
            return "livreurs/livreur_edit";
        }
    }

    // Supprimer un livreur via un formulaire Thymeleaf sécurisé
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

