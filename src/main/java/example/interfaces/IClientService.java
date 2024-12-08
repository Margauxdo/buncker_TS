package example.interfaces;

import example.entity.Client;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IClientService {
    Client createClient(Client client);
    Client updateClient(int id, Client client);

    @Transactional
    Client updateClient(Integer id, Client client);

    void deleteClient(int id);
    Client getClientById(int id);
    List<Client> getAllClients();
    boolean existsById(int id);


}
