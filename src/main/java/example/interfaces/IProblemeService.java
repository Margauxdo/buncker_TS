package example.interfaces;

import example.DTO.ProblemeDTO;
import example.entity.Probleme;

import java.util.List;

public interface IProblemeService {
    Probleme createProbleme(Probleme probleme);

    Probleme updateProbleme(int id, Probleme probleme);

    ProblemeDTO createProbleme(ProblemeDTO problemeDTO);
    ProblemeDTO updateProbleme(int id, ProblemeDTO problemeDTO);
    void deleteProbleme(int id);

    Probleme getProbleme(int id);

    ProblemeDTO getProblemeById(int id);
    List<ProblemeDTO> getAllProblemes();

    boolean existsByDescriptionAndDetails(String description, String details);
}
