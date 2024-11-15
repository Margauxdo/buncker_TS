package example.controller;

import example.entities.TypeRegle;
import example.interfaces.ITypeRegleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/typeRegle")
public class TypeRegleController {

    @Autowired
    private ITypeRegleService typeRegleService;

    @GetMapping
    public List<TypeRegle> getTypeRegles() {
        List<TypeRegle> typeRegleList = typeRegleService.getTypeRegles();
        return new ResponseEntity<>(typeRegleList, HttpStatus.OK).getBody();
    }

    @GetMapping("{id}")
    public ResponseEntity<TypeRegle> getTypeRegle(@PathVariable int id) {
        TypeRegle typeRegle = typeRegleService.getTypeRegle(id);
        return typeRegle != null ? new ResponseEntity<>(typeRegle, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<TypeRegle> createTypeRegle(@RequestBody TypeRegle typeRegle) {
       try{
           TypeRegle createdTypeRegle = typeRegleService.createTypeRegle(typeRegle);
           return new ResponseEntity<>(createdTypeRegle, HttpStatus.CREATED);
       }catch (IllegalArgumentException e) {
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }catch (IllegalStateException e){
           return new ResponseEntity<>(HttpStatus.CONFLICT);
       }catch (RuntimeException e) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
    @PutMapping("{id}")
    public ResponseEntity<TypeRegle> updateTypeRegle(@PathVariable int id, @RequestBody TypeRegle typeRegle) {
        try {
            TypeRegle updatedTypeRegle = typeRegleService.updateTypeRegle(id, typeRegle);
            return new ResponseEntity<>(updatedTypeRegle, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @DeleteMapping("{id}")
    public ResponseEntity<TypeRegle> deleteTypeRegle(@PathVariable int id) {
        try {
            typeRegleService.deleteTypeRegle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) { //  404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
