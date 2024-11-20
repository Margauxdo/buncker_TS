package example.controller;

import example.entity.TypeRegle;
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
    public ResponseEntity<List<TypeRegle>> getTypeRegles() {
        List<TypeRegle> typeRegleList = typeRegleService.getTypeRegles();
        return ResponseEntity.ok(typeRegleList);
    }

    @GetMapping("{id}")
    public ResponseEntity<TypeRegle> getTypeRegle(@PathVariable int id) {
        TypeRegle typeRegle = typeRegleService.getTypeRegle(id);
        return typeRegle != null ? ResponseEntity.ok(typeRegle) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TypeRegle> createTypeRegle(@RequestBody TypeRegle typeRegle) {
        try {
            TypeRegle createdTypeRegle = typeRegleService.createTypeRegle(typeRegle);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTypeRegle);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<TypeRegle> updateTypeRegle(@PathVariable int id, @RequestBody TypeRegle typeRegle) {
        try {
            TypeRegle updatedTypeRegle = typeRegleService.updateTypeRegle(id, typeRegle);
            return ResponseEntity.ok(updatedTypeRegle);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTypeRegle(@PathVariable int id) {
        try {
            typeRegleService.deleteTypeRegle(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
