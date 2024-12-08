package example.controller;

import example.entity.Formule;
import example.entity.Regle;
import example.exceptions.FormuleNotFoundException;
import example.interfaces.IFormuleService;
import example.services.RegleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/formules")
public class FormuleController {

    @Autowired
    private IFormuleService formuleService;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(FormuleController.class);
    @Autowired
    private RegleService regleService;

    @ExceptionHandler(FormuleNotFoundException.class)
    public ResponseEntity<String> handleFormuleNotFoundException(FormuleNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>("Invalid data for Formula creation", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "formules/error";
    }

    // Vue Thymeleaf pour lister les formules
    @GetMapping("/list")
    public String viewFormuleList(Model model) {
        model.addAttribute("formules", formuleService.getAllFormules());
        return "formules/formule_list";
    }

    // Vue Thymeleaf pour voir une formule par ID
    @GetMapping("/view/{id}")
    public String viewFormuleById(@PathVariable int id, Model model) {
        model.addAttribute("formule", formuleService.getFormuleById(id));
        return "formules/formule_detail";
    }




    // Formulaire Thymeleaf pour créer une formule
    @GetMapping("/create")
    public String createFormuleForm(Model model) {
        model.addAttribute("formule", new Formule());
        return "formules/formule_create";
    }

    // Création d'une formule via formulaire Thymeleaf
    @PostMapping("/create")
    public String createFormule(@Valid @ModelAttribute("formule") Formule formule,
                                @RequestParam(value = "regle.id", required = false) Integer regleId, Model model) {
        try {
            // Log des données reçues
            logger.info("Creating formule...");
            logger.info("Received libelle: {}", formule.getLibelle());
            logger.info("Received formule: {}", formule.getFormule());
            logger.info("Received regle ID: {}", regleId);


            // Vérification et récupération de la règle associée
            if (regleId != null) {
                logger.info("Fetching Regle with ID: {}", regleId);
                Regle regle = regleService.getRegleById(regleId);
                formule.setRegle(regle);
                logger.info("Regle fetched successfully: {}", regle);
            } else {
                logger.warn("No Regle ID provided in the request.");
                throw new IllegalArgumentException("A valid Regle must be provided.");
            }

            // Création de la formule
            logger.info("Saving formule: {}", formule);
            formuleService.createFormule(formule);
            logger.info("Formule created successfully with ID: {}", formule.getId());

            return "redirect:/formules/list";
        } catch (Exception e) {
            // Log de l'erreur
            logger.error("Error occurred while creating formule: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            return "formules/formule_create";
        }
    }







    // Formulaire Thymeleaf pour modifier une formule
    @GetMapping("/edit/{id}")
    public String editFormuleForm(@PathVariable int id, Model model) {
        Formule formule = formuleService.getFormuleById(id);
        if (formule == null) {
            throw new EntityNotFoundException("Formule with ID " + id + " does not exist.");
        }
        model.addAttribute("formule", formule);
        return "formules/formule_edit";
    }


    // Modifier une formule via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateFormule(@PathVariable int id, @Valid @ModelAttribute("formule") Formule formule,
                                @RequestParam(value = "regle.id", required = false) Integer regleId, Model model) {
        try {
            if (regleId != null) {
                Regle regle = regleService.getRegleById(regleId);
                formule.setRegle(regle);
            }
            formuleService.updateFormule(id, formule);
            return "redirect:/formules/list";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Formule not found.");
            return "formules/formule_edit";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating formule.");
            return "formules/formule_edit";
        }

    }







    // Supprimer une formule via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFormule(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            formuleService.deleteFormule(id);
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/formules/list").build();
        } catch (FormuleNotFoundException ex) {
            logger.error("Error deleting Formule with ID: {}", id, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Unexpected error deleting Formule with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
