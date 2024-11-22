package example.controller;

import example.entity.Probleme;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.IProblemeService;
import example.repositories.ProblemeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pb")
public class ProblemeController {

    @Autowired
    private IProblemeService problemeService;
    @Autowired
    private ProblemeRepository problemeRepository;

    // API REST: Récupérer tous les pb
    @GetMapping("/api")
    public List<Probleme> getAllProblemesApi() {
        List<Probleme> problemes = problemeService.getAllProblemes();
        return new ResponseEntity<>(problemes, HttpStatus.OK).getBody();
    }

    // API REST: Récupérer un pb par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<Probleme> getProblemByIdApi(@PathVariable int id) {
        Probleme probleme = problemeService.getProblemeById(id);
        return probleme != null ? new ResponseEntity<>(probleme, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // API REST: Créer un pb
    @PostMapping("/api")
    public ResponseEntity<Probleme> createProblemeApi(@Valid @RequestBody Probleme probleme) {
        try {
            Probleme newProbleme = problemeService.createProbleme(probleme);
            return new ResponseEntity<>(newProbleme, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API REST: Modifier un pb
    @PutMapping("/api/{id}")
    public ResponseEntity<Probleme> updateProblemeApi(@PathVariable int id, @RequestBody Probleme probleme) {
        try {
            Probleme updatedProbleme = problemeService.updateProbleme(id, probleme);
            if (updatedProbleme == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(updatedProbleme);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Gérer l'exception de manière cohérente
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // API REST: Supprimer un pb
    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> deleteProblemeApi(@PathVariable int id) {
        try {
            Optional<Probleme> existingProbleme = problemeRepository.findById(id);
            if (existingProbleme.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            problemeService.deleteProbleme(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            // Si une RuntimeException est levée, on retourne INTERNAL_SERVER_ERROR
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Vue Thymeleaf pour lister les pb
    @GetMapping("/list")
    public String viewAllProblemes(Model model) {
        model.addAttribute("problemes", problemeService.getAllProblemes());
        return "problemes/pb_list";
    }

    // Vue Thymeleaf pour voir un pb par ID
    @GetMapping("/view/{id}")
    public String viewProbleme(@PathVariable int id, Model model) {
        Probleme probleme = problemeService.getProblemeById(id);
        if (probleme == null) {
            model.addAttribute("errorMessage", "Probleme ave l'ID" + id + "non trouve");
            return "problemes/error";
        }
        model.addAttribute("probleme", probleme);
        return "problemes/pb_edit";
    }

    // Formulaire Thymeleaf pour créer un pb
    @GetMapping("/create")
    public String createProblemeForm(Model model) {
        model.addAttribute("probleme", new Probleme());
        return "problemes/pb_create";
    }

    // Création d'un pb via formulaire Thymeleaf
    @PostMapping("/create")
    public String createProblemeForm(@Valid @ModelAttribute("probleme") Probleme probleme) {
        problemeService.createProbleme(probleme);
        return "redirect:/problemes/pb_list";
    }

    // Formulaire Thymeleaf pour modifier un pb
    @GetMapping("/edit/{id}")
    public String updatedProblemeForm(@PathVariable int id, Model model) {
        Probleme probleme = problemeService.getProblemeById(id);
        if (probleme == null) {
            return "problemes/error";
        }
        model.addAttribute("probleme", probleme);
        return "problemes/pb_edit";
    }

    // Modifier un pb via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updatedProbleme(@PathVariable int id, @Valid @ModelAttribute("probleme") Probleme probleme) {
        problemeService.updateProbleme(id, probleme);
        return "redirect:/problemes/pb_list";
    }

    // Supprimer un pb via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public String deleteProbleme(@PathVariable int id) {
        problemeService.deleteProbleme(id);
        return "redirect:/problemes/pb_list";
    }












}
