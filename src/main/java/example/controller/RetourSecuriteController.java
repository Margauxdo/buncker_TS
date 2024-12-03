package example.controller;

import example.entity.RetourSecurite;
import example.interfaces.IRetourSecuriteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/retourSecurite")
public class RetourSecuriteController {

    @Autowired
    private IRetourSecuriteService retourSecuriteService;

    /*// API REST: Récupérer tous les RS
    @GetMapping("/api")
    public ResponseEntity<List<RetourSecurite>> getAllRetourSecuritesApi() {
        List<RetourSecurite> retourSecurites = retourSecuriteService.getAllRetourSecurites();
        return ResponseEntity.ok(retourSecurites);
    }

    // API REST: Récupérer un RS par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<RetourSecurite> getRetourSecuriteByIdApi(@PathVariable int id) {
        RetourSecurite retourSecurite = retourSecuriteService.getRetourSecurite(id);
        return retourSecurite != null ? ResponseEntity.ok(retourSecurite) : ResponseEntity.notFound().build();
    }

    // API REST: Créer un RS
    @PostMapping("/api")
    public ResponseEntity<RetourSecurite> createRetourSecuriteApi(@Valid @RequestBody RetourSecurite retourSecurite) {
        try {
            RetourSecurite createdRetourSecurite = retourSecuriteService.createRetourSecurite(retourSecurite);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRetourSecurite);
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // API REST: Modifier un RS
    @PutMapping("/api/{id}")
    public ResponseEntity<RetourSecurite> updateRetourSecuriteApi(@PathVariable int id, @Valid @RequestBody RetourSecurite retourSecurite) {
        try {
            RetourSecurite updatedRetourSecurite = retourSecuriteService.updateRetourSecurite(id, retourSecurite);
            return ResponseEntity.ok(updatedRetourSecurite);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // API REST: Supprimer un RS
    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> deleteRetourSecuriteApi(@PathVariable int id) {
        try {
            retourSecuriteService.deleteRetourSecurite(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
    // Vue Thymeleaf pour lister les RS
    @GetMapping("/list")
    public String viewAllRetourSecurites(Model model) {
        model.addAttribute("retourSecurites", retourSecuriteService.getAllRetourSecurites());
        return "retourSecurites/RS_list";
    }

    // Vue Thymeleaf pour voir un RS par ID
    @GetMapping("/view/{id}")
    public String viewRetourSecuriteById(@PathVariable int id, Model model) {
        RetourSecurite retourSecurite = retourSecuriteService.getRetourSecurite(id);
        if (retourSecurite == null ){
            model.addAttribute("errorMessage", "Retour securité avec l'ID" + id + "non trouve");
            return "retourSecurites/error";
        }
        model.addAttribute("retourSecurite", retourSecurite);
        return "retourSecurites/RS_details";
    }
    // Formulaire Thymeleaf pour créer un RS
    @GetMapping("/create")
    public String createRetourSecuriteForm(Model model) {
        model.addAttribute("retourSecurite", new RetourSecurite());
        return "retourSecurites/RS_create";
    }
    // Création d'un client via formulaire Thymeleaf
    @PostMapping("/create")
    public String createRetourSecurite(@Valid @ModelAttribute("retourSecurite") RetourSecurite retourSecurite) {
        retourSecuriteService.createRetourSecurite(retourSecurite);
        return "redirect:/retourSecurites/RS_list";
    }

    // Formulaire Thymeleaf pour modifier un RS
    @GetMapping("/edit/{id}")
    public String editRetourSecuriteForm(@PathVariable int id, Model model) {
        RetourSecurite ret = retourSecuriteService.getRetourSecurite(id);
        if (ret == null) {
            return "retourSecurites/error";
        }
        model.addAttribute("retourSecurite", ret);
        return "retourSecurites/RS_edit";
    }
    // Modifier un RS via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateRetourSecurite(@PathVariable int id, @Valid @ModelAttribute("retourSecurite") RetourSecurite retourSecurite){
        retourSecuriteService.updateRetourSecurite(id, retourSecurite);
        return "redirect:/retourSecurites/RS_list";
    }
    // Supprimer un RS via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public String deleteRetourSecurite(@PathVariable int id) {
        try {
            retourSecuriteService.deleteRetourSecurite(id);
        } catch (EntityNotFoundException e) {
            return "redirect:/retourSecurites/RS_list";
        }
        return "redirect:/retourSecurites/RS_list";
    }



}
