package example.controller;

import example.DTO.*;
import example.entity.Client;
import example.interfaces.IClientService;
import example.services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);


    private final ClientService clientService;
    private final ValiseService valiseService;
    private final ProblemeService problemeService;
    private final RetourSecuriteService retourSecuriteService;
    private final RegleService regleService;


    public ClientController(ClientService clientService, ValiseService valiseService
            , ProblemeService problemeService, RetourSecuriteService retourSecuriteService, RegleService regleService) {
        this.clientService = clientService;
        this.valiseService = valiseService;
        this.problemeService = problemeService;
        this.retourSecuriteService = retourSecuriteService;
        this.regleService = regleService;
    }

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
        List<ClientDTO> clients = clientService.getAllClients();
        log.info("Liste des clients après suppression : {}", clients);
        model.addAttribute("clients", clients);
        return "clients/client_list";
    }


    /**
     * Vue Thymeleaf pour voir un client par ID.
     */
    @GetMapping("/view/{id}")
    public String getClientById(@PathVariable int id, Model model) {
        try {
            ClientDTO clientDTO = clientService.getClientById(id);
            model.addAttribute("client", clientDTO);
            return "clients/client_detail";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Client avec l'ID " + id + " non trouvé.");
            return "clients/error";
        }
    }


    /**
     * Formulaire Thymeleaf pour créer un client.
     */
    @GetMapping("/create")
    public String createClientForm(Model model) {
        // Add a new ClientDTO object
        model.addAttribute("client", new ClientDTO());

        // Populate and log the lists
        List<ValiseDTO> valises = valiseService.getAllValises();
        List<ProblemeDTO> problemes = problemeService.getAllProblemes();
        List<RetourSecuriteDTO> retours = retourSecuriteService.getAllRetourSecurites();
        List<RegleDTO> regles = regleService.getAllRegles();

        System.out.println("Valises: " + valises);
        System.out.println("Problemes: " + problemes);
        System.out.println("Retours: " + retours);
        System.out.println("Regles: " + regles);

        // Add to model
        model.addAttribute("valisesList", valises);
        model.addAttribute("problemsList", problemes);
        model.addAttribute("retoursList", retours);
        model.addAttribute("rulesList", regles);

        return "clients/client_create";
    }

    /**
     * Création d'un client via formulaire Thymeleaf.
     */
    @PostMapping("/create")
    public String createClient(@Valid @ModelAttribute("client") ClientDTO clientDTO, Model model) {
        try {
            System.out.println("ClientDTO reçu : " + clientDTO);
            clientService.createClient(clientDTO);
            return "redirect:/clients/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Erreur : " + e.getMessage());
            return "clients/error";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur inattendue : " + e.getMessage());
            return "clients/error";
        }
    }


    @GetMapping("/edit/{id}")
    public String editClient(@PathVariable int id, Model model) {
        ClientDTO client = clientService.getClientById(id);

        List<ValiseDTO> valises = valiseService.getAllValises();
        List<ProblemeDTO> problemes = problemeService.getAllProblemes();
        List<RetourSecuriteDTO> retours = retourSecuriteService.getAllRetourSecurites();
        List<RegleDTO> regles = regleService.getAllRegles();

        System.out.println("Valises: " + valises);
        System.out.println("Problemes: " + problemes);
        System.out.println("Retours: " + retours);
        System.out.println("Regles: " + regles);

        model.addAttribute("client", client);

        // Populer les listes avec des objets Valise, Probleme, RetourSecurite, et Regle
        model.addAttribute("valisesList", valiseService.getAllValises());
        model.addAttribute("problemsList", problemeService.getAllProblemes());
        model.addAttribute("retoursList", retourSecuriteService.getAllRetourSecurites());
        model.addAttribute("rulesList", regleService.getAllRegles());

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
            log.info("Tentative de suppression du client avec ID: {}", id);
            clientService.deleteClient(id);
            log.info("Client avec ID {} supprimé avec succès.", id);
            return "redirect:/clients/list";
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du client avec ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "clients/error";
        }
    }

}








