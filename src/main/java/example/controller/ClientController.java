package example.controller;

import example.DTO.ClientDTO;
import example.interfaces.IClientService;
import jakarta.validation.Valid;
import org.hibernate.loader.MultipleBagFetchException;
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

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(MultipleBagFetchException.class)
        public String handleMultipleBagFetchException(MultipleBagFetchException ex, Model model) {
            model.addAttribute("errorMessage", "Erreur lors du chargement des données : " + ex.getMessage());
            return "clients/error";
        }
    }



    // Vue Thymeleaf pour lister les clients
    @GetMapping("/list")
    public String viewClients(Model model) {
        List<ClientDTO> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        return "clients/client_list"; // Vue correcte
    }

    // Vue Thymeleaf pour voir un client par ID
    @GetMapping("/view/{id}")
    public String viewClientById(@PathVariable int id, Model model) {
        ClientDTO client = clientService.getClientById(id);
        if (client == null) {
            model.addAttribute("errorMessage", "Client avec l'ID " + id + " non trouvé.");
            return "clients/error"; // Assurez-vous que la vue "error.html" est bien dans le dossier principal
        }
        model.addAttribute("client", client);
        return "clients/client_detail";
    }

    // Formulaire Thymeleaf pour créer un client
    @GetMapping("/create")
    public String createClientForm(Model model) {
        model.addAttribute("client", new ClientDTO());
        return "clients/client_create";
    }

    // Création d'un client via formulaire Thymeleaf
    @PostMapping("/create")
    public String createClient(@Valid @ModelAttribute("client") ClientDTO clientDTO, Model model) {
        try {
            clientService.createClient(clientDTO);
            return "redirect:/clients/list"; // Redirige vers la liste après création
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la création du client : " + e.getMessage());
            return "clients/error";
        }
    }

    // Formulaire Thymeleaf pour modifier un client
    @GetMapping("/edit/{id}")
    public String editClientForm(@PathVariable int id, Model model) {
        ClientDTO client = clientService.getClientById(id);
        if (client == null) {
            model.addAttribute("errorMessage", "Client avec l'ID " + id + " non trouvé.");
            return "clients/error"; // Vue d'erreur générale
        }
        model.addAttribute("client", client);
        return "clients/client_edit";
    }

    // Modifier un client via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateClient(@PathVariable int id, @Valid @ModelAttribute("client") ClientDTO clientDTO, Model model) {
        try {
            clientService.updateClient(id, clientDTO);
            return "redirect:/clients/list"; // Redirige vers la liste
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la modification du client : " + e.getMessage());
            return "clients/error";
        }
    }

    // Supprimer un client via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public String deleteClient(@PathVariable int id, Model model) {
        try {
            clientService.deleteClient(id);
            return "redirect:/clients/list"; // Chemin corrigé
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression du client : " + e.getMessage());
            return "clients/error";
        }
    }
}
