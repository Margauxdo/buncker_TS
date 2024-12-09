package example.services;

import example.DTO.ValiseDTO;
import example.entity.Client;
import example.entity.Valise;
import example.exceptions.ResourceNotFoundException;
import example.interfaces.IValiseService;
import example.repositories.ClientRepository;
import example.repositories.ValiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValiseService implements IValiseService {

    @Autowired
    private ValiseRepository valiseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ValiseDTO createValise(ValiseDTO valiseDTO) {
        Client client = clientRepository.findById(valiseDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        Valise valise = Valise.builder()
                .description(valiseDTO.getDescription())
                .numeroValise(valiseDTO.getNumeroValise())
                .client(client)
                .build();
        valise = valiseRepository.save(valise);
        return mapToDTO(valise);
    }

    @Override
    public ValiseDTO updateValise(int id, ValiseDTO valiseDTO) {
        Valise valise = valiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Valise not found"));
        valise.setDescription(valiseDTO.getDescription());
        valise.setNumeroValise(valiseDTO.getNumeroValise());
        valiseRepository.save(valise);
        return mapToDTO(valise);
    }

    @Override
    public void deleteValise(int id) {
        if (!valiseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Valise not found");
        }
        valiseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ValiseDTO getValiseById(int id) {
        Valise valise = valiseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Valise not found"));
        return mapToDTO(valise);
    }

    @Override
    @Transactional
    public List<ValiseDTO> getAllValises() {
        return valiseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ValiseDTO mapToDTO(Valise valise) {
        return ValiseDTO.builder()
                .id(valise.getId())
                .description(valise.getDescription())
                .numeroValise(valise.getNumeroValise())
                .clientId(valise.getClient() != null ? valise.getClient().getId() : null)
                .build();
    }
}
