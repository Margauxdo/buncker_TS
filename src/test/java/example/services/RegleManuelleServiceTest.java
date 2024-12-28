/*package example.services;

import example.DTO.RegleManuelleDTO;
import example.entity.RegleManuelle;
import example.repositories.RegleManuelleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class RegleManuelleServiceTest {

    @Mock
    private RegleManuelleRepository regleManuelleRepository;

    @InjectMocks
    private RegleManuelleService regleManuelleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private RegleManuelle toEntity(RegleManuelleDTO dto) {
        return RegleManuelle.builder()
                .id(dto.getId())
                .coderegle(dto.getCoderegle())
                .reglePourSortie(dto.getReglePourSortie())
                .dateRegle(dto.getDateRegle())
                .descriptionRegle(dto.getDescriptionRegle())
                .build();
    }

    private RegleManuelleDTO toDTO(RegleManuelle entity) {
        return RegleManuelleDTO.builder()
                .id(entity.getId())
                .coderegle(entity.getCoderegle())
                .reglePourSortie(entity.getReglePourSortie())
                .dateRegle(entity.getDateRegle())
                .descriptionRegle(entity.getDescriptionRegle())
                .build();
    }

    @Test
    public void testCreateRegleManuelle_Success() {
        RegleManuelleDTO regleManuelleDTO = new RegleManuelleDTO();
        regleManuelleDTO.setCoderegle("REGLE001");

        RegleManuelle regleManuelle = toEntity(regleManuelleDTO);
        when(regleManuelleRepository.save(any(RegleManuelle.class))).thenReturn(regleManuelle);

        RegleManuelleDTO result = regleManuelleService.createRegleManuelle(regleManuelleDTO);

        Assertions.assertNotNull(result, "Manual rule must not be null");
        Assertions.assertEquals("REGLE001", result.getCoderegle(), "Code should match");
        verify(regleManuelleRepository, times(1)).save(any(RegleManuelle.class));
    }

    @Test
    public void testUpdateRegleManuelle_Success() {
        int id = 1;
        RegleManuelleDTO regleManuelleDTO = new RegleManuelleDTO();
        regleManuelleDTO.setCoderegle("UPDATED_CODE");

        RegleManuelle existingRegle = new RegleManuelle();
        existingRegle.setId(id);

        when(regleManuelleRepository.existsById(id)).thenReturn(true);
        when(regleManuelleRepository.save(any(RegleManuelle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RegleManuelleDTO result = regleManuelleService.updateRegleManuelle(id, regleManuelleDTO);

        Assertions.assertNotNull(result, "Manual rule must not be null");
        Assertions.assertEquals("UPDATED_CODE", result.getCoderegle(), "Code should match updated value");
        verify(regleManuelleRepository, times(1)).existsById(id);
        verify(regleManuelleRepository, times(1)).save(any(RegleManuelle.class));
    }

    @Test
    public void testDeleteRegleManuelle_Success() {
        int id = 1;

        when(regleManuelleRepository.existsById(id)).thenReturn(true);

        regleManuelleService.deleteRegleManuelle(id);

        verify(regleManuelleRepository, times(1)).existsById(id);
        verify(regleManuelleRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetRegleManuelle_Success() {
        int id = 1;
        RegleManuelle regleManuelle = new RegleManuelle();
        regleManuelle.setId(id);
        regleManuelle.setCoderegle("REGLE001");

        when(regleManuelleRepository.findById(id)).thenReturn(Optional.of(regleManuelle));

        RegleManuelleDTO result = regleManuelleService.getRegleManuelle(id);

        Assertions.assertNotNull(result, "Manual rule must not be null");
        Assertions.assertEquals("REGLE001", result.getCoderegle(), "Code should match");
        verify(regleManuelleRepository, times(1)).findById(id);
    }

    @Test
    public void testGetRegleManuelles_Success() {
        RegleManuelle regle1 = new RegleManuelle();
        regle1.setCoderegle("REGLE001");

        RegleManuelle regle2 = new RegleManuelle();
        regle2.setCoderegle("REGLE002");

        when(regleManuelleRepository.findAll()).thenReturn(List.of(regle1, regle2));

        List<RegleManuelleDTO> result = regleManuelleService.getRegleManuelles();

        Assertions.assertEquals(2, result.size(), "There should be 2 rules");
        verify(regleManuelleRepository, times(1)).findAll();
    }
}
*/
