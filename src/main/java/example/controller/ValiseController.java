package example.controller;

import example.DTO.ValiseDTO;
import example.entity.TypeValise;
import example.interfaces.IValiseService;
import example.repositories.ClientRepository;
import example.repositories.MouvementRepository;
import example.repositories.RegleRepository;
import example.repositories.TypeValiseRepository;
import example.services.TypeValiseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/valise")
public class ValiseController {

    @Autowired
    private IValiseService valiseService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TypeValiseRepository typeValiseRepository;
    @Autowired
    private TypeValiseService typeValiseService;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private RegleRepository regleRepository;

    @GetMapping("/list")
    public String viewValises(Model model) {
        List<ValiseDTO> valises = valiseService.getAllValises();
        model.addAttribute("valises", valises);
        return "valises/valises_list";
    }

    @GetMapping("/view/{id}")
    public String getValiseDetails(@PathVariable int id, Model model) {
        ValiseDTO valise = valiseService.getValiseById(id); // Le type attendu est int
        System.out.println("Valise details: " + valise); // Debugging
        model.addAttribute("valise", valise);
        return "valises/valise_details";
    }


    @GetMapping("/create")
    public String createValiseForm(Model model) {
        model.addAttribute("valise", new ValiseDTO());
        model.addAttribute("clients", clientRepository.findAll()); // Ajoutez ici la liste des clients
        model.addAttribute("typesValise", typeValiseRepository.findAll()); // Ajoutez ici la liste des types de valise
        return "valises/valise_create";
    }


    @PostMapping("/create")
    public String createValise(@Valid @ModelAttribute("valise") ValiseDTO valiseDTO, Model model) {
        try {
            valiseService.createValise(valiseDTO);
            return "redirect:/valise/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la création de la valise : " + e.getMessage());
            return "valises/error";
        }
    }

    @GetMapping("/edit/{id}")
    public String editValise(@PathVariable int id, Model model) {
        ValiseDTO valiseDTO = valiseService.getValiseById(id);
        model.addAttribute("valise", valiseDTO);

        // Ajout des listes nécessaires au modèle
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("typesValise", typeValiseRepository.findAll());
        model.addAttribute("mouvements", mouvementRepository.findAll());
        model.addAttribute("regles", regleRepository.findAll());

        return "valises/valise_edit";
    }







    @PostMapping("/edit/{id}")
    public String updateValise(@PathVariable int id, @Valid @ModelAttribute("valise") ValiseDTO valiseDTO, Model model) {
        try {
            valiseService.updateValise(id, valiseDTO);
            return "redirect:/valise/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la modification de la valise : " + e.getMessage());
            return "valises/error";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteValise(@PathVariable int id, Model model) {
        try {
            valiseService.deleteValise(id);
            return "redirect:/valise/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression de la valise : " + e.getMessage());
            return "valises/error";
        }
    }
}
