package example.controller;

import example.DTO.SortieSemaineDTO;
import example.entity.Regle;
import example.interfaces.ISortieSemaineService;
import example.repositories.RegleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sortieSemaine")
public class SortieSemaineController {

    @Autowired
    private ISortieSemaineService sortieSemaineService;
    @Autowired
    private RegleRepository regleRepository;

    // Vue Thymeleaf pour lister les SortieSemaine
    @GetMapping("/list")
    public String viewSortieSemaine(Model model) {
        List<SortieSemaineDTO> sortieSemaines = sortieSemaineService.getAllSortieSemaine();
        if (sortieSemaines.isEmpty()) {
            System.out.println("Aucune sortie semaine à afficher."); // Log temporaire
        }
        model.addAttribute("sortiesSemaine", sortieSemaines); // Vérifiez cet attribut
        return "sortieSemaines/SS_list";
    }

    // Vue Thymeleaf pour voir une SortieSemaine par ID
    @GetMapping("/view/{id}")
    public String viewSortieSemaine(@PathVariable int id, Model model) {
        SortieSemaineDTO dto = sortieSemaineService.getSortieSemaine(id);
        if (dto == null) {
            return "sortieSemaines/error";
        }
        model.addAttribute("sortieSemaine", dto);
        return "sortieSemaines/SS_details";
    }

    // Formulaire Thymeleaf pour créer une SortieSemaine
    @GetMapping("/create")
    public String createSortieSemaineForm(Model model) {
        List<Regle> regles = regleRepository.findAll();
        model.addAttribute("sortieSemaine", new SortieSemaineDTO());
        model.addAttribute("regles", regles); // Ajoutez les règles au modèle
        return "sortieSemaines/SS_create";
    }


    @PostMapping("/create")
    public String createSortieSemaine(@ModelAttribute("sortieSemaine") SortieSemaineDTO sortieSemaineDTO) {
        sortieSemaineService.createSortieSemaine(sortieSemaineDTO);
        return "redirect:/sortieSemaine/list";
    }

    // Formulaire Thymeleaf pour modifier une SortieSemaine
    @GetMapping("/edit/{id}")
    public String editSortieSemaineForm(@PathVariable int id, Model model) {
        SortieSemaineDTO sortieSemaine = sortieSemaineService.getSortieSemaine(id);
        if (sortieSemaine == null) {
            return "sortieSemaines/error";
        }
        List<Regle> regles = regleRepository.findAll();
        model.addAttribute("sortieSemaine", sortieSemaine);
        model.addAttribute("regles", regles); // Ajouter les règles au modèle
        return "sortieSemaines/SS_edit";
    }


    @PostMapping("/edit/{id}")
    public String updateSortieSemaine(@PathVariable int id,
                                      @ModelAttribute("sortieSemaine") SortieSemaineDTO sortieSemaineDTO,
                                      Model model) {
        try {
            // Mise à jour de la sortie semaine via le service
            sortieSemaineService.updateSortieSemaine(id, sortieSemaineDTO);
            return "redirect:/sortieSemaine/list"; // Redirection vers la liste après succès
        } catch (EntityNotFoundException ex) {
            model.addAttribute("errorMessage", "Erreur : " + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Erreur inattendue : " + ex.getMessage());
        }

        // Réafficher le formulaire en cas d'erreur
        List<Regle> regles = regleRepository.findAll();
        model.addAttribute("regles", regles);
        return "sortieSemaines/SS_edit";
    }




    @PostMapping("/delete/{id}")
    public String deleteSortieSemaine(@PathVariable int id) {
        sortieSemaineService.deleteSortieSemaine(id);
        return "redirect:/sortieSemaine/list";
    }
}
