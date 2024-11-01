package example.interfaces;

import example.entities.JourFerie;

import java.util.List;

public interface IJourFerieService {
    JourFerie getJourFerie(int id);
    List<JourFerie> getJourFeries();
    JourFerie saveJourFerie(JourFerie jourFerie);



}
