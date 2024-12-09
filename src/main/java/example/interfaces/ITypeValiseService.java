package example.interfaces;

import example.DTO.TypeValiseDTO;

import java.util.List;

public interface ITypeValiseService {
    TypeValiseDTO createTypeValise(TypeValiseDTO typeValiseDTO);
    TypeValiseDTO updateTypeValise(int id, TypeValiseDTO typeValiseDTO);
    void deleteTypeValise(int id);
    TypeValiseDTO getTypeValise(int id);
    List<TypeValiseDTO> getTypeValises();
}
