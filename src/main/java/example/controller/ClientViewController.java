package example.controller;

import example.entities.Client;
import example.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientViewController {

    @Autowired
    private ClientService clientService;

    //all clients
    @GetMapping
    public String listClients(Model model) {
        List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        return "clients/listClients";
    }

    //form create owner
    @GetMapping("/createClient")
    public String createClient(Model model) {
        model.addAttribute("client", new Client());
        return "clients/createClient";
    }

    //Subscribe new client
    @PostMapping("/createClient")
    public String createClient(@ModelAttribute("client") Client client) {
        clientService.createClient(client);
        return "redirect:/clients";
    }

    //show update form
    @GetMapping("/editClient/{id}")
    public String editClient(@PathVariable("id") int id, Model model) {
        Client client = clientService.getClientById(id);
        model.addAttribute("client", client);
        return "clients/editClient";
    }

    //save changes
    @PostMapping("/editClient/{id}")
    public String editClient(@PathVariable("id") int id, @ModelAttribute("client") Client client) {
        clientService.updateClient(id,client);
            return "redirect:/clients";

    }

    //delete a form
    @GetMapping("/deleteClient/{id}")
    public String deleteClient(@PathVariable("id") int id) {
        clientService.deleteClient(id);
        return "redirect:/clients";
    }
}
