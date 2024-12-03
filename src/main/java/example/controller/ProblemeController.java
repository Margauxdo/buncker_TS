package example.controller;

import example.entity.Probleme;
import example.entity.Valise;
import example.interfaces.IProblemeService;
import example.repositories.ProblemeRepository;
import example.services.ClientService;
import example.services.ValiseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pb")
public class ProblemeController {

    @Autowired
    private IProblemeService problemeService;
    @Autowired
    private ProblemeRepository problemeRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ValiseService valiseService;

    /*// API REST: Récupérer tous les pb
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
    }*/


        @GetMapping("/list")
        public String viewAllProblemes(Model model) {
            List<Probleme> pbList = problemeService.getAllProblemes();
            model.addAttribute("problemes", problemeService.getAllProblemes());
            return "problemes/pb_list";
        }

    @GetMapping("/view/{id}")
    public String viewProbleme(@PathVariable int id, Model model) {
        Probleme probleme = problemeService.getProblemeById(id);
        if (probleme == null) {
            model.addAttribute("errorMessage", "Problème avec l'ID " + id + " non trouvé.");
            return "problemes/error";
        }

        // Ajout de la liaison client au modèle si elle existe
        if (probleme.getClient() != null) {
            model.addAttribute("client", probleme.getClient());
        }

        model.addAttribute("probleme", probleme);
        return "problemes/pb_details";
    }


    @GetMapping("/create")
    public String createProblemeForm(Model model) {
        model.addAttribute("probleme", new Probleme());

        // Add all Valises to the Model for the dropdown
        List<Valise> valises = valiseService.getAllValises();
        model.addAttribute("valises", valises);

        return "problemes/pb_create";
    }


    @PostMapping("/create")
    public String createProblemeForm(@Valid @ModelAttribute("probleme") Probleme probleme,
                                     @RequestParam("valise.id") Long valiseId, Model model) {
        try {
            // Vérification si la Valise existe
            Valise valise = valiseService.getValiseById(Math.toIntExact(valiseId));
            if (valise == null) {
                throw new EntityNotFoundException("La valise avec l'ID " + valiseId + " n'existe pas.");
            }

            // Association de la Valise au Problème
            probleme.setValise(valise);

            // Sauvegarde du Problème
            problemeService.createProbleme(probleme);
            return "redirect:/pb/list"; // Redirection en cas de succès
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "La valise avec l'ID " + valiseId + " est introuvable.");
            return "problemes/error"; // Page d'erreur Thymeleaf
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la création du problème.");
            return "problemes/error"; // Page d'erreur générique
        }
    }



    @GetMapping("/edit/{id}")
    public String updatedProblemeForm(@PathVariable int id, Model model) {
        Probleme probleme = problemeService.getProblemeById(id);
        if (probleme == null) {
            model.addAttribute("errorMessage", "Problème avec l'ID " + id + " non trouvé.");
            return "problemes/error";
        }
        model.addAttribute("probleme", probleme);
        List<Valise> valises = valiseService.getAllValises();
        model.addAttribute("valises", valises);
        return "problemes/pb_edit";
    }




    @PostMapping("/edit/{id}")
    public String updatedProbleme(@PathVariable int id, @Valid @ModelAttribute("probleme") Probleme probleme, Model model) {
        try {
            // Ensure the entity is correctly updated with its relationships
            Probleme existingProbleme = problemeService.getProblemeById(id);
            if (existingProbleme == null) {
                model.addAttribute("errorMessage", "Problème non trouvé.");
                return "problemes/error";
            }

            // Set the existing values if needed
            probleme.setId(id);
            probleme.setValise(existingProbleme.getValise());
            probleme.setClient(existingProbleme.getClient());

            // Proceed with update
            problemeService.updateProbleme(id, probleme);
            return "redirect:/pb/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la mise à jour du problème.");
            return "problemes/error";
        }
    }


    @PostMapping("/delete/{id}")
    public String deleteProbleme(@PathVariable int id, Model model) {
        try {
            problemeService.deleteProbleme(id);  // Delete the "Probleme" entity
            return "redirect:/pb/list";  // Redirect to the list of problems
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression du problème.");
            return "problemes/error";
        }
    }



}








