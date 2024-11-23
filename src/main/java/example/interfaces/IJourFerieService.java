package example.interfaces;

import example.entity.JourFerie;

import java.util.List;

public interface IJourFerieService {
    JourFerie getJourFerie(int id);
    List<JourFerie> getJourFeries();
    /*JourFerie saveJourFerie(JourFerie jourFerie);*/



}
