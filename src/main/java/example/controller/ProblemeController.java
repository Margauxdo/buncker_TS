package example.controller;

import example.DTO.ClientDTO;
import example.DTO.ProblemeDTO;
import example.DTO.RegleDTO;
import example.DTO.ValiseDTO;
import example.entity.Client;
import example.entity.Valise;
import example.interfaces.IProblemeService;
import example.repositories.ClientRepository;
import example.services.ClientService;
import example.services.ValiseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;


@Controller
@RequestMapping("/pb")
public class ProblemeController {

    @Autowired
    private IProblemeService problemeService;

    @Autowired
    private ValiseService valiseService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;

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
        problemeDTO.setValiseId(null); // Initialiser valiseId à null
        problemeDTO.setClientIds(new ArrayList<>()); // Initialiser clientIds à une liste vide

        // Récupérer tous les clients depuis votre service
        List<ClientDTO> clientsDTO = clientService.getAllClients();

        model.addAttribute("probleme", problemeDTO); // Ajouter le problème à la vue
        model.addAttribute("clients", clientsDTO); // Ajouter les clients à la vue
        model.addAttribute("valises", valiseService.getAllValises()); // Ajouter les valises à la vue

        return "problemes/pb_create"; // Retourner le formulaire
    }




    @PostMapping("/create")
    public String createProbleme(@Valid @ModelAttribute("probleme") ProblemeDTO problemeDTO, Model model) {
        try {
            // Vérifier et récupérer les clients associés
            if (problemeDTO.getClientIds() != null && !problemeDTO.getClientIds().isEmpty()) {
                List<Client> clients = clientRepository.findAllById(problemeDTO.getClientIds());
                if (clients.isEmpty()) {
                    model.addAttribute("errorMessage", "Aucun client trouvé.");
                    return "problemes/error";
                }
                problemeDTO.setClientNoms(clients.stream().map(Client::getName).collect(Collectors.toList()));
            }

            // Création du problème avec la valise et les clients
            problemeService.createProbleme(problemeDTO);

            return "redirect:/pb/list"; // Rediriger vers la liste des problèmes après la création
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
