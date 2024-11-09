package example.integration.repositories;

import example.entities.Regle;
import example.entities.SortieSemaine;
import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class SortieSemaineRepositoryIntegrationTest {

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;
    @Autowired
    private RegleRepository regleRepository;

    @BeforeEach
    public void setUp() {
        sortieSemaineRepository.deleteAll();
    }

    @Test
    public void testSaveSortieSemaine() {
        Regle regle = new Regle();
        regle.setCoderegle("code 2524Al");
        regleRepository.save(regle);
        SortieSemaine sortieSem = new SortieSemaine();
        sortieSem.setSortieSemaine(new Date());
        sortieSem.setRegle(regle);
        SortieSemaine savedSS = sortieSemaineRepository.save(sortieSem);
        assertNotNull(savedSS.getId());
        assertEquals(regle.getCoderegle(), savedSS.getRegle().getCoderegle());

    }
    @Test
    public void testFindSortieSemaineById() {
        Regle regle = new Regle();
        regle.setCoderegle("code 2524Al");
        regleRepository.save(regle);
        SortieSemaine ss = new SortieSemaine();
        ss.setSortieSemaine(new Date());
        ss.setRegle(regle);
        SortieSemaine savedSS = sortieSemaineRepository.save(ss);
        Optional<SortieSemaine> foundSS = sortieSemaineRepository.findById(savedSS.getId());
        assertTrue(foundSS.isPresent());
        assertEquals(regle.getCoderegle(), foundSS.get().getRegle().getCoderegle());
    }
    @Test
    public void testDeleteSortieSemaine(){
        Regle regle = new Regle();
        regle.setCoderegle("code 2524Al");
        regleRepository.save(regle);
        SortieSemaine ss = new SortieSemaine();
        ss.setSortieSemaine(new Date());
        ss.setRegle(regle);
        SortieSemaine savedSS = sortieSemaineRepository.save(ss);
        sortieSemaineRepository.deleteById(ss.getId());
        Optional<SortieSemaine> deleteSS = sortieSemaineRepository.findById(savedSS.getId());
        assertFalse(deleteSS.isPresent());
    }
    @Test
    public void testUpdateSortieSemaine(){
        Regle regleA = new Regle();
        regleA.setCoderegle("code 2524Al");
        regleRepository.save(regleA);
        SortieSemaine ss = new SortieSemaine();
        ss.setSortieSemaine(new Date());
        ss.setRegle(regleA);
        SortieSemaine savedSS = sortieSemaineRepository.save(ss);
        savedSS.setRegle(regleA);
        SortieSemaine updatedSS = sortieSemaineRepository.save(savedSS);
        Optional<SortieSemaine> foundSS = sortieSemaineRepository.findById(updatedSS.getId());
        assertTrue(foundSS.isPresent());
        assertEquals(regleA.getCoderegle(), foundSS.get().getRegle().getCoderegle());

    }
    @Test
    public void testFindAllSortieSemaine(){
        Regle regleA = new Regle();
        regleA.setCoderegle("code 2524Al");
        regleRepository.save(regleA);
        Regle regleB = new Regle();
        regleB.setCoderegle("code 2568Bl");
        regleRepository.save(regleB);
        SortieSemaine ssA = new SortieSemaine();
        ssA.setSortieSemaine(new Date());
        ssA.setRegle(regleA);
        sortieSemaineRepository.save(ssA);
        SortieSemaine ssB = new SortieSemaine();
        ssB.setSortieSemaine(new Date());
        ssB.setRegle(regleB);
        sortieSemaineRepository.save(ssB);
        List<SortieSemaine> listSS = sortieSemaineRepository.findAll();
        assertNotNull(listSS);
        assertEquals(2, listSS.size());


    }

    @Test
    public void testFindSortieSemaineByDate() {
        Regle regle = new Regle();
        regle.setCoderegle("code 2524Al");
        regleRepository.save(regle);

        Date dateSortie = new Date();
        SortieSemaine ss = new SortieSemaine();
        ss.setDateSortieSemaine(dateSortie);
        ss.setRegle(regle);
        sortieSemaineRepository.save(ss);

        List<SortieSemaine> sorties = sortieSemaineRepository.findByDateSortieSemaine(dateSortie);
        assertNotNull(sorties);
        assertEquals(1, sorties.size(), "There should be only one release for this date");
        assertEquals(regle.getCoderegle(), sorties.get(0).getRegle().getCoderegle());
    }
    @Test
    public void testSaveSortieSemaineWithoutRegle() {
        SortieSemaine sortieSem = new SortieSemaine();
        sortieSem.setDateSortieSemaine(new Date());

        assertThrows(Exception.class, () -> {
            sortieSemaineRepository.save(sortieSem);
        }, "An exception should be thrown if the Rule is missing");
    }
    @Test
    public void testUpdateSortieSemaineDate() {
        Regle regle = new Regle();
        regle.setCoderegle("code 2524Al");
        regleRepository.save(regle);

        SortieSemaine ss = new SortieSemaine();
        ss.setDateSortieSemaine(new Date());
        ss.setRegle(regle);
        SortieSemaine savedSS = sortieSemaineRepository.save(ss);

        Date newDate = new Date(System.currentTimeMillis() + 86400000);
        savedSS.setDateSortieSemaine(newDate);
        sortieSemaineRepository.save(savedSS);

        Optional<SortieSemaine> foundSS = sortieSemaineRepository.findById(savedSS.getId());
        assertTrue(foundSS.isPresent());
        assertEquals(newDate, foundSS.get().getDateSortieSemaine(), "The output date should be updated");
    }








}
