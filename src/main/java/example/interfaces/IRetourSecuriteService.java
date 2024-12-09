package example.interfaces;

import example.DTO.RetourSecuriteDTO;

import java.util.List;

public interface IRetourSecuriteService {
    RetourSecuriteDTO createRetourSecurite(RetourSecuriteDTO retourSecuriteDTO);
    RetourSecuriteDTO updateRetourSecurite(int id, RetourSecuriteDTO retourSecuriteDTO);
    void deleteRetourSecurite(int id);
    RetourSecuriteDTO getRetourSecurite(int id);
    List<RetourSecuriteDTO> getAllRetourSecurites();
}
