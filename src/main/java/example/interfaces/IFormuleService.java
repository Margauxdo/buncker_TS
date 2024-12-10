
package example.interfaces;

import example.DTO.FormuleDTO;

import java.util.List;

public interface IFormuleService {
    FormuleDTO createFormule(FormuleDTO formuleDTO);

    FormuleDTO updateFormule(int id, FormuleDTO formuleDTO);

    void deleteFormule(int id);

    FormuleDTO getFormuleById(int id);

    List<FormuleDTO> getAllFormules();


}
