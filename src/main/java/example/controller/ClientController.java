package example.controller;

import example.DTO.ClientDTO;
import example.entity.Client;
import example.interfaces.IClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private IClientService clientService;

    /**
     * Gestion globale des exceptions pour les erreurs inattendues.
     */
    @ControllerAdvice
    public static class GlobalExceptionHandler {
        @ExceptionHandler(Exception.class)
        public String handleGenericException(Exception ex, Model model) {
            model.addAttribute("errorMessage", "Erreur inattendue : " + ex.getMessage());
            return "clients/error";
        }
    }

    /**
     * Liste des clients pour la vue Thymeleaf.
     */
    @GetMapping("/list")
    public String listClients(Model model) {
        // Obtenir les clients en tant que DTO directement depuis le service
        List<ClientDTO> clientDTOs = clientService.getAllClients();
        model.addAttribute("clients", clientDTOs);
        return "clients/client_list";
    }

    /**
     * Vue Thymeleaf pour voir un client par ID.
     */
    @GetMapping("/view/{id}")
    public String viewClientById(@PathVariable int id, Model model) {
        ClientDTO client = clientService.getClientById(id);
        if (client == null) {
            model.addAttribute("errorMessage", "Client avec l'ID " + id + " non trouvé.");
            return "clients/error";
        }
        model.addAttribute("client", client); // Pas besoin de conversion supplémentaire
        return "clients/client_detail";
    }

    /**
     * Formulaire Thymeleaf pour créer un client.
     */
    @GetMapping("/create")
    public String createClientForm(Model model) {
        model.addAttribute("client", new ClientDTO());
        return "clients/client_create";
    }

    /**
     * Création d'un client via formulaire Thymeleaf.
     */
    @PostMapping("/create")
    public String createClient(@Valid @ModelAttribute("client") ClientDTO clientDTO, Model model) {
        try {
            clientService.createClient(clientDTO);
            return "redirect:/clients/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la création du client : " + e.getMessage());
            return "clients/error";
        }
    }

    /**
     * Formulaire Thymeleaf pour modifier un client.
     */
    @GetMapping("/edit/{id}")
    public String editClientForm(@PathVariable int id, Model model) {
        ClientDTO client = clientService.getClientById(id);
        if (client == null) {
            model.addAttribute("errorMessage", "Client avec l'ID " + id + " non trouvé.");
            return "clients/error";
        }
        model.addAttribute("client", client); // Pas besoin de conversion supplémentaire
        return "clients/client_edit";
    }

    /**
     * Modifier un client via formulaire Thymeleaf.
     */
    @PostMapping("/edit/{id}")
    public String updateClient(@PathVariable int id, @Valid @ModelAttribute("client") ClientDTO clientDTO, Model model) {
        try {
            clientService.updateClient(id, clientDTO);
            return "redirect:/clients/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la modification du client : " + e.getMessage());
            return "clients/error";
        }
    }

    /**
     * Supprimer un client via un formulaire Thymeleaf sécurisé.
     */
    @PostMapping("/delete/{id}")
    public String deleteClient(@PathVariable int id, Model model) {
        try {
            clientService.deleteClient(id);
            return "redirect:/clients/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression du client : " + e.getMessage());
            return "clients/error";
        }
    }
}
