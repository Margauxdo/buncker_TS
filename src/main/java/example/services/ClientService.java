
package example.services;

import example.entity.Client;
import example.interfaces.IClientService;
import example.repositories.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            if (client.getValises() == null) {
                client.setValises(List.of());
            }
            if (client.getProblemes() == null) {
                client.setProblemes(List.of());
            }
            return clientRepository.save(client);
        }

        @Override
        public Client updateClient(int id, Client client) {
            return null;
        }

        @Override
        @Transactional
        public Client updateClient(Integer id, Client client) {
            if (id == null) {
                throw new IllegalArgumentException("ID cannot be null");
            }

            if (client.getId() != null && !client.getId().equals(id)) {
                throw new IllegalArgumentException("Client ID mismatch");
            }

            if (!clientRepository.existsById(id)) {
                throw new EntityNotFoundException("Client not found with ID " + id);
            }

            Client existingClient = clientRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException("Client not found with ID " + id));

            if (client.getName() != null) {
                existingClient.setName(client.getName());
            }
            if (client.getAdresse() != null) {
                existingClient.setAdresse(client.getAdresse());
            }
            if (client.getEmail() != null) {
                existingClient.setEmail(client.getEmail());
            }
            if (client.getTelephoneExploitation() != null) {
                existingClient.setTelephoneExploitation(client.getTelephoneExploitation());
            }
            if (client.getVille() != null) {
                existingClient.setVille(client.getVille());
            }

            return clientRepository.save(existingClient);
        }


        @Override
        @Transactional
        public void deleteClient(int id) {
            Client client = clientRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException("Client not found with ID " + id));
            clientRepository.delete(client);
        }


        @Override
        public Client getClientById(int id) {
            return clientRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException("Client not found with ID " + id));
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



