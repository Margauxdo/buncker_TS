package example.integration.services;

import example.DTO.SortieSemaineDTO;
import example.entity.Regle;
import example.entity.SortieSemaine;
import example.repositories.RegleRepository;
import example.repositories.SortieSemaineRepository;
import example.services.SortieSemaineService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class SortieSemaineServiceIntegrationTest {

    @Autowired
    private SortieSemaineService sortieSemaineService;

    @Autowired
    private SortieSemaineRepository sortieSemaineRepository;

    @Autowired
    private RegleRepository regleRepository;

    private SortieSemaineDTO semaineDTO;

    @BeforeEach
    public void setUp() {
        sortieSemaineRepository.deleteAll();
        regleRepository.deleteAll();

        Regle regle = new Regle();
        regle.setCoderegle("25463AL");
        regle = regleRepository.save(regle);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse("01/01/2016");

            semaineDTO = SortieSemaineDTO.builder()
                    .dateSortieSemaine(date)
                    .regleIds(Collections.singletonList(regle.getId()))
                    .build();
            semaineDTO = sortieSemaineService.createSortieSemaine(semaineDTO);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateSortieSemaine() {
        // Act
        SortieSemaineDTO savedDTO = sortieSemaineService.createSortieSemaine(semaineDTO);

        // Assert
        assertNotNull(savedDTO);
        assertNotNull(savedDTO.getDateSortieSemaine());
        assertNotNull(savedDTO.getRegleIds());
        assertEquals(semaineDTO.getRegleIds(), savedDTO.getRegleIds());
    }


    @Test
    public void testDeleteSortieSemaine() {
        // Arrange
        int id = semaineDTO.getId();

        // Act
        sortieSemaineService.deleteSortieSemaine(id);

        // Assert
        boolean exists = sortieSemaineRepository.findById(id).isPresent();
        assertFalse(exists);
    }

    @Test
    public void testGetSortieSemaine() {
        // Act
        SortieSemaineDTO foundDTO = sortieSemaineService.getSortieSemaine(semaineDTO.getId());

        // Assert
        assertNotNull(foundDTO);
        assertEquals(semaineDTO.getId(), foundDTO.getId());
    }

    @Test
    public void testGetAllSortieSemaine() {
        sortieSemaineRepository.deleteAll();
        regleRepository.deleteAll();

        // Arrange
        Regle regle1 = new Regle();
        regle1.setCoderegle("25445ML");
        regle1 = regleRepository.save(regle1);

        Regle regle2 = new Regle();
        regle2.setCoderegle("569823AL");
        regle2 = regleRepository.save(regle2);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse("25/11/2024");
            Date date2 = sdf.parse("20/11/2024");

            SortieSemaineDTO dto1 = SortieSemaineDTO.builder()
                    .dateSortieSemaine(date1)
                    .regleIds(Collections.singletonList(regle1.getId()))
                    .build();

            SortieSemaineDTO dto2 = SortieSemaineDTO.builder()
                    .dateSortieSemaine(date2)
                    .regleIds(Collections.singletonList(regle2.getId()))
                    .build();

            sortieSemaineService.createSortieSemaine(dto1);
            sortieSemaineService.createSortieSemaine(dto2);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Act
        List<SortieSemaineDTO> sortieSemaines = sortieSemaineService.getAllSortieSemaine();

        // Assert
        assertNotNull(sortieSemaines);
        assertEquals(2, sortieSemaines.size());
    }
}
