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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Vue Thymeleaf pour lister les valises
    @GetMapping("/list")
    public String viewValises(Model model) {
        List<Valise> valises = valiseService.getAllValises();
        model.addAttribute("valises", valises);
        return "valises/valises_list";
    }

    // Vue Thymeleaf pour voir une valise par ID
    @GetMapping("/view/{id}")
    public String viewValise(@PathVariable int id, Model model) {
        Valise valise = valiseService.getValiseById(id);
        model.addAttribute("valise", valise);
        return "valises/valise_details";
    }

    // Formulaire Thymeleaf pour créer une valise
    @GetMapping("/create")
    public String createValiseForm(Model model) {
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
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Veuillez corriger les erreurs.");
            return "valises/valise_create";
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + clientId));
        valise.setClient(client);
        valiseService.persistValise(valise);
        return "redirect:/valise/list";
    }



    // Formulaire Thymeleaf pour modifier une valise
    @GetMapping("/edit/{id}")
    public String editValiseForm(@PathVariable int id, Model model) {
        Valise valise = valiseService.getValiseById(id);
        if (valise == null) {
            return "valises/error";
        }
        model.addAttribute("valise", valise);
        return "valises/valise_edit";
    }
    // Modifier une valise via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateValise(@PathVariable int id, @Valid  @ModelAttribute("valise") Valise valise) {
        valiseService.updateValise(id, valise);
        return "redirect:/valise/list";
    }
    // Supprimer une valise via un formulaire Thymeleaf sécurisé

    @PostMapping("/delete/{id}")
    public String deleteValise(@PathVariable int id, Model model) {
        try {
            valiseService.deleteValise(id);
            return "redirect:/valises/valises_list";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("errorMessage", "Valise with ID " + id + " not found!");
            return "valises/error";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred while deleting the Valise.");
            return "valises/error";
        }
    }







}
