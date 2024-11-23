package example.controller;

import example.entity.Livreur;
import example.exceptions.ConflictException;
import example.interfaces.ILivreurService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livreurs")
public class LivreurController {

    @Autowired
    private ILivreurService livreurService;

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

   /* // API REST: Récupérer tous les livreurs
    @GetMapping("/api")
    public ResponseEntity<List<Livreur>> getAllLivreursApi() {
        List<Livreur> livreurs = livreurService.getAllLivreurs();
        return ResponseEntity.ok(livreurs);
    }

    // API REST: Récupérer un livreur par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<Livreur> getLivreurByIdApi(@PathVariable int id) {
        Livreur livreur = livreurService.getLivreurById(id);
        return livreur != null ? ResponseEntity.ok(livreur) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // API REST: Créer un livreur
    @PostMapping("/api")
    public ResponseEntity<Livreur> createLivreurApi(@RequestBody Livreur livreur) {
        try {
            Livreur createdLivreur = livreurService.createLivreur(livreur);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLivreur);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // API REST: Modifier un livreur
    @PutMapping("/api/{id}")
    public ResponseEntity<Livreur> updateLivreurApi(@PathVariable int id, @RequestBody Livreur livreur) {
        if (livreur.getNomLivreur() == null || livreur.getNomLivreur().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        livreur.setId(id);
        Livreur updatedLivreur = livreurService.updateLivreur(id, livreur);
        return updatedLivreur != null ? ResponseEntity.ok(updatedLivreur) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // API REST: Supprimer un livreur
    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> deleteLivreurApi(@PathVariable int id) {
        try {
            livreurService.deleteLivreur(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/

    // Vue Thymeleaf pour lister les livreurs
    @GetMapping("/list")
    public String listLivreurs(Model model) {
        model.addAttribute("livreurs", livreurService.getAllLivreurs());
        return "livreurs/livreur_list";
    }

    // Vue Thymeleaf pour voir un livreur par ID
    @GetMapping("/view/{id}")
    public String viewLivreurById(@PathVariable int id, Model model) {
        Livreur livreur = livreurService.getLivreurById(id);
        if (livreur == null) {
            model.addAttribute("errorMessage", "Livreur avec l'ID " + id + " non trouvé.");
            return "livreurs/error";
        }
        model.addAttribute("livreur", livreur);
        return "livreurs/livreur_details";
    }

    // Formulaire Thymeleaf pour créer un livreur
    @GetMapping("/create")
    public String createLivreurForm(Model model) {
        model.addAttribute("livreur", new Livreur());
        return "livreurs/livreur_create";
    }

    // Création d'un livreur via formulaire Thymeleaf
    @PostMapping("/create")
    public String createLivreur(@Valid @ModelAttribute("livreur") Livreur livreur) {
        livreurService.createLivreur(livreur);
        return "redirect:/livreurs/livreur_list";
    }

    // Formulaire Thymeleaf pour modifier un livreur
    @GetMapping("/edit/{id}")
    public String editLivreurForm(@PathVariable int id, Model model) {
        Livreur livreur = livreurService.getLivreurById(id);
        if (livreur == null) {
            return "livreurs/error";
        }
        model.addAttribute("livreur", livreur);
        return "livreurs/livreur_edit";
    }

    // Modifier un livreur via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateLivreur(@PathVariable int id, @Valid @ModelAttribute("livreur") Livreur livreur) {
        livreurService.updateLivreur(id, livreur);
        return "redirect:/livreurs/livreur_list";
    }

    // Supprimer un livreur via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public String deleteLivreur(@PathVariable int id) {
        livreurService.deleteLivreur(id);
        return "redirect:/livreurs/livreur_list";
    }
}
