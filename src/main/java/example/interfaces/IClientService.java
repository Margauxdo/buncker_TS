package example.interfaces;

import example.DTO.ClientDTO;
import example.entity.Client;

import java.util.List;

public interface IClientService {

    void createClient(ClientDTO clientDTO);

    void updateClient(Integer id, ClientDTO clientDTO);

    void deleteClient(int id);

    ClientDTO getClientById(int id);

    List<ClientDTO> getAllClients();

    boolean existsById(int id);
}


