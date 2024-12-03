package example.controller;

import example.entity.RegleManuelle;
import example.exceptions.ConflictException;
import example.interfaces.IRegleManuelleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reglemanuelle")
public class RegleManuelleController {

    private final IRegleManuelleService regleManuelleService;

    public RegleManuelleController(IRegleManuelleService regleManuelleService) {
        this.regleManuelleService = regleManuelleService;
    }

    // API REST: Récupérer tous les RM
    @GetMapping("/api")
    public List<RegleManuelle> getRegleManuellesApi() {
        return regleManuelleService.getRegleManuelles();
    }

    /*// API REST: Récupérer une RM par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<RegleManuelle> getRegleManuelleByIdApi(@PathVariable int id) {
        RegleManuelle regleManuelle = regleManuelleService.getRegleManuelle(id);
        if (regleManuelle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(regleManuelle, HttpStatus.OK);
    }

    // API REST: Créer une RM
    @PostMapping("/api")
    public ResponseEntity<RegleManuelle> createRegleManuelleApi(@RequestBody RegleManuelle regleManuelle) {
        if (regleManuelle == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            RegleManuelle createdRegleManuelle = regleManuelleService.createRegleManuelle(regleManuelle);
            return new ResponseEntity<>(createdRegleManuelle, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Constraint violations
        } catch (IllegalArgumentException e) { // Handle specific exception
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ConflictException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API REST: Modifier une RM
    @PutMapping("/api/{id}")
    public ResponseEntity<RegleManuelle> updateRegleManuelleApi(@PathVariable int id, @RequestBody RegleManuelle regleManuelle) {
        if (regleManuelle == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            RegleManuelle updatedRegleManuelle = regleManuelleService.updateRegleManuelle(id, regleManuelle);
            if (updatedRegleManuelle == null) {
                throw new EntityNotFoundException("Manual ruler not found with ID " + id);
            }
            return new ResponseEntity<>(updatedRegleManuelle, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // API REST: Supprimer une RM
    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> deleteRegleManuelleApi(@PathVariable int id) {
        try {
            regleManuelleService.deleteRegleManuelle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    // Vue Thymeleaf pour lister les RM
    @GetMapping("/list")
    public String listRegleManuelles(Model model) {
        model.addAttribute("regleManuelles", regleManuelleService.getRegleManuelles());
        return "reglesManuelles/RM_list";
    }
    // Vue Thymeleaf pour voir une RM par ID
    @GetMapping("/view/{id}")
    public String viewRegleManuelleById(@PathVariable int id, Model model) {
        RegleManuelle regleManuelle = regleManuelleService.getRegleManuelle(id);
        if (regleManuelle == null) {
            model.addAttribute("regleManuelle", regleManuelle);
            return "reglesManuelles/error";
        }
        model.addAttribute("regleManuelle", regleManuelle);
        return "reglesManuelles/RM_details";
    }
    // Formulaire Thymeleaf pour créer une RM
    @GetMapping("/create")
    public String createRegleManuelleForm(Model model) {
        model.addAttribute("regleManuelle", new RegleManuelle());
        return "reglesManuelles/RM_create";
    }

    // Création d'une RM via formulaire Thymeleaf
    @PostMapping("/create")
    public String createRegleManuelle(@Valid @ModelAttribute("regleManuelle") RegleManuelle regleManuelle) {
        regleManuelleService.createRegleManuelle(regleManuelle);
        return "redirect:/reglesManuelles/RM_list";
    }
    // Formulaire Thymeleaf pour modifier une RM
    @GetMapping("/edit/{id}")
    public String editRegleManuelleForm(@PathVariable int id, Model model) {
        RegleManuelle regleManuelle = regleManuelleService.getRegleManuelle(id);
        if (regleManuelle == null) {
            return "reglesManuelles/error";
        }
        model.addAttribute("regleManuelle", regleManuelle);
        return "reglesManuelles/RM_edit";
    }
    // Modifier une RM via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updatedRegleManuelle(@PathVariable int id, @Valid @ModelAttribute("regleManuelle") RegleManuelle regleManuelle) {
        regleManuelleService.updateRegleManuelle(id, regleManuelle);
        return "redirect:/reglesManuelles/RM_list";
    }
    // Supprimer une RM via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public String deleteRegleManuelle(@PathVariable int id) {
        System.out.println("Controller: Calling deleteRegleManuelle service method");
        try {
            regleManuelleService.deleteRegleManuelle(id);
        } catch (EntityNotFoundException e) {
            System.err.println("Erreur : " + e.getMessage());
            return "redirect:/reglesManuelles/error";
        }
        return "redirect:/reglesManuelles/RM_list";
    }











}
