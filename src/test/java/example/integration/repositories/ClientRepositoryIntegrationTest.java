package example.integration.repositories;

import example.entity.Client;
import example.entity.Valise;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("integrationtest")
@Transactional
public class ClientRepositoryIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ValiseRepository valiseRepository;

    @BeforeEach
    public void setUp() {
        valiseRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testSaveClientSuccess() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        Client savedClient = clientRepository.save(client);

        assertNotNull(savedClient.getId());
        assertEquals("John Doe", savedClient.getName());
        assertEquals("john.doe@example.com", savedClient.getEmail());
    }



    @Test
    public void testSaveClientWithValise() {
        // Arrange
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        Valise valise = new Valise();
        valise.setNumeroValise("VAL123");
        valise.setDescription("Description de la valise"); // Ajouter une description pour respecter la contrainte
        valise.setClient(client);

        client.setValises(List.of(valise));

        // Act
        Client savedClient = clientRepository.save(client);

        // Assert
        Optional<Client> foundClient = clientRepository.findById(savedClient.getId());
        assertTrue(foundClient.isPresent());
        assertEquals(1, foundClient.get().getValises().size());
        assertEquals("VAL123", foundClient.get().getValises().get(0).getNumeroValise());
        assertEquals("Description de la valise", foundClient.get().getValises().get(0).getDescription());
    }


    @Test
    public void testDeleteClientWithValises() {
        // Arrange
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        Valise valise = new Valise();
        valise.setNumeroValise("VAL789");
        valise.setDescription("Valise à supprimer");
        valise.setClient(client);

        client.setValises(List.of(valise));
        client = clientRepository.save(client);

        // Act
        clientRepository.deleteById(client.getId());
        Optional<Client> foundClient = clientRepository.findById(client.getId());

        // Assert
        assertFalse(foundClient.isPresent());
        List<Valise> valises = valiseRepository.findAll();
        assertTrue(valises.isEmpty()); // Vérifie que les valises liées ont été supprimées
    }

    @Test
    public void testUpdateClientDetails() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        Client savedClient = clientRepository.save(client);

        savedClient.setName("John Updated");
        savedClient.setEmail("john.updated@example.com");
        clientRepository.save(savedClient);

        Optional<Client> updatedClient = clientRepository.findById(savedClient.getId());
        assertTrue(updatedClient.isPresent());
        assertEquals("John Updated", updatedClient.get().getName());
        assertEquals("john.updated@example.com", updatedClient.get().getEmail());
    }
}
