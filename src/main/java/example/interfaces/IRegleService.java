package example.interfaces;

import example.entity.Regle;

import java.util.List;

public interface IRegleService {
    Regle createRegle(Regle regle);
    Regle readRegle(int id);
    Regle updateRegle(int id,Regle regle);
    void deleteRegle(int id);

    Regle readRegleById(Long id);

    List<Regle> readAllRegles();

    boolean regleExists(String coderegle);
}
