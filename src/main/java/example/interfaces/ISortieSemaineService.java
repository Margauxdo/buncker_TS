package example.interfaces;

import example.DTO.SortieSemaineDTO;
import example.entity.SortieSemaine;

import java.util.List;

public interface ISortieSemaineService {
    SortieSemaineDTO createSortieSemaine(SortieSemaineDTO sortieSemaineDTO);
    SortieSemaineDTO updateSortieSemaine(int id, SortieSemaineDTO sortieSemaineDTO);
    void deleteSortieSemaine(int id);


    void deleteSortieSemaine(Integer id);

    SortieSemaineDTO getSortieSemaine(int id);

    SortieSemaine findById(Long id);

    List<SortieSemaineDTO> getAllSortieSemaine();
}

