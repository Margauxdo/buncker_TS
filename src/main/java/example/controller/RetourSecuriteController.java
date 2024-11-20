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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retourSecurite")
public class RetourSecuriteController {

    @Autowired
    private IRetourSecuriteService retourSecuriteService;

    @GetMapping
    public ResponseEntity<List<RetourSecurite>> getAllRetourSecurites() {
        List<RetourSecurite> retourSecurites = retourSecuriteService.getAllRetourSecurites();
        return ResponseEntity.ok(retourSecurites);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RetourSecurite> getRetourSecuriteById(@PathVariable int id) {
        RetourSecurite retourSecurite = retourSecuriteService.getRetourSecurite(id);
        return retourSecurite != null ? ResponseEntity.ok(retourSecurite) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<RetourSecurite> createRetourSecurite(@Valid @RequestBody RetourSecurite retourSecurite) {
        try {
            RetourSecurite createdRetourSecurite = retourSecuriteService.createRetourSecurite(retourSecurite);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRetourSecurite);
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RetourSecurite> updateRetourSecurite(@PathVariable int id, @Valid @RequestBody RetourSecurite retourSecurite) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRetourSecurite(@PathVariable int id) {
        try {
            retourSecuriteService.deleteRetourSecurite(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
