package example.controller;

import example.entities.TypeValise;
import example.interfaces.ITypeValiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/typeValise")
public class TypeValiseController {

    @Autowired
    private ITypeValiseService typeValiseService;

    @GetMapping
    public List<TypeValise> getTypeValises() {
        List<TypeValise> typeValises = typeValiseService.getTypeValises();
        return new ResponseEntity<>(typeValises, HttpStatus.OK).getBody();
    }

    @GetMapping("{id}")
    ResponseEntity<TypeValise> getTypeValise(@PathVariable int id) {
        TypeValise typeValise = typeValiseService.getTypeValise(id);
        return typeValise != null ? new ResponseEntity<>(typeValise, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<TypeValise> createTypeValise(@RequestBody TypeValise typeValise) {
        try{
            TypeValise createdTypeValise = typeValiseService.createTypeValise(typeValise);
            return new ResponseEntity<>(createdTypeValise, HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping
    public ResponseEntity<TypeValise> updateTypeValise(@RequestBody TypeValise typeValise, @PathVariable int id) {
        try {
            TypeValise updatedTypeValise = typeValiseService.updateTypeValise(id, typeValise);
            return updatedTypeValise != null ? new ResponseEntity<>(updatedTypeValise, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TypeValise> deleteTypeValise(@PathVariable int id) {
        try{
            typeValiseService.deleteTypeValise(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
