package example.controller;

import example.entity.Client;
import example.interfaces.IClientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private IClientService clientService;

    // API REST: Récupérer tous les clients
    @GetMapping("/api")
    public ResponseEntity<List<Client>> getAllClientsApi() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    // API REST: Récupérer un client par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<Client> getClientByIdApi(@PathVariable int id) {
        Client client = clientService.getClientById(id);
        return client != null ? ResponseEntity.ok(client)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // API REST: Créer un client
    @PostMapping("/api")
    public ResponseEntity<Client> createClientApi(@Valid @RequestBody Client client) {
        Client newClient = clientService.createClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    // API REST: Modifier un client
    @PutMapping("/api/{id}")
    public ResponseEntity<Client> updateClientApi(@PathVariable int id, @RequestBody Client client) {
        Client updatedClient = clientService.updateClient(id, client);
        if (updatedClient == null) {
            throw new EntityNotFoundException("Client not found with ID " + id);  // Cette ligne lèvera une exception capturée par l'@ExceptionHandler
        }
        return ResponseEntity.ok(updatedClient);
    }


    // API REST: Supprimer un client
    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> deleteClientApi(@PathVariable int id) {
        if (!clientService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    // Vue Thymeleaf pour lister les clients
    @GetMapping("/list")
    public String viewClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "clients/client_list";
    }

    // Vue Thymeleaf pour voir un client par ID
    @GetMapping("/view/{id}")
    public String viewClientById(@PathVariable int id, Model model) {
        Client client = clientService.getClientById(id);
        if (client == null) {
            model.addAttribute("errorMessage", "Client avec l'ID " + id + " non trouvé.");
            return "error";
        }
        model.addAttribute("client", client);
        return "clients/client_detail";
    }



    // Formulaire Thymeleaf pour créer un client
    @GetMapping("/create")
    public String createClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/client_create";
    }



    // Création d'un client via formulaire Thymeleaf
    @PostMapping("/create")
    public String createClient(@Valid @ModelAttribute("client") Client client) {
        clientService.createClient(client);
        return "redirect:/clients/list";
    }

    // Formulaire Thymeleaf pour modifier un client
    @GetMapping("/edit/{id}")
    public String editClientForm(@PathVariable int id, Model model) {
        Client client = clientService.getClientById(id);
        if (client == null) {
            return "error"; // Affiche une page d'erreur si le client n'existe pas
        }
        model.addAttribute("client", client);
        return "clients/client_edit";
    }

    // Modifier un client via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateClient(@PathVariable int id, @Valid @ModelAttribute("client") Client client) {
        clientService.updateClient(id, client);
        return "redirect:/clients/list";
    }

    // Supprimer un client via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public String deleteClient(@PathVariable int id) {
        clientService.deleteClient(id);
        return "redirect:/clients/list";
    }
}
