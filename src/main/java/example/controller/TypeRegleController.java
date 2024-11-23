package example.controller;

import example.entity.TypeRegle;
import example.interfaces.ITypeRegleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/typeRegle")
public class TypeRegleController {

    @Autowired
    private ITypeRegleService typeRegleService;

    /*// API REST: Récupérer tous les TR
    @GetMapping("/api")
    public ResponseEntity<List<TypeRegle>> getTypeReglesApi() {
        List<TypeRegle> typeRegleList = typeRegleService.getTypeRegles();
        return ResponseEntity.ok(typeRegleList);
    }

    // API REST: Récupérer un TR par ID
    @GetMapping("/api/{id}")
    public ResponseEntity<TypeRegle> getTypeRegleApi(@PathVariable int id) {
        TypeRegle typeRegle = typeRegleService.getTypeRegle(id);
        return typeRegle != null ? ResponseEntity.ok(typeRegle) : ResponseEntity.notFound().build();
    }

    // API REST: Créer un TR
    @PostMapping("/api")
    public ResponseEntity<TypeRegle> createTypeRegleApi(@RequestBody TypeRegle typeRegle) {
        try {
            TypeRegle createdTypeRegle = typeRegleService.createTypeRegle(typeRegle);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTypeRegle);
        } catch (IllegalArgumentException | IllegalStateException e) { // Handle specific exceptions
            if (e instanceof IllegalStateException) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    // API REST: Modifier un TR
    @PutMapping("/api/{id}")
    public ResponseEntity<TypeRegle> updateTypeRegleApi(@PathVariable int id, @RequestBody TypeRegle typeRegle) {
        try {
            TypeRegle updatedTypeRegle = typeRegleService.updateTypeRegle(id, typeRegle);
            return ResponseEntity.ok(updatedTypeRegle);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API REST: Supprimer un TR
    @DeleteMapping("/api/{id}")
    public ResponseEntity<Void> deleteTypeRegleApi(@PathVariable int id) {
        try {
            typeRegleService.deleteTypeRegle(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/

    // Vue Thymeleaf pour lister les TR
    @GetMapping("/list")
    public String viewAllTypeRegles(Model model) {
        model.addAttribute("typeRegle", typeRegleService.getTypeRegles());
        return "typeRegles/TR_list";
    }

    // Vue Thymeleaf pour voir un TR par ID
    @GetMapping("/view/{id}")
    public String viewTypeRegleById(@PathVariable int id, Model model) {
        TypeRegle typeRegle = typeRegleService.getTypeRegle(id);
        if (typeRegle == null) {
            model.addAttribute("errorMessage", "typeRegle avec l'Id" + id + " non trouve !");
            return "typeRegles/error";
        }
        model.addAttribute("typeRegle", typeRegle);
        return "typeRegles/TP_details";
    }

    // Formulaire Thymeleaf pour créer un TR
    @GetMapping("/create")
    public String createTypeRegleForm(Model model) {
        model.addAttribute("typeRegle", new TypeRegle());
        return "typeRegles/TP_create";
    }

    // Création d'un client via formulaire Thymeleaf
    @PostMapping("/create")
    public String createTypeRegle(@Valid @ModelAttribute("typeRegle") TypeRegle typeRegle) {
        typeRegleService.createTypeRegle(typeRegle);
        return "redirect:/typeRegles/TR_list";
    }
    // Formulaire Thymeleaf pour modifier un TR
    @GetMapping("/edit/{id}")
    public String editTypeRegleForm(@PathVariable int id, Model model) {
        TypeRegle typeRegle = typeRegleService.getTypeRegle(id);
        if (typeRegle  == null) {
            return "typeRegles/error";
        }
        model.addAttribute("typeRegle", typeRegle);
        return "typeRegles/TR_edit";
    }

    // Modifier un TR via formulaire Thymeleaf
    @PostMapping("/edit/{id}")
    public String updateTypeRegle(@PathVariable int id,@Valid @ModelAttribute("typeRegle") TypeRegle typeRegle) {
        typeRegleService.updateTypeRegle(id, typeRegle);
        return "redirect:/typeRegles/TR_list";
    }
    // Supprimer un TR via un formulaire Thymeleaf sécurisé
    @PostMapping("/delete/{id}")
    public String deleteTypeRegle(@PathVariable int id, Model model) {
        try {
            typeRegleService.deleteTypeRegle(id);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "TypeRegle avec l'ID " + id + " non trouvé !");
            return "typeRegles/error";
        }
        return "redirect:/typeRegles/TR_list";
    }






}
