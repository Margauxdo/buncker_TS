package example.controller;

import example.entity.TypeValise;
import example.entity.Valise;
import example.interfaces.ITypeValiseService;
import example.interfaces.IValiseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
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
        model.addAttribute("typeValises", typeValiseService.getTypeValises());
        return "typeValises/TV_list";
    }

    // Vue Thymeleaf pour voir un TV par ID
    @GetMapping("/view/{id}")
    public String viewTypeValiseById(@PathVariable int id, Model model) {
        TypeValise typeValise = typeValiseService.getTypeValise(id);
        if (typeValise == null) {
            model.addAttribute("errorMessage", "Type de valise avec l'Id" + id + " non trouvé");
            return "typeValises/error";
        }
        model.addAttribute("typeValise", typeValise);
        return "typeValises/TV_details";
    }
    // Formulaire Thymeleaf pour créer une TV
    @GetMapping("/create")
    public String createTypeValiseForm(Model model) {
        model.addAttribute("typeValise", new TypeValise());
        return "typeValises/TV_create";
    }

    // Création d'une TV via formulaire Thymeleaf
    @PostMapping("/create")
    public String createTypeValise(@Valid  @ModelAttribute("typeValise") TypeValise typeValise) {
        typeValiseService.createTypeValise(typeValise);
        return "redirect:/typeValises/TV_list";
    }

    // Formulaire Thymeleaf pour modifier un TV
    @GetMapping("/edit/{id}")
    public String editTypeValiseForm(@PathVariable int id, Model model) {
        TypeValise typeValise = typeValiseService.getTypeValise(id);
        if (typeValise == null) {
            model.addAttribute("errorMessage", "Le type de valise avec l'ID " + id + " n'a pas été trouvé.");
            return "typeValises/error";
        }
        model.addAttribute("typeValise", typeValise);
        return "typeValises/TV_edit";
    }

    // Modifier un TV via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String editTypeValise(@PathVariable int id, @Valid TypeValise typeValise) {
        typeValiseService.updateTypeValise(id, typeValise);
        return "redirect:/typeValises/TV_list";
    }

    // Supprimer un TV via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public String deleteTypeValise(@PathVariable int id, Model model) {
        try {
            typeValiseService.deleteTypeValise(id);
            return "redirect:/typeValises/TV_list";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "TypeValise avec l'ID " + id + " non trouvé !");
            return "typeValises/error";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite.");
            return "typeValises/error";
        }
    }








}
