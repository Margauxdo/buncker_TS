package example.controller;

import example.entity.SortieSemaine;
import example.interfaces.ISortieSemaineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sortieSemaine")
public class SortieSemaineController {

    @Autowired
    private ISortieSemaineService sortieSemaineService;

    /*// API REST: Récupérer tous les SS
    @GetMapping("/api")
    public ResponseEntity<List<SortieSemaine>> getAllSortieSemaineApi() {
        try {
            List<SortieSemaine> sortieSemaines = sortieSemaineService.getAllSortieSemaine();
            return new ResponseEntity<>(sortieSemaines, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // API REST: Récupérer un SS par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<SortieSemaine> getSortieSemaineApi(@PathVariable int id) {
        SortieSemaine sortieSemaine = sortieSemaineService.getSortieSemaine(id);
        return sortieSemaine != null ? new ResponseEntity<>(sortieSemaine, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // API REST: Créer un SS
    @PostMapping("/api")
    public ResponseEntity<SortieSemaine> createSortieSemaineApi(@Valid @RequestBody SortieSemaine sortieSemaine) {
        try {
            SortieSemaine createdSortie = sortieSemaineService.createSortieSemaine(sortieSemaine);
            return new ResponseEntity<>(createdSortie, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // API REST: Modifier un SS
    @PutMapping("/api/{id}")
    public ResponseEntity<SortieSemaine> updateSortieSemaineApi(@PathVariable int id, @RequestBody SortieSemaine sortieSemaine) {
        try {
            SortieSemaine updatedSortie = sortieSemaineService.updateSortieSemaine(id, sortieSemaine);
            return updatedSortie != null ?
                    new ResponseEntity<>(updatedSortie, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    // API REST: Supprimer un SS
    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> deleteSortieSemaineApi(@PathVariable int id) {
        try {
            sortieSemaineService.deleteSortieSemaine(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
    // Vue Thymeleaf pour lister les clients
    @GetMapping("/list")
    public String viewSortieSemaine(Model model) {
        model.addAttribute("sortieSemaine", sortieSemaineService.getAllSortieSemaine());
        return "sortieSemaines/SS_list";
    }

    // Vue Thymeleaf pour voir une SS par ID
    @GetMapping("/view/{id}")
    public String viewSortieSemaineById(@PathVariable int id, Model model) {
        SortieSemaine semaine = sortieSemaineService.getSortieSemaine(id);
        if (semaine == null) {
            model.addAttribute("errormessage", "sortieSemaine avec l'Id" + id + "non trouvé");
            return "sortieSemaines/error";
        }
        model.addAttribute("sortieSemaine", semaine);
        return "sortieSemaines/SS_details";
    }

    // Formulaire Thymeleaf pour créer un SS
    @GetMapping("/create")
    public String createSortieSemaineForm(Model model) {
        model.addAttribute("sortieSemaine", new SortieSemaine());
        return "sortieSemaines/SS_create";
    }

    // Création d'un SS via formulaire Thymeleaf
    @PostMapping("/create")
    public String createSortieSemaine(@Valid @ModelAttribute("sortieSemaine") SortieSemaine sortieSemaine) {
        sortieSemaineService.createSortieSemaine(sortieSemaine);
        return "redirect:/sortieSemaines/SS_list";
    }

    // Formulaire Thymeleaf pour modifier un SS
    @GetMapping("/edit/{id}")
    public String editSortieSemaineForm(@PathVariable int id, Model model) {
        SortieSemaine semaine = sortieSemaineService.getSortieSemaine(id);
        if (semaine == null) {
            return "sortieSemaines/error";
        }
        model.addAttribute("sortieSemaine", semaine);
        return "sortieSemaines/SS_edit";
    }

    // Modifier un SS via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateSortieSemaine(@PathVariable int id, @Valid @ModelAttribute("sortieSemaine") SortieSemaine sortieSemaine) {
        sortieSemaineService.updateSortieSemaine(id, sortieSemaine);
        return "redirect:/sortieSemaines/SS_list";
    }
    // Supprimer un SS via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public String deleteSortieSemaine(@PathVariable int id) {
        sortieSemaineService.deleteSortieSemaine(id);
        return "redirect:/sortieSemaines/list";
    }







}
