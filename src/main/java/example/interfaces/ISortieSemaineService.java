package example.interfaces;

import example.entity.SortieSemaine;

import java.util.List;

public interface ISortieSemaineService {

    SortieSemaine createSortieSemaine(SortieSemaine semaine);
    SortieSemaine updateSortieSemaine(int id, SortieSemaine semaine);
    void deleteSortieSemaine(int id);
    SortieSemaine getSortieSemaine(int id);
    List<SortieSemaine> getAllSortieSemaine();


    boolean existsById(int id);
}
