package example.interfaces;

import example.entity.TypeRegle;

import java.util.List;

public interface ITypeRegleService {
   TypeRegle createTypeRegle(TypeRegle typeRegle);
   TypeRegle updateTypeRegle(int id, TypeRegle typeRegle);
   void deleteTypeRegle(int id);
   TypeRegle getTypeRegle(int id);
   List<TypeRegle> getTypeRegles();

}
