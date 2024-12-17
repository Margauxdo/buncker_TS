package example.integration.services;

import example.DTO.ClientDTO;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import example.services.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("integrationtest")
public class ClientServiceIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    private ClientDTO clientDTO;

   /* @BeforeEach
    public void setUp() {
        clientRepository.deleteAll();

        clientDTO = new ClientDTO(
                null,
                "John Doe",
                "123 Main St",
                "john.doe@example.com",
                "555-1234",
                "Springfield",
                "Manager",
                "Ramassage 1", "Ramassage 2", null, null, null, null, null,
                "Standard",
                "Memo 1", "Memo 2",
                "Type 1",
                "C001",
                List.of(), // valiseIds
                List.of(), // problemeIds
                null, // retourSecuriteId
                null  // regleId
        );
    }*/

    @Test
    public void testCreateClientSuccess() {
        // Act
        clientService.createClient(clientDTO);

        // Assert
        List<ClientDTO> clients = clientService.getAllClients();
        assertEquals(1, clients.size());
        ClientDTO savedClient = clients.get(0);
        assertEquals("John Doe", savedClient.getName());
        assertEquals("john.doe@example.com", savedClient.getEmail());
    }

    @Test
    public void testUpdateClientSuccess() {
        // Arrange
        clientService.createClient(clientDTO);
        List<ClientDTO> clients = clientService.getAllClients();
        ClientDTO savedClient = clients.get(0);

        // Act
        savedClient.setName("Jane Doe");
        savedClient.setEmail("jane.doe@example.com");
        clientService.updateClient(savedClient.getId(), savedClient);

        // Assert
        ClientDTO updatedClient = clientService.getClientById(savedClient.getId());
        assertEquals("Jane Doe", updatedClient.getName());
        assertEquals("jane.doe@example.com", updatedClient.getEmail());
    }

    @Test
    public void testDeleteClientSuccess() {
        // Arrange
        clientService.createClient(clientDTO);
        List<ClientDTO> clients = clientService.getAllClients();
        ClientDTO savedClient = clients.get(0);

        // Act
        clientService.deleteClient(savedClient.getId());

        // Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clientService.getClientById(savedClient.getId());
        });

        assertEquals("Client non trouvé", exception.getMessage());
    }
}

   /* @Test
    public void testGetAllClientsSuccess() {
        // Arrange
        ClientDTO clientDTO2 = new ClientDTO(
                null,
                "Jane Doe",
                "456 Main St",
                "jane.doe@example.com",
                "555-5678",
                "Springfield",
                "Supervisor",
                "Ramassage 1", "Ramassage 2", null, null, null, null, null,
                "Standard",
                "Memo 1", "Memo 2",
                "Type 1",
                "C002",
                List.of(), // valiseIds
                List.of(), // problemeIds
                null, // retourSecuriteId
                null  // regleId
        );

        clientService.createClient(clientDTO);
        clientService.createClient(clientDTO2);

        // Act
        List<ClientDTO> clients = clientService.getAllClients();

        // Assert
        assertEquals(2, clients.size());
    }*/

    /*@Test
    public void testCreateClient_Failure_DuplicateEmail() {
        // Arrange
        clientService.createClient(clientDTO);

        ClientDTO duplicateClientDTO = new ClientDTO(
                null,
                "Jane Doe",
                "456 Main St",
                "john.doe@example.com", // Même email que clientDTO
                "555-5678",
                "Springfield",
                "Supervisor",
                "Ramassage 1", "Ramassage 2", null, null, null, null, null,
                "Standard",
                "Memo 1", "Memo 2",
                "Type 1",
                "C002",
                List.of(),
                List.of(),
                null,
                null
        );

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            clientService.createClient(duplicateClientDTO);
        });
    }
}*/
