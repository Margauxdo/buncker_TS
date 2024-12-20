package example.controller;

import example.DTO.*;
import example.entity.Formule;
import example.exceptions.RegleNotFoundException;
import example.interfaces.IRegleService;
import example.services.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Controller
@RequestMapping("/regles")
public class RegleController {

    private static final Logger logger = LoggerFactory.getLogger(RegleController.class);


    @Autowired
    private IRegleService regleService;
    @Autowired
    private FormuleService formuleService;
    @Autowired
    private ValiseService valiseService;
    @Autowired
    private TypeRegleService typeRegleService;
    @Autowired
    private JourFerieService jourFerieService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private RetourSecuriteService retourSecuriteService;

    public RegleController(RegleService regleService) {
        this.regleService = regleService;
    }

    @GetMapping("/list")
    public String listRegles(Model model) {

        List<RegleDTO> reglesDTO = regleService.getAllRegles();

        model.addAttribute("regles", reglesDTO);

        return "regles/regle_list";
    }

    @GetMapping("/view/{id}")
    public String viewRetourSecuriteById(@PathVariable int id, Model model) {
        RetourSecuriteDTO retourSecurite = retourSecuriteService.getRetourSecurite(id);
        model.addAttribute("retourSecurite", retourSecurite);

        // Ajouter les mouvements au modèle
        List<String> mouvements = retourSecurite.getMouvementStatuts();
        model.addAttribute("mouvements", mouvements);

        return "retourSecurites/RS_details";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("regleDTO", new RegleDTO());
        model.addAttribute("valises", valiseService.getAllValises());
        model.addAttribute("typesRegle", typeRegleService.getTypeRegles());
        model.addAttribute("formules", formuleService.getAllFormules());
        return "regle_create";
    }






    @PostMapping("/create")
    public String createRegle(@ModelAttribute("regle") @Valid RegleDTO regleDTO, BindingResult result, Model model) {
        logger.info("Création d'une règle avec coderegle : {}", regleDTO.getCoderegle());
        if (result.hasErrors()) {
            model.addAttribute("typesRegle", typeRegleService.getTypeRegles());
            model.addAttribute("formules", formuleService.getAllFormules());
            model.addAttribute("joursFeries", jourFerieService.getJourFeries());
            model.addAttribute("clients", clientService.getAllClients());
            return "regles/regle_create";
        }
        regleService.createRegle(regleDTO);
        return "redirect:/regles/list";
    }




    @GetMapping("/edit/{id}")
    public String editRegle(@PathVariable int id, Model model) {
        RegleDTO regle = regleService.getRegleById(id); // Récupère la règle
        model.addAttribute("regle", regle);
        model.addAttribute("valises", valiseService.getAllValises()); // Liste des valises
        model.addAttribute("typesRegle", typeRegleService.getTypeRegles()); // Liste des types de règles
        model.addAttribute("formules", formuleService.getAllFormules()); // Liste des formules
        model.addAttribute("joursFeries", jourFerieService.getJourFeries()); // Liste des jours fériés
        return "regles/regle_edit";
    }



    @PostMapping("/edit/{id}")
    public String updateRegle(@PathVariable int id, @ModelAttribute("regle") RegleDTO regleDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "regles/regle_edit"; // Retourne à la vue de modification si des erreurs sont détectées
        }
        regleService.updateRegle(id, regleDTO); // Met à jour la règle
        return "redirect:/regles/list"; // Redirige vers la liste des règles après la modification
    }


    @PostMapping("/delete/{id}")
    public String deleteRegle(@PathVariable String id, RedirectAttributes redirectAttributes) {
        logger.info("Requête reçue pour supprimer la règle avec ID : {}", id);
        try {
            regleService.deleteRegle(id);
            redirectAttributes.addFlashAttribute("successMessage", "La règle a été supprimée avec succès.");
            logger.info("La règle avec ID : {} a été supprimée avec succès.", id);
        } catch (RegleNotFoundException e) {
            logger.error("Erreur : La règle avec ID : {} n'a pas été trouvée.", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : La règle n'a pas été trouvée.");
        } catch (Exception e) {
            logger.error("Une erreur inattendue s'est produite lors de la suppression de la règle avec ID : {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue lors de la suppression.");
        }
        return "redirect:/regles/list";
    }

}




