package example.controller;

import example.entity.Livreur;
import example.exceptions.ConflictException;
import example.interfaces.ILivreurService;
import example.repositories.MouvementRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/livreurs")
public class LivreurController {

    @Autowired
    private ILivreurService livreurService;
    @Autowired
    private MouvementRepository mouvementRepository;

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
        Livreur livreur = new Livreur();
        livreur.setCodeLivreur(""); // Valeur par défaut
        livreur.setMotDePasse(""); // Valeur par défaut
        livreur.setNomLivreur(""); // Valeur par défaut
        livreur.setPrenomLivreur(""); // Valeur par défaut
        livreur.setMouvement(null); // Mouvement peut être vide
        model.addAttribute("livreur", livreur);
        return "livreurs/livreur_create";
    }




    // Création d'un livreur via formulaire Thymeleaf
    @PostMapping("/create")
    public String createLivreur(@Valid @ModelAttribute Livreur livreur, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Veuillez corriger les erreurs dans le formulaire.");
            return "livreurs/livreur_create"; // Retourner au formulaire avec un message d'erreur
        }
        try {
            livreurService.createLivreur(livreur);
            return "redirect:/livreurs/list"; // Redirection vers la liste en cas de succès
        } catch (ConflictException e) {
            model.addAttribute("errorMessage", "Un livreur avec ce code existe déjà.");
            return "livreurs/livreur_create"; // Retourner au formulaire avec un message d'erreur spécifique
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite.");
            return "livreurs/livreur_create"; // Retourner au formulaire avec un message générique
        }
    }




    // Formulaire Thymeleaf pour modifier un livreur
    @GetMapping("/edit/{id}")
    public String editLivreurForm(@PathVariable int id, Model model) {
        try {
            Livreur livreur = livreurService.getLivreurById(id);
            model.addAttribute("livreur", livreur);
            return "livreurs/livreur_edit";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors du chargement de la valise : " + e.getMessage());
            return "livreurs/error";
        }
    }






    // Modifier un livreur via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateLivreur(@PathVariable int id, @Valid @ModelAttribute("livreur") Livreur livreur, Model model) {
        try {
            if (livreur.getNomLivreur() == null || livreur.getNomLivreur().isEmpty()) {
                model.addAttribute("errorMessage", "Le nom du livreur est obligatoire.");
                return "livreurs/livreur_edit";
            }
            if (livreur.getMouvement() == null || livreur.getMouvement().getId() == 0) {
                model.addAttribute("errorMessage", "Un mouvement valide est obligatoire.");
                return "livreurs/livreur_edit";
            }
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

