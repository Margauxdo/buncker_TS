package templates.typeRegles.controller;

import example.DTO.RetourSecuriteDTO;
import example.controller.RetourSecuriteController;
import example.interfaces.IRetourSecuriteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RetourSecuriteControllerTest {

    @InjectMocks
    private RetourSecuriteController RScontroller;

    @Mock
    private IRetourSecuriteService retourSecuriteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


}
