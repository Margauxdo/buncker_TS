package example.services;

import example.DTO.ClientDTO;
import example.entity.*;
import example.interfaces.IClientService;
import example.repositories.ClientRepository;
import example.repositories.RegleRepository;
import example.repositories.RetourSecuriteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final RetourSecuriteRepository retourSecuriteRepository;
    private final RegleRepository regleRepository;

    public ClientService(ClientRepository clientRepository, RetourSecuriteRepository retourSecuriteRepository, RegleRepository regleRepository) {
        this.clientRepository = clientRepository;
        this.retourSecuriteRepository = retourSecuriteRepository;
        this.regleRepository = regleRepository;
    }

    // Méthode pour convertir un Client en ClientDTO
    public ClientDTO convertToDTO(Client client) {
        if (client == null) {
            return null;
        }
        return ClientDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .adresse(client.getAdresse())
                .email(client.getEmail())
                .telephoneExploitation(client.getTelephoneExploitation())
                .ville(client.getVille())
                .personnelEtFonction(client.getPersonnelEtFonction())
                .ramassage1(client.getRamassage1())
                .ramassage2(client.getRamassage2())
                .ramassage3(client.getRamassage3())
                .ramassage4(client.getRamassage4())
                .ramassage5(client.getRamassage5())
                .ramassage6(client.getRamassage6())
                .ramassage7(client.getRamassage7())
                .envoiparDefaut(client.getEnvoiparDefaut())
                .memoRetourSecurite1(client.getMemoRetourSecurite1())
                .memoRetourSecurite2(client.getMemoRetourSecurite2())
                .typeSuivie(client.getTypeSuivie())
                .codeClient(client.getCodeClient())
                .build();
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

        // Ajouter RetourSecurite et Regle
        if (clientDTO.getRetourSecuriteId() != null) {
            RetourSecurite retourSecurite = retourSecuriteRepository.findById(clientDTO.getRetourSecuriteId())
                    .orElseThrow(() -> new EntityNotFoundException("RetourSecurite non trouvé"));
            client.setRetourSecurite(retourSecurite);
        }

        if (clientDTO.getRegleId() != null) {
            Regle regle = regleRepository.findById(clientDTO.getRegleId())
                    .orElseThrow(() -> new EntityNotFoundException("Règle non trouvée"));
            client.setRegle(regle);
        }

        return client;
    }

    @Override
    public void createClient(ClientDTO clientDTO) {
        if (clientRepository.existsByEmail(clientDTO.getEmail())) {
            throw new IllegalArgumentException("L'email existe déjà : " + clientDTO.getEmail());
        }
        Client client = convertToEntity(clientDTO);
        clientRepository.save(client);
    }

    @Override
    @Transactional
    public void updateClient(Integer id, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));
        Client updatedClient = convertToEntity(clientDTO);
        updatedClient.setId(existingClient.getId());
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

        // Initialiser explicitement les collections
        Hibernate.initialize(client.getValises());
        Hibernate.initialize(client.getProblemes());

        return convertToDTO(client); // Use the correct method name
    }



    @Transactional
    @Override
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        clients.forEach(client -> {
            Hibernate.initialize(client.getValises());
            Hibernate.initialize(client.getProblemes());
        });
        return clients.stream().map(this::convertToDTO).collect(Collectors.toList()); // Use the correct method name
    }

    @Override
    public boolean existsById(int id) {
        return clientRepository.existsById(id);
    }


}
