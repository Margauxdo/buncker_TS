package example.interfaces;

import example.DTO.ValiseDTO;

import java.util.List;

public interface IValiseService {
    ValiseDTO createValise(ValiseDTO valiseDTO);
    ValiseDTO updateValise(int id, ValiseDTO valiseDTO);
    void deleteValise(int id);
    ValiseDTO getValiseById(int id);
    List<ValiseDTO> getAllValises();
}
