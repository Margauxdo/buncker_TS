package example.interfaces;

import example.DTO.ClientDTO;

import java.util.List;

public interface IClientService {

    ClientDTO createClient(ClientDTO clientDTO);

    ClientDTO updateClient(Integer id, ClientDTO clientDTO);

    void deleteClient(int id);

    ClientDTO getClientById(int id);

    List<ClientDTO> getAllClients();

    boolean existsById(int id);
}


