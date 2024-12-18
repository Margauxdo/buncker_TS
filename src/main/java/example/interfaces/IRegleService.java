package example.interfaces;

import example.DTO.RegleDTO;
import example.entity.Regle;

import java.util.List;

public interface IRegleService {
    Regle createRegle(RegleDTO regleDTO);

    RegleDTO readRegle(int id);

    List<RegleDTO> readAllRegles();

    RegleDTO updateRegle(int id, RegleDTO regleDTO);
    RegleDTO getRegleById(int id);
    List<RegleDTO> getAllRegles();
    void deleteRegle(int id);
}
