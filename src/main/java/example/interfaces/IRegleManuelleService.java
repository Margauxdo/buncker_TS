package example.interfaces;

import example.DTO.RegleManuelleDTO;

import java.util.List;

public interface IRegleManuelleService {
    RegleManuelleDTO createRegleManuelle(RegleManuelleDTO regleManuelleDTO);
    RegleManuelleDTO updateRegleManuelle(int id, RegleManuelleDTO regleManuelleDTO);
    void deleteRegleManuelle(int id);
    RegleManuelleDTO getRegleManuelle(int id);
    List<RegleManuelleDTO> getRegleManuelles();
}
