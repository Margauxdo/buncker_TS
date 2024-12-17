package example.interfaces;

import example.DTO.RegleDTO;
import example.entity.RegleManuelle;

import java.util.List;

public interface IRegleService {
    RegleDTO createRegle(RegleDTO regleDTO);

    RegleDTO readRegle(int id);

    List<RegleDTO> readAllRegles();

    RegleDTO updateRegle(int id, RegleDTO regleDTO);
    RegleDTO getRegleById(int id);
    List<RegleDTO> getAllRegles();
    void deleteRegle(int id);

    RegleDTO saveRegleManuelle(RegleManuelle regleManuelle);
}
