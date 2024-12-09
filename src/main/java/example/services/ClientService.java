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
    private ClientDTO convertToDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getName(),
                client.getAdresse(),
                client.getEmail(),
                client.getTelephoneExploitation(),
                client.getVille(),
                client.getPersonnelEtFonction(),
                client.getRamassage1(),
                client.getRamassage2(),
                client.getRamassage3(),
                client.getRamassage4(),
                client.getRamassage5(),
                client.getRamassage6(),
                client.getRamassage7(),
                client.getEnvoiparDefaut(),
                client.getMemoRetourSecurite1(),
                client.getMemoRetourSecurite2(),
                client.getTypeSuivie(),
                client.getCodeClient(),
                client.getValises() != null ? client.getValises().stream().map(Valise::getId).collect(Collectors.toList()) : List.of(),
                client.getProblemes() != null ? client.getProblemes().stream().map(Probleme::getId).collect(Collectors.toList()) : List.of(),
                client.getRetourSecurite() != null ? client.getRetourSecurite().getId() : null,
                client.getRegle() != null ? client.getRegle().getId() : null
        );
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

    @Override
    @Transactional
    public ClientDTO getClientById(int id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));
        Hibernate.initialize(client.getValises());
        return convertToDTO(client);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(client -> new ClientDTO(
                        client.getId(),
                        client.getName(),
                        client.getAdresse(),
                        client.getEmail(),
                        client.getTelephoneExploitation(),
                        client.getVille(),
                        client.getPersonnelEtFonction(),
                        client.getRamassage1(),
                        client.getRamassage2(),
                        client.getRamassage3(),
                        client.getRamassage4(),
                        client.getRamassage5(),
                        client.getRamassage6(),
                        client.getRamassage7(),
                        client.getEnvoiparDefaut(),
                        client.getMemoRetourSecurite1(),
                        client.getMemoRetourSecurite2(),
                        client.getTypeSuivie(),
                        client.getCodeClient(),
                        null, // Ne pas charger les relations complexes
                        null,
                        null,
                        null
                ))
                .collect(Collectors.toList());
    }


    @Override
    public boolean existsById(int id) {
        return clientRepository.existsById(id);
    }
}
