package example.interfaces;

import example.entities.Probleme;

import java.util.List;

public interface IProblemeService {
    Probleme createProbleme(Probleme probleme);
    Probleme updateProbleme(int id, Probleme probleme);
    void deleteProbleme(int id);
    Probleme getProbleme(int id);
    List<Probleme> getAllProblemes();
}
