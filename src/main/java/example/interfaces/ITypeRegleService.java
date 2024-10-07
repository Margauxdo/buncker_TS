package example.interfaces;

import example.entities.TypeRegle;
import example.entities.TypeValise;

import java.util.List;

public interface ITypeRegleService {
   TypeRegle createTypeRegle(TypeRegle typeRegle);
   TypeRegle updateTypeRegle(int id, TypeRegle typeRegle);
   void deleteTypeRegle(int id);
   TypeRegle getTypeRegle(int id);
   List<TypeRegle> getTypeRegles();

}
