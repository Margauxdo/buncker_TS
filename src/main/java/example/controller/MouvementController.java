package example.controller;

import example.entity.Mouvement;
import example.interfaces.IMouvementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/mouvements")
public class MouvementController {

    @Autowired
    private IMouvementService mouvementService;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(MouvementController.class);


    @GetMapping
    public List<Mouvement> getAllMouvements() {
        List<Mouvement> mouvements = mouvementService.getAllMouvements();
        return new ResponseEntity<>(mouvements, HttpStatus.OK).getBody();
    }

    @GetMapping("{id}")
    public ResponseEntity<Mouvement> getMouvementById(@PathVariable int id) {
        Mouvement mouvement = mouvementService.getMouvementById(id);
        return mouvement != null ? new ResponseEntity<>(mouvement, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Mouvement> createMouvement(@RequestBody Mouvement mouvement) {
       logger.info("entrée --> create mouvement{} " ,mouvement);
        try {
            if (mouvement.getDateHeureMouvement() == null || mouvement.getStatutSortie() == null || mouvement.getStatutSortie().length() < 3) {
                logger.info("entrée --> create mouvement{} " ,mouvement);

                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Mouvement createdMouvement = mouvementService.createMouvement(mouvement);
            logger.info("Mouement creer --> create mouvement{} " ,mouvement);

            return new ResponseEntity<>(createdMouvement, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.info("Create mouvement --> IllegalArgumentException");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            logger.info("Create mouvement --> RuntimeException");

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @PutMapping("/{id}")
    public ResponseEntity<Mouvement> updateMouvement(@PathVariable int id, @RequestBody Mouvement mouvement) {
        try {
            if (!mouvementService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (mouvement.getStatutSortie() == null || mouvement.getStatutSortie().length() < 3
                    || mouvement.getDateHeureMouvement() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Mouvement updatedMouvement = mouvementService.updateMouvement(id, mouvement);
            return new ResponseEntity<>(updatedMouvement, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable int id) {
        try {
            if (!mouvementService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            mouvementService.deleteMouvement(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
