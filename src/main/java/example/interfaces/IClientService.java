package example.interfaces;

import example.entity.Client;

import java.util.List;

public interface IClientService {
    Client createClient(Client client);
    Client updateClient(int id, Client client);
    void deleteClient(int id);
    Client getClientById(int id);
    List<Client> getAllClients();
    boolean existsById(int id);


}
