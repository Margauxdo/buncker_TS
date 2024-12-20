package example.controller;

import example.DTO.RetourSecuriteDTO;
import example.entity.Client;
import example.entity.Mouvement;
import example.interfaces.IRetourSecuriteService;
import example.repositories.ClientRepository;
import example.repositories.MouvementRepository;
import example.services.ClientService;
import example.services.RetourSecuriteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequestMapping("/retourSecurite")
public class RetourSecuriteController {

    private static final Logger logger = LoggerFactory.getLogger(RetourSecuriteService.class);


    private final IRetourSecuriteService retourSecuriteService;
    private final ClientRepository clientRepository;
    private final MouvementRepository mouvementRepository;
    private final ClientService clientService;

    public RetourSecuriteController(IRetourSecuriteService retourSecuriteService, ClientRepository clientRepository, MouvementRepository mouvementRepository, ClientService clientService) {
        this.retourSecuriteService = retourSecuriteService;
        this.clientRepository = clientRepository;
        this.mouvementRepository = mouvementRepository;
        this.clientService = clientService;
    }

    @GetMapping("/list")
    public String listRetourSecurites(Model model) {
        List<RetourSecuriteDTO> retourSecurites = retourSecuriteService.getAllRetourSecurites();
        for (RetourSecuriteDTO retour : retourSecurites) {
            if (retour.getMouvementStatuts() != null) {
                String mouvementStatutsString = String.join(", ", retour.getMouvementStatuts());
                retour.setMouvementStatutsString(mouvementStatutsString);
            }
            // Set the nombreClients property
            retour.setNombreClients(retour.getMouvementStatuts() != null ? retour.getMouvementStatuts().size() : 0);
        }
        model.addAttribute("retoursSecurite", retourSecurites);
        return "retourSecurites/RS_list";
    }




    @GetMapping("/view/{id}")
    public String viewRetourSecuriteById(@PathVariable int id, Model model) {
        RetourSecuriteDTO retourSecurite = retourSecuriteService.getRetourSecurite(id);
        model.addAttribute("retourSecurite", retourSecurite);

        // Ajout des mouvements dans le modèle
        List<Mouvement> mouvements = mouvementRepository.findAllByRetourSecuriteId(id);
        model.addAttribute("mouvements", mouvements);

        return "retourSecurites/RS_details";
    }



    @GetMapping("/create")
    public String createRetourSecuriteForm(Model model) {
        model.addAttribute("retourSecurite", new RetourSecuriteDTO());
        List<Client> clients = clientRepository.findAll();
        List<Mouvement> mouvements = mouvementRepository.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("mouvements", mouvements);
        return "retourSecurites/RS_create";
    }



    @PostMapping("/create")
    public String createRetourSecurite(@Valid @ModelAttribute("retourSecurite") RetourSecuriteDTO retourSecuriteDTO) {
        logger.info("Création d'un retour sécurité avec date de clôture : {}", retourSecuriteDTO.getDateCloture());
        retourSecuriteService.createRetourSecurite(retourSecuriteDTO);
        return "redirect:/retourSecurite/list";
    }


    @GetMapping("/edit/{id}")
    public String editRetourSecuriteForm(@PathVariable int id, Model model) {
        RetourSecuriteDTO retourSecurite = retourSecuriteService.getRetourSecurite(id);
        List<Client> clients = clientRepository.findAll();
        List<Mouvement> mouvements = mouvementRepository.findAll();
        model.addAttribute("retourSecurite", retourSecurite);
        model.addAttribute("clients", clients);
        model.addAttribute("mouvements", mouvements);
        return "retourSecurites/RS_edit";
    }


    @PostMapping("/edit/{id}")
    public String updateRetourSecurite(@PathVariable int id, @Valid @ModelAttribute("retourSecurite") RetourSecuriteDTO retourSecuriteDTO) {
        retourSecuriteService.updateRetourSecurite(id, retourSecuriteDTO);
        return "redirect:/retourSecurite/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteRetourSecurite(@PathVariable int id) {
        retourSecuriteService.deleteRetourSecurite(id);
        return "redirect:/retourSecurite/list";
    }
}
