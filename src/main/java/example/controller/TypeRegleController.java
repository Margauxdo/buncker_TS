package example.controller;

import example.entities.TypeRegle;
import example.interfaces.ITypeRegleService;
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
       }catch (RuntimeException e) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
    @PutMapping
    public ResponseEntity<TypeRegle> updateTypeRegle(@RequestBody TypeRegle typeRegle, @PathVariable int id) {
        TypeRegle updatedTypeReggle = typeRegleService.updateTypeRegle(id, typeRegle);
        return updatedTypeReggle != null ? new ResponseEntity<>(updatedTypeReggle, HttpStatus.OK):
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TypeRegle> deleteTypeRegle(@PathVariable int id) {
        typeRegleService.deleteTypeRegle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
