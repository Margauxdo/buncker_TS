package example.controller;

import example.entity.Client;
import example.entity.Valise;
import example.interfaces.IValiseService;
import example.exceptions.ResourceNotFoundException;
import example.repositories.ClientRepository;
import example.repositories.RegleRepository;
import example.repositories.TypeValiseRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static example.services.ValiseService.logger;

@Controller
@RequestMapping("/valise")
public class ValiseController {

    @Autowired
    private IValiseService valiseService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RegleRepository regleRepository;
    @Autowired
    private TypeValiseRepository typeValiseRepository;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

   /* // API REST: Récupérer tous les valises
    @GetMapping("/api")
    public ResponseEntity<List<Valise>> getAllValisesApi() {
        List<Valise> valises = valiseService.getAllValises();
        return new ResponseEntity<>(valises, HttpStatus.OK);
    }



    // API REST: Récupérer une valise par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<Valise> getValiseByIdApi(@PathVariable int id) {
        Valise valise = valiseService.getValiseById(id);
        if (valise == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(valise, HttpStatus.OK);
    }

    // API REST: Créer une valise
    @PostMapping("/api")
    public ResponseEntity<Valise> createValiseApi(@RequestBody Valise valise) {
        try {
            Valise createdValise = valiseService.createValise(valise);
            return new ResponseEntity<>(createdValise, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Argument invalide : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            logger.error("Ressource non trouvée : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            logger.error("Erreur interne du serveur : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // API REST: Modifier une valise
    @PutMapping("/api/{id}")
    public ResponseEntity<Valise> updateValiseApi(@RequestBody Valise valise, @PathVariable int id) {
        try {
            Valise updatedValise = valiseService.updateValise(id, valise);
            return updatedValise != null ? new ResponseEntity<>(updatedValise, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // API REST: Supprimer une valise
    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> deleteValiseApi(@PathVariable int id) {
        try {
            valiseService.deleteValise(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
   /*@ExceptionHandler(Exception.class)
   public String handleGlobalException(Exception ex, Model model) {
       model.addAttribute("errorMessage", ex.getMessage());
       return "valises/error";
   }*/


    // Vue Thymeleaf pour lister les valises
    @GetMapping("/list")
    public String viewValises(Model model) {
        logger.info("Controller - Get -> valises/list");
        try {
            List<Valise> valises = valiseService.getAllValises();
            model.addAttribute("valises", valises);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la récupération des valises : " + e.getMessage());
            return "valises/error";
        }
        return "valises/valises_list";
    }

    // Vue Thymeleaf pour voir une valise par ID
    @GetMapping("/view/{id}")
    public String viewValise(@PathVariable int id, Model model) {
        logger.info("Action 'Voir' déclenchée pour la valise avec ID : {}", id);
        try {
            Valise valise = valiseService.getValiseById(id);
            logger.debug("Valise récupérée pour affichage : {}", valise);
            model.addAttribute("valise", valise);
            return "valises/valise_details";
        } catch (Exception e) {
            logger.error("Erreur lors de l'affichage de la valise avec ID : {}", id, e);
            model.addAttribute("errorMessage", "Erreur lors du chargement de la valise : " + e.getMessage());
            return "valises/error";
        }
    }



    // Formulaire Thymeleaf pour créer une valise
    @GetMapping("/create")
    public String createValiseForm(Model model) {
        logger.info("Action 'Créer une nouvelle valise' déclenchée.");
        model.addAttribute("valise", new Valise());
        return "valises/valise_create";
    }


    // Création d'une valise via formulaire Thymeleaf
    @PostMapping("/create")
    public String createValise(
            @Valid @ModelAttribute("valise") Valise valise,
            @RequestParam("client.id") int clientId,
            BindingResult result,
            Model model) {
        logger.info("Action 'Créer' déclenchée pour une nouvelle valise avec client ID : {}", clientId);
        if (result.hasErrors()) {
            logger.warn("Erreurs de validation détectées lors de la création de la valise.");
            model.addAttribute("errorMessage", "Veuillez corriger les erreurs dans le formulaire.");
            return "valises/valise_create";
        }
        try {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID : " + clientId));
            valise.setClient(client);
            logger.debug("Client associé à la nouvelle valise : {}", client);

            valiseService.persistValise(valise);
            logger.info("Nouvelle valise créée avec succès : {}", valise);
            return "redirect:/valise/list";
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la valise", e);
            model.addAttribute("errorMessage", "Une erreur interne s'est produite : " + e.getMessage());
            return "valises/error";
        }
    }







    // Formulaire Thymeleaf pour modifier une valise
    @GetMapping("/edit/{id}")
    public String editValiseForm(@PathVariable int id, Model model) {
        logger.info("Action 'Modifier' déclenchée pour afficher le formulaire de la valise avec ID : {}", id);
        try {
            Valise valise = valiseService.getValiseById(id);
            logger.debug("Valise récupérée pour modification : {}", valise);
            model.addAttribute("valise", valise);
            return "valises/valise_edit";
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la valise pour modification avec ID : {}", id, e);
            model.addAttribute("errorMessage", "Erreur lors du chargement de la valise : " + e.getMessage());
            return "valises/error";
        }
    }


    // Modifier une valise via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateValise(
            @PathVariable int id,
            @Valid @ModelAttribute("valise") Valise valise,
            BindingResult result,
            Model model) {
        logger.info("Action 'Modifier' déclenchée pour sauvegarder les modifications de la valise avec ID : {}", id);
        if (result.hasErrors()) {
            logger.warn("Erreurs de validation détectées lors de la modification de la valise avec ID : {}", id);
            model.addAttribute("errorMessage", "Veuillez corriger les erreurs dans le formulaire.");
            return "valises/valise_edit";
        }
        try {
            Valise updatedValise = valiseService.updateValise(id, valise);
            logger.info("Valise modifiée avec succès : {}", updatedValise);
            return "redirect:/valise/list";
        } catch (Exception e) {
            logger.error("Erreur lors de la modification de la valise avec ID : {}", id, e);
            model.addAttribute("errorMessage", "Une erreur est survenue : " + e.getMessage());
            return "valises/error";
        }
    }



    @PostMapping("/delete/{id}")
    public String deleteValise(@PathVariable int id, Model model) {
        logger.info("Action 'Supprimer' déclenchée pour la valise avec ID : {}", id);
        try {
            valiseService.deleteValise(id);
            logger.info("Valise supprimée avec succès, ID : {}", id);
            return "redirect:/valise/list";
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la valise avec ID : {}", id, e);
            model.addAttribute("errorMessage", "Erreur lors de la suppression de la valise : " + e.getMessage());
            return "valises/error";
        }
    }








}
