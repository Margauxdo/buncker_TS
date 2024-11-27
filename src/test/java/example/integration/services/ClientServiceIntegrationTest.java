package example.integration.services;

import example.entity.Client;
import example.entity.Probleme;
import example.entity.Valise;
import example.repositories.ValiseRepository;
import example.services.ClientService;
import example.repositories.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class ClientServiceIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;
    @Autowired
    private ValiseRepository valiseRepository;

    @BeforeEach
    public void setUp() {
        clientRepository.deleteAll();

        client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .adresse("123 Main St")
                .telephoneExploitation("555-1234")
                .ville("Springfield")
                .personnelEtFonction("Manager")
                .build();
    }


    @Test
    public void testCreateClientSuccess() {
        // Act
        Client savedClient = clientService.createClient(client);

        // Assert
        assertNotNull(savedClient);
        assertNotNull(savedClient.getId());
        assertEquals("John Doe", savedClient.getName());
        assertEquals("john.doe@example.com", savedClient.getEmail());
        assertEquals("123 Main St", savedClient.getAdresse());
        assertEquals("555-1234", savedClient.getTelephoneExploitation());
        assertEquals("Springfield", savedClient.getVille());
        assertEquals("Manager", savedClient.getPersonnelEtFonction());
    }
    @Test
    public void testUpdateClientSuccess() {
        // Arrange: Save the client first
        Client savedClient = clientService.createClient(client);

        // Act
        savedClient.setName("Jane Doe");
        savedClient.setEmail("jane.doe@example.com");
        Client updatedClient = clientService.updateClient(savedClient.getId(), savedClient);

        // Assert
        assertNotNull(updatedClient);
        assertEquals("Jane Doe", updatedClient.getName());
        assertEquals("jane.doe@example.com", updatedClient.getEmail());
    }

    @Test
    public void testDeleteClientSuccess() {
        // Arrange
        Client savedClient = clientService.createClient(client);

        // Act & Assert
        clientService.deleteClient(savedClient.getId());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.getClientById(savedClient.getId());
        });

        assertEquals("Client not found with ID " + savedClient.getId(), exception.getMessage());
    }

    @Test
    public void testGetClientByIdSuccess() {
        // Arrange
        Client savedClient = clientService.createClient(client);

        // Act
        Client foundClient = clientService.getClientById(savedClient.getId());

        // Assert
        assertNotNull(foundClient);
        assertEquals(savedClient.getId(), foundClient.getId());
        assertEquals("John Doe", foundClient.getName());
    }

    @Test
    public void testGetAllClientsSuccess() {
        // Arrange
        Client client2 = Client.builder()
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .adresse("456 Main St")
                .telephoneExploitation("555-5678")
                .ville("Springfield")
                .personnelEtFonction("Supervisor")
                .build();

        clientService.createClient(client);
        clientService.createClient(client2);

        // Act
        List<Client> clients = clientService.getAllClients();

        // Assert
        assertNotNull(clients);
        assertEquals(2, clients.size(), "Expected only 2 clients in the repository");
    }




    @Test
    public void testCreateClient_Failure_DuplicateEmail() {
        // Arrange
        clientService.createClient(client);

        Client duplicateClient = Client.builder()
                .name("Jane Doe")
                .email("john.doe@example.com") // Même email que 'client'
                .adresse("456 Main St")
                .telephoneExploitation("555-5678")
                .ville("Springfield")
                .personnelEtFonction("Supervisor")
                .build();

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            clientService.createClient(duplicateClient);
        }, "Une DataIntegrityViolationException devrait être levée pour un email en double");
    }



    @Test
    public void testDeleteClient_WithRelations() {
        // Arrange
        Valise valise = Valise.builder()
                .description("Valise B")
                .build();

        client.addValise(valise);
        Client savedClient = clientService.createClient(client);

        // Act
        clientService.deleteClient(savedClient.getId());

        // Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.getClientById(savedClient.getId());
        });

        assertEquals("Client not found with ID " + savedClient.getId(), exception.getMessage());

        // Vérifier que les valises associées sont également supprimées si CascadeType.REMOVE est configuré
        assertTrue(valiseRepository.findAll().isEmpty(), "Les valises associées devraient être supprimées");
    }

    @Test
    public void testUpdateClient_Failure_IdMismatch() {
        // Arrange
        Client savedClient = clientService.createClient(client);

        // Act & Assert
        Client updatedClient = Client.builder()
                .id(savedClient.getId() + 1) // ID différent
                .name("New Name")
                .email("new.email@example.com")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.updateClient(savedClient.getId(), updatedClient);
        });

        assertEquals("Client ID mismatch", exception.getMessage());
    }

    @Test
    public void testDeleteClient_Failure_NonExistent() {
        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.deleteClient(9999); // ID inexistant
        });

        assertEquals("Client not found with ID 9999", exception.getMessage());
    }





}






