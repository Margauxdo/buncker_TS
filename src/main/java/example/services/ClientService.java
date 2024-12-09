package example.services;

import example.DTO.ClientDTO;
import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.interfaces.IClientService;
import example.repositories.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Méthode pour convertir un Client en ClientDTO
    private ClientDTO convertToDto(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setName(client.getName());
        clientDTO.setAdresse(client.getAdresse());
        clientDTO.setEmail(client.getEmail());
        clientDTO.setTelephoneExploitation(client.getTelephoneExploitation());
        clientDTO.setVille(client.getVille());
        clientDTO.setPersonnelEtFonction(client.getPersonnelEtFonction());
        clientDTO.setRamassage1(client.getRamassage1());
        clientDTO.setRamassage2(client.getRamassage2());
        clientDTO.setRamassage3(client.getRamassage3());
        clientDTO.setRamassage4(client.getRamassage4());
        clientDTO.setRamassage5(client.getRamassage5());
        clientDTO.setRamassage6(client.getRamassage6());
        clientDTO.setRamassage7(client.getRamassage7());
        clientDTO.setEnvoiparDefaut(client.getEnvoiparDefaut());
        clientDTO.setMemoRetourSecurite1(client.getMemoRetourSecurite1());
        clientDTO.setMemoRetourSecurite2(client.getMemoRetourSecurite2());
        clientDTO.setTypeSuivie(client.getTypeSuivie());
        clientDTO.setCodeClient(client.getCodeClient());
        clientDTO.setRetourSecuriteId(client.getRetourSecurite() != null ? client.getRetourSecurite().getId() : null);
        clientDTO.setRegleId(client.getRegle() != null ? client.getRegle().getId() : null);

        // Mapper les valises
        if (client.getValises() != null) {
            client.getValises().forEach(valise -> {
                clientDTO.getValiseIds().add(valise.getId());
                clientDTO.getValisesDescriptions().add(valise.getDescription());
            });
        }

        return clientDTO;
    }

    // Méthode pour convertir un ClientDTO en Client
    private Client convertToEntity(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setName(clientDTO.getName());
        client.setAdresse(clientDTO.getAdresse());
        client.setEmail(clientDTO.getEmail());
        client.setTelephoneExploitation(clientDTO.getTelephoneExploitation());
        client.setVille(clientDTO.getVille());
        client.setPersonnelEtFonction(clientDTO.getPersonnelEtFonction());
        client.setRamassage1(clientDTO.getRamassage1());
        client.setRamassage2(clientDTO.getRamassage2());
        client.setRamassage3(clientDTO.getRamassage3());
        client.setRamassage4(clientDTO.getRamassage4());
        client.setRamassage5(clientDTO.getRamassage5());
        client.setRamassage6(clientDTO.getRamassage6());
        client.setRamassage7(clientDTO.getRamassage7());
        client.setEnvoiparDefaut(clientDTO.getEnvoiparDefaut());
        client.setMemoRetourSecurite1(clientDTO.getMemoRetourSecurite1());
        client.setMemoRetourSecurite2(clientDTO.getMemoRetourSecurite2());
        client.setTypeSuivie(clientDTO.getTypeSuivie());
        client.setCodeClient(clientDTO.getCodeClient());
        return client;
    }

    @Override
    public void createClient(ClientDTO clientDTO) {
        Client client = convertToEntity(clientDTO);
        clientRepository.save(client);
    }

    @Override
    @Transactional
    public void updateClient(Integer id, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));
        Client updatedClient = convertToEntity(clientDTO);
        updatedClient.setId(existingClient.getId()); // Conservez l'ID
        clientRepository.save(updatedClient);
    }

    @Override
    @Transactional
    public void deleteClient(int id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));
        clientRepository.delete(client);
    }

    @Transactional
    @Override
    public ClientDTO getClientById(int id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));
        Hibernate.initialize(client.getValises());
        Hibernate.initialize(client.getProblemes());
        return convertToDto(client);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::convertToDto) // Utilisation correcte de convertToDto
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(int id) {
        return clientRepository.existsById(id);
    }
}
