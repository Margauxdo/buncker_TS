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

    @PostMapping
    public ResponseEntity<RetourSecurite> createRetourSecurite(@RequestBody RetourSecurite retourSecurite) {
        try{
            RetourSecurite createdRS = retourSecuriteService.createRetourSecurite(retourSecurite);
            return new ResponseEntity<>(createdRS, HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<RetourSecurite> updateRetourSecurite(@PathVariable int id, @RequestBody RetourSecurite retourSecurite) {
        RetourSecurite updatedRS = retourSecuriteService.updateRetourSecurite(id, retourSecurite);
        return updatedRS != null ? new ResponseEntity<>(updatedRS, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<RetourSecurite> deleteRetourSecurite(@PathVariable int id) {
        retourSecuriteService.deleteRetourSecurite(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
