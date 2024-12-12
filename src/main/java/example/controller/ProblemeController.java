package example.controller;

import example.DTO.ClientDTO;
import example.DTO.ProblemeDTO;
import example.DTO.RegleDTO;
import example.interfaces.IProblemeService;
import example.services.ClientService;
import example.services.ValiseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;

import static example.services.FormuleService.logger;

@Controller
@RequestMapping("/pb")
public class ProblemeController {

    @Autowired
    private IProblemeService problemeService;

    @Autowired
    private ValiseService valiseService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/list")
    public String viewAllProblemes(Model model) {
        try {
            List<ProblemeDTO> problemes = problemeService.getAllProblemes();
            if (problemes.isEmpty()) {
                System.out.println("Aucun problème trouvé.");
            } else {
                System.out.println("Problèmes trouvés : " + problemes.size());
            }
            model.addAttribute("problemes", problemes);
            return "problemes/pb_list";
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des problèmes : " + e.getMessage());
            model.addAttribute("errorMessage", "Erreur lors de la récupération des problèmes.");
            return "problemes/error";
        }
    }


    @GetMapping("/view/{id}")
    public String viewProbleme(@PathVariable int id, Model model) {
        try {
            ProblemeDTO probleme = problemeService.getProblemeById(id);
            System.out.println("Probleme retrieved: " + probleme);
            model.addAttribute("probleme", probleme);
            return "problemes/pb_details";
        } catch (Exception e) {
            System.err.println("Error retrieving probleme: " + e.getMessage());
            model.addAttribute("errorMessage", "Problème non trouvé avec l'ID: " + id);
            return "problemes/error";
        }
    }



    @GetMapping("/create")
    public String createProblemeForm(Model model) {
        ProblemeDTO problemeDTO = new ProblemeDTO();
        problemeDTO.setValiseId(null); // Explicitly set default values
        problemeDTO.setClientId(null);

        List<ClientDTO> clientsDTO = clientService.getAllClients();

        model.addAttribute("probleme", problemeDTO);
        model.addAttribute("clients", clientsDTO);

        model.addAttribute("valises", valiseService.getAllValises());

        return "problemes/pb_create";
    }


    @PostMapping("/create")
    public String createProbleme(@Valid @ModelAttribute("probleme") ProblemeDTO problemeDTO, Model model) {
        try {
            problemeService.createProbleme(problemeDTO);
            return "redirect:/pb/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la création du problème.");
            return "problemes/error";
        }
    }

    @GetMapping("/edit/{id}")
    public String updatedProblemeForm(@PathVariable int id, Model model) {
        ProblemeDTO probleme = problemeService.getProblemeById(id);
        if (probleme == null) {
            model.addAttribute("errorMessage", "Problème non trouvé.");
            return "problemes/error";
        }
        model.addAttribute("probleme", probleme);
        model.addAttribute("valises", valiseService.getAllValises());
        model.addAttribute("clients", clientService.getAllClients());
        return "problemes/pb_edit";
    }


    @PostMapping("/edit/{id}")
    public String updateProbleme(@PathVariable int id, @Valid @ModelAttribute("probleme") ProblemeDTO problemeDTO, Model model) {
        try {
            problemeService.updateProbleme(id, problemeDTO);
            return "redirect:/pb/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la mise à jour du problème.");
            return "problemes/error";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteProbleme(@PathVariable int id, Model model) {
        try {
            problemeService.deleteProbleme(id);
            return "redirect:/pb/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression du problème.");
            return "problemes/error";
        }
    }
}
