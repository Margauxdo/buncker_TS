package example.controller;

import example.entities.RetourSecurite;
import example.interfaces.IRetourSecuriteService;
import jakarta.persistence.EntityNotFoundException;
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
    public List<RetourSecurite> getAllRetourSecurites() {
        List<RetourSecurite> retourSecurites = retourSecuriteService.getAllRetourSecurites();
        return new ResponseEntity<>(retourSecurites, HttpStatus.OK).getBody();
    }

    @GetMapping("{id}")
    public ResponseEntity<RetourSecurite> getRetourSecuriteById(@PathVariable int id) {
        RetourSecurite retourSecurite = retourSecuriteService.getRetourSecurite(id);
        return retourSecurite != null ? new ResponseEntity<>(retourSecurite, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping
    public ResponseEntity<RetourSecurite> createRetourSecurite(@Valid @RequestBody RetourSecurite retourSecurite) {
        try {
            RetourSecurite createdRetourSecurite = retourSecuriteService.createRetourSecurite(retourSecurite);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRetourSecurite);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }







    @PutMapping("/{id}")
    public ResponseEntity<RetourSecurite> updateRetourSecurite(@PathVariable int id, @Valid @RequestBody RetourSecurite retourSecurite) {
        try {
            RetourSecurite updatedRetourSecurite = retourSecuriteService.updateRetourSecurite(id, retourSecurite);
            return new ResponseEntity<>(updatedRetourSecurite, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRetourSecurite(@PathVariable int id) {
        try {
            retourSecuriteService.deleteRetourSecurite(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
