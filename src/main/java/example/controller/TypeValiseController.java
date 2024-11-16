package example.controller;

import example.entities.TypeValise;
import example.entities.Valise;
import example.interfaces.ITypeValiseService;
import example.interfaces.IValiseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/typeValise")
public class TypeValiseController {

    @Autowired
    private ITypeValiseService typeValiseService;
    @Autowired
    private IValiseService valiseService;

    @GetMapping
    public List<TypeValise> getTypeValises() {
        List<TypeValise> typeValises = typeValiseService.getTypeValises();
        return new ResponseEntity<>(typeValises, HttpStatus.OK).getBody();
    }

    @Transactional
    @GetMapping("{id}")
    ResponseEntity<TypeValise> getTypeValise(@PathVariable int id) {
        TypeValise typeValise = typeValiseService.getTypeValise(id);
        return typeValise != null ? new ResponseEntity<>(typeValise, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @PostMapping
    public ResponseEntity<TypeValise> createTypeValise(@RequestBody TypeValise typeValise) {
        try {
            if (typeValise == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            System.out.println("TypeValise reçu : " + typeValise);
            System.out.println("Valises reçues : " + (typeValise.getValises() != null ? typeValise.getValises() : "Aucune valise"));

            // Si des valises sont associées, récupérez-les explicitement depuis la base de données
            if (typeValise.getValises() != null && !typeValise.getValises().isEmpty()) {
                List<Valise> attachedValises = new ArrayList<>();
                for (Valise valise : typeValise.getValises()) {
                    Valise managedValise = valiseService.getValiseById(valise.getId());
                    if (managedValise == null) {
                        throw new EntityNotFoundException("Valise not found with ID: " + valise.getId());
                    }
                    managedValise.setTypevalise(typeValise); // Associe la valise au TypeValise
                    attachedValises.add(managedValise);
                }
                typeValise.setValises(attachedValises);
            }

            // Sauvegarder le TypeValise
            TypeValise createdTypeValise = typeValiseService.createTypeValise(typeValise);
            return new ResponseEntity<>(createdTypeValise, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("{id}")
    public ResponseEntity<TypeValise> updateTypeValise(@PathVariable int id, @RequestBody TypeValise typeValise) {
        try {
            TypeValise updatedTypeValise = typeValiseService.updateTypeValise(id, typeValise);
            return new ResponseEntity<>(updatedTypeValise, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }




    @DeleteMapping("{id}")
    public ResponseEntity<TypeValise> deleteTypeValise(@PathVariable int id) {
        try {
            typeValiseService.deleteTypeValise(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) { //  404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
