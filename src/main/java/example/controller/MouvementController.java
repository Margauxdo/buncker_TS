package example.controller;

import example.entity.Mouvement;
import example.interfaces.IMouvementService;
import example.services.LivreurService;
import example.services.ValiseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/mouvements")
public class MouvementController {

    @Autowired
    private IMouvementService mouvementService;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(MouvementController.class);
    @Autowired
    private ValiseService valiseService;
    @Autowired
    private LivreurService livreurService;

    /*// API REST: Récupérer tous les mouvements
    @GetMapping("/api")
    public List<Mouvement> getAllMouvementsApi() {
        List<Mouvement> mouvements = mouvementService.getAllMouvements();
        return new ResponseEntity<>(mouvements, HttpStatus.OK).getBody();
    }
    // API REST: Récupérer un mouvement par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<Mouvement> getMouvementByIdApi(@PathVariable int id) {
        Mouvement mouvement = mouvementService.getMouvementById(id);
        return mouvement != null ? new ResponseEntity<>(mouvement, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // API REST: Créer un mouvement
        @PostMapping("/api")
        public ResponseEntity<Mouvement> createMouvementApi(@RequestBody Mouvement mouvement) {
            try {
                if (mouvement.getDateHeureMouvement() == null || mouvement.getStatutSortie() == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                Mouvement createdMouvement = mouvementService.createMouvement(mouvement);
                return new ResponseEntity<>(createdMouvement, HttpStatus.CREATED);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    // API REST: Modifier un mouvement

        @PutMapping("/api/{id}")
        public ResponseEntity<Mouvement> updateMouvementApi(@PathVariable int id, @RequestBody Mouvement mouvement) {
            try {
                if (!mouvementService.existsById(id)) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                Mouvement updatedMouvement = mouvementService.updateMouvement(id, mouvement);
                return new ResponseEntity<>(updatedMouvement, HttpStatus.OK);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


    // API REST: Supprimer un mouvement
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMouvementApi(@PathVariable int id) {
        try {
            if (!mouvementService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            mouvementService.deleteMouvement(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    // Vue Thymeleaf pour lister les mouvements
    @GetMapping("/list")
    public String listAllMouvements(Model model) {
        model.addAttribute("mouvements", mouvementService.getAllMouvements());
        return "mouvements/mouv_list";
    }

    // Vue Thymeleaf pour voir un mouvement par ID
    @GetMapping("/view/{id}")
    public String viewMouvementById(@PathVariable int id, Model model) {
        Mouvement mouvement = mouvementService.getMouvementById(id);
        if (mouvement == null) {
            model.addAttribute("errorMessage", "Mouvement avec l'ID " + id + " non trouvé.");
            return "mouvements/error";  // Retourne la vue d'erreur si le mouvement n'est pas trouvé
        }
        model.addAttribute("mouvement", mouvement);
        return "mouvements/mouv_details";  // Retourne la vue de détails si le mouvement est trouvé
    }



    // Formulaire Thymeleaf pour créer un mouvement
    @GetMapping("/create")
    public String createMouvementForm(Model model) {
        model.addAttribute("mouvement", new Mouvement());
        model.addAttribute("valises", valiseService.getAllValises()); // Assuming valiseService provides Valise data
        model.addAttribute("allLivreurs", livreurService.getAllLivreurs()); // Assuming livreurService provides Livreur data
        return "mouvements/mouv_create";
    }


    // Création d'un mouvement via formulaire Thymeleaf
    @PostMapping("/create")
    public String createMouvementThymeleaf(@Valid @ModelAttribute("mouvement") Mouvement mouvement, Model model) {
        try {
            mouvementService.createMouvement(mouvement);
            return "redirect:/mouvements/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to create Mouvement: " + e.getMessage());
            model.addAttribute("valises", valiseService.getAllValises());
            model.addAttribute("allLivreurs", livreurService.getAllLivreurs());
            return "mouvements/mouv_create";
        }
    }

    // Formulaire Thymeleaf pour modifier un mouvement
    @GetMapping("/edit/{id}")
    public String editMouvementForm(@PathVariable int id, Model model) {
        Mouvement mouvement = mouvementService.getMouvementById(id);
        if (mouvement == null) {
            model.addAttribute("errorMessage", "Mouvement avec l'ID " + id + " non trouvé.");
            return "mouvements/error";   }
        model.addAttribute("mouvement", mouvement);
        return "mouvements/mouv_edit";
    }



    @PostMapping("/edit/{id}")
    public String updateMouvement(@PathVariable int id, @Valid @ModelAttribute("mouvement") Mouvement mouvement, Model model) {
        try {
            mouvementService.updateMouvement(id, mouvement);
            return "redirect:/mouvements/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to update Mouvement: " + e.getMessage());
            model.addAttribute("valises", valiseService.getAllValises());
            model.addAttribute("allLivreurs", livreurService.getAllLivreurs());
            return "mouvements/mouv_edit";
        }
    }
        @PostMapping("/delete/{id}")
        public String deleteMouvement ( @PathVariable int id){
            mouvementService.deleteMouvement(id);
            return "redirect:/mouvements/mouv_list";
        }


    }



