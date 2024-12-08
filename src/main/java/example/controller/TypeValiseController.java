package example.controller;

import example.entity.TypeValise;
import example.entity.Valise;
import example.interfaces.ITypeValiseService;
import example.interfaces.IValiseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/typeValise")
public class TypeValiseController {

    @Autowired
    private ITypeValiseService typeValiseService;
    @Autowired
    private IValiseService valiseService;

    /*// API REST: Récupérer tous les TV
    @GetMapping("/api")
    public List<TypeValise> getTypeValisesApi() {
        List<TypeValise> typeValises = typeValiseService.getTypeValises();
        return new ResponseEntity<>(typeValises, HttpStatus.OK).getBody();
    }

    // API REST: Récupérer un TV par ID
    @Transactional
    @GetMapping("/api/{id}")
    ResponseEntity<TypeValise> getTypeValiseApi(@PathVariable int id) {
        TypeValise typeValise = typeValiseService.getTypeValise(id);
        return typeValise != null ? new ResponseEntity<>(typeValise, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    // API REST: Créer un TV
    @PostMapping("/api")
    public ResponseEntity<TypeValise> createTypeValiseApi(@RequestBody TypeValise typeValise) {
        try {
            if (typeValise == null || typeValise.getValise() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Valise managedValise = valiseService.getValiseById(typeValise.getValise().getId());
            if (managedValise == null) {
                throw new EntityNotFoundException("Valise not found with ID: " + typeValise.getValise().getId());
            }

            typeValise.setValise(managedValise);
            TypeValise createdTypeValise = typeValiseService.createTypeValise(typeValise);
            return new ResponseEntity<>(createdTypeValise, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Conflit détecté
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // API REST: Modifier un TV
    @PutMapping("/api/{id}")
    public ResponseEntity<TypeValise> updateTypeValiseApi(@PathVariable int id, @RequestBody TypeValise typeValise) {
        try {
            TypeValise updatedTypeValise = typeValiseService.updateTypeValise(id, typeValise);
            return new ResponseEntity<>(updatedTypeValise, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // API REST: Supprimer un TV
    @DeleteMapping("/api/{id}")
    public ResponseEntity<TypeValise> deleteTypeValiseApi(@PathVariable int id) {
        try {
            typeValiseService.deleteTypeValise(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) { //  404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    // Vue Thymeleaf pour lister les TV
    @GetMapping("/list")
    public String viewTypeValises(Model model) {
        try{
            List<TypeValise> typeValises = typeValiseService.getTypeValises();
            model.addAttribute("typeValises", typeValises);
        }catch (Exception e){
            model.addAttribute("errorMessage", "Erreur lors de la récupération des valises : " + e.getMessage());
            return "typeValises/error";
        }

        return "typeValises/TV_list";
    }

    // Vue Thymeleaf pour voir un TV par ID
    @GetMapping("/view/{id}")
    public String viewTypeValiseById(@PathVariable int id, Model model) {
        TypeValise typeValise = typeValiseService.getTypeValise(id);
        if (typeValise == null) {
            model.addAttribute("errorMessage", "Type de valise avec l'ID " + id + " non trouvé.");
            return "typeValises/error";
        }
        model.addAttribute("typeValise", typeValise);
        return "typeValises/TV_details";
    }

    // Formulaire Thymeleaf pour créer une TV
    @GetMapping("/create")
    public String createTypeValiseForm(Model model) {
        model.addAttribute("typeValise", new TypeValise());

        // Charger toutes les valises
        List<Valise> valises = valiseService.getAllValises();
        model.addAttribute("valises", valises); // Ajout des valises au modèle

        return "typeValises/TV_create";
    }






    // Création d'une TV via formulaire Thymeleaf
    @PostMapping("/create")
    public String createTypeValise(@Valid @ModelAttribute("typeValise") TypeValise typeValise,
                                   @RequestParam("valise.id") Long valiseId, Model model) {
        try {
            if (typeValise.getProprietaire() == null || typeValise.getProprietaire().isEmpty()) {
                model.addAttribute("errorMessage", "Le champ 'Propriétaire' est obligatoire.");
                return "typeValises/TV_create";
            }

            if (valiseId == null) {
                model.addAttribute("errorMessage", "Une valise doit être sélectionnée.");
                return "typeValises/TV_create";
            }

            // Récupérer la Valise
            Valise valise = valiseService.getValiseById(valiseId.intValue());
            typeValise.setValise(valise);

            // Sauvegarder TypeValise
            typeValiseService.createTypeValise(typeValise);

            return "redirect:/typeValise/list";

        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "La valise sélectionnée n'existe pas.");
            return "typeValises/TV_create";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur inattendue : " + e.getMessage());
            return "typeValises/TV_create";
        }
    }










    // Formulaire Thymeleaf pour modifier un TV
    @GetMapping("/edit/{id}")
    public String editTypeValiseForm(@PathVariable int id, Model model) {
        try {
            TypeValise typeValise = typeValiseService.getTypeValise(id);
            if (typeValise == null) {
                model.addAttribute("errorMessage", "Type de valise avec l'ID " + id + " non trouvé.");
                return "typeValises/error";
            }
            model.addAttribute("typeValise", typeValise);
            model.addAttribute("valises", valiseService.getAllValises());
            return "typeValises/TV_edit";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur inattendue : " + e.getMessage());
            return "typeValises/error";
        }
    }



    // Modifier un TV via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String editTypeValise(@PathVariable int id,
                                 @Valid @ModelAttribute("typeValise") TypeValise typeValise,
                                 @RequestParam("valise.id") Long valiseId, Model model) {
        try {
            // Vérifier si le champ proprietaire est vide
            if (typeValise.getProprietaire() == null || typeValise.getProprietaire().trim().isEmpty()) {
                model.addAttribute("errorMessage", "Le champ 'Propriétaire' est obligatoire.");
                model.addAttribute("valises", valiseService.getAllValises());
                return "typeValises/TV_edit";
            }

            // Récupérer la Valise associée
            Valise valise = valiseService.getValiseById(valiseId.intValue());
            typeValise.setValise(valise);

            // Mettre à jour l'entité
            typeValiseService.updateTypeValise(id, typeValise);
            return "redirect:/typeValise/list";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "La valise sélectionnée n'existe pas.");
            model.addAttribute("valises", valiseService.getAllValises());
            return "typeValises/TV_edit";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur inattendue : " + e.getMessage());
            model.addAttribute("valises", valiseService.getAllValises());
            return "typeValises/TV_edit";
        }
    }






    // Supprimer un TV via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public String deleteTypeValise(@PathVariable int id, Model model) {
        try {
            typeValiseService.deleteTypeValise(id);
            return "redirect:/typeValise/list";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Type de valise avec l'ID " + id + " non trouvé.");
            return "typeValises/error";
        }
    }



}









