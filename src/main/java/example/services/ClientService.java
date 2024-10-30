package example.services;

import example.entities.Client;
import example.interfaces.IClientService;
import example.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService implements IClientService {

@Autowired
private ClientRepository clientRepository;

public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
}

    @Override
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(int id, Client client) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found");
        }

        if (client.getId() != id) {
            throw new RuntimeException("Expression not valid");
        }

        client.setId(id);
        return clientRepository.save(client);
    }






    @Override
    public void deleteClient(int id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Client getClientById(int id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    @Override
    public boolean existsById(int id) {
        return clientRepository.existsById(id);
    }
}
