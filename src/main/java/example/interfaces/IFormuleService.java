package example.interfaces;


import example.entities.Formule;

import java.util.List;

public interface IFormuleService {
    Formule createFormule(Formule formule);
    Formule updateFormule(int id, Formule formule);
    Formule getFormuleById(int id);
    List<Formule> getAllFormules();
    void deleteFormule(int id);
}
