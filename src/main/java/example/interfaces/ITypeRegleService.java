package example.interfaces;

import example.DTO.TypeRegleDTO;

import java.util.List;

public interface ITypeRegleService {
    TypeRegleDTO createTypeRegle(TypeRegleDTO typeRegleDTO);
    TypeRegleDTO updateTypeRegle(int id, TypeRegleDTO typeRegleDTO);
    void deleteTypeRegle(int id);
    TypeRegleDTO getTypeRegle(int id);
    List<TypeRegleDTO> getTypeRegles();
}
