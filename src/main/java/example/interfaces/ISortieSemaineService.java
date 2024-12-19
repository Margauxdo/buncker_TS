package example.interfaces;

import example.DTO.SortieSemaineDTO;
import example.entity.SortieSemaine;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ISortieSemaineService {
    SortieSemaineDTO createSortieSemaine(SortieSemaineDTO sortieSemaineDTO);
    SortieSemaineDTO updateSortieSemaine(Integer id, SortieSemaineDTO sortieSemaineDTO);

    @Transactional
    SortieSemaineDTO updateSortieSemaine(int id, SortieSemaineDTO sortieSemaineDTO);

    void deleteSortieSemaine(Integer id);
;

    SortieSemaineDTO getSortieSemaine(Integer id);


    SortieSemaine findById(Long id);

    List<SortieSemaineDTO> getAllSortieSemaine();
}

