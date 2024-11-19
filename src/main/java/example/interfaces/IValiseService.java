package example.interfaces;

import example.entity.Valise;

import java.util.List;

public interface IValiseService {
    Valise createValise(Valise valise);
    Valise updateValise(int id,Valise valise);
    void deleteValise(int id);
    Valise getValiseById(int id);
    List<Valise> getAllValises();
}
