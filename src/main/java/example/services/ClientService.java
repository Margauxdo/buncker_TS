
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
            // Initialiser les collections pour éviter les erreurs null lors de la manipulation
            if (client.getValises() == null) {
                client.setValises(List.of());
            }
            if (client.getProblemes() == null) {
                client.setProblemes(List.of());
            }
            return clientRepository.save(client);
        }

        @Override
        @Transactional
        public Client updateClient(int id, Client client) {
            if (!clientRepository.existsById(id)) {
                throw new EntityNotFoundException("Client not found with ID " + id);
            }

            if (client.getId() != null && client.getId() != id) {
                throw new IllegalArgumentException("Client ID mismatch");
            }

            Client existingClient = clientRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException("Client not found with ID " + id));

            // Mettre à jour uniquement les champs nécessaires
            existingClient.setName(client.getName());
            existingClient.setAdresse(client.getAdresse());
            existingClient.setEmail(client.getEmail());
            existingClient.setTelephoneExploitation(client.getTelephoneExploitation());
            existingClient.setVille(client.getVille());
            existingClient.setPersonnelEtFonction(client.getPersonnelEtFonction());
            existingClient.setRamassage1(client.getRamassage1());
            existingClient.setRamassage2(client.getRamassage2());
            existingClient.setRamassage3(client.getRamassage3());
            existingClient.setRamassage4(client.getRamassage4());
            existingClient.setRamassage5(client.getRamassage5());
            existingClient.setRamassage6(client.getRamassage6());
            existingClient.setRamassage7(client.getRamassage7());
            existingClient.setEnvoiparDefaut(client.getEnvoiparDefaut());
            existingClient.setMemoRetourSecurite1(client.getMemoRetourSecurite1());
            existingClient.setMemoRetourSecurite2(client.getMemoRetourSecurite2());
            existingClient.setTypeSuivie(client.getTypeSuivie());
            existingClient.setCodeClient(client.getCodeClient());

            // Relations
            existingClient.setValises(client.getValises());
            existingClient.setProblemes(client.getProblemes());
            existingClient.setRetourSecurite(client.getRetourSecurite());
            existingClient.setRegle(client.getRegle());

            return clientRepository.save(existingClient);
        }

        @Override
        @Transactional
        public void deleteClient(int id) {
            Client client = clientRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException("Client not found with ID " + id));

            // Supprimer les entités liées manuellement si nécessaire
            if (client.getValises() != null) {
                client.getValises().clear();
            }
            if (client.getProblemes() != null) {
                client.getProblemes().clear();
            }

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



