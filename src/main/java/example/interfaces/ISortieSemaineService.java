package example.interfaces;

import example.DTO.SortieSemaineDTO;
import example.entity.SortieSemaine;

import java.util.List;

public interface ISortieSemaineService {

    SortieSemaineDTO createSortieSemaine(SortieSemaineDTO sortieSemaineDTO);
    SortieSemaineDTO updateSortieSemaine(int id, SortieSemaine sortieSemaineDTO);

    SortieSemaineDTO updateSortieSemaine(int id, SortieSemaineDTO sortieSemaineDTO);

    void deleteSortieSemaine(int id);
    SortieSemaineDTO getSortieSemaine(int id);
    List<SortieSemaineDTO> getAllSortieSemaine();
}
