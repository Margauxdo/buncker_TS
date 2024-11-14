package example.interfaces;

import example.entities.TypeValise;
import example.entities.Valise;

import java.util.List;

public interface ITypeValiseService {
    TypeValise createTypeValise(TypeValise typeValise);
    TypeValise updateTypeValise(int id, TypeValise typeValise);
    void deleteTypeValise(int id);
    TypeValise getTypeValise(int id);
    List<TypeValise> getTypeValises();



}
