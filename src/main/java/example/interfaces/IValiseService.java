package example.interfaces;

import example.DTO.ValiseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IValiseService {
    ValiseDTO createValise(ValiseDTO valiseDTO);
    ValiseDTO updateValise(Integer id, ValiseDTO valiseDTO);
    void deleteValise(Integer id);

    @Transactional
    ValiseDTO updateValise(int id, ValiseDTO valiseDTO);

    ValiseDTO getValiseById(Integer id);
    List<ValiseDTO> getAllValises();

    @Transactional
    void deleteValise(int id);
}
