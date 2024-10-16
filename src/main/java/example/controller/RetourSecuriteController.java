package example.controller;

import example.entities.RetourSecurite;
import example.interfaces.IRetourSecuriteService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/retourSecurite")
    public ResponseEntity<RetourSecurite> createRetourSecurite(@RequestBody RetourSecurite retourSecurite) {
        if (retourSecurite == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            RetourSecurite createdRetourSecurite = retourSecuriteService.createRetourSecurite(retourSecurite);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRetourSecurite);
        } catch (IllegalArgumentException e) { // Catch IllegalArgumentException
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }






    @PutMapping("/{id}") // Assurez-vous que la méthode utilise le bon mappage
    public ResponseEntity<RetourSecurite> updateRetourSecurite(@PathVariable int id, @RequestBody RetourSecurite retourSecurite) {
        try {
            RetourSecurite updatedRS = retourSecuriteService.updateRetourSecurite(id, retourSecurite);
            return updatedRS != null ? new ResponseEntity<>(updatedRS, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) { // Ajoutez ce bloc pour gérer les exceptions Runtime
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping
    public ResponseEntity<RetourSecurite> deleteRetourSecurite(@PathVariable int id) {
        try{
            retourSecuriteService.deleteRetourSecurite(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
