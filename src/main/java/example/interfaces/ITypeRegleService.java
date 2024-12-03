package example.interfaces;

import example.entity.TypeRegle;

import java.util.List;
import java.util.Optional;

public interface ITypeRegleService {
    TypeRegle createTypeRegle(String nomTypeRegle, Long regleId);

    TypeRegle createTypeRegle(TypeRegle typeRegle);
   TypeRegle updateTypeRegle(int id, TypeRegle typeRegle);
   void deleteTypeRegle(int id);
   Optional<TypeRegle> getTypeRegle(int id);
   List<TypeRegle> getTypeRegles();

}
