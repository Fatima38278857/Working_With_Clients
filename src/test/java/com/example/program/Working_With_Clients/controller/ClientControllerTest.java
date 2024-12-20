package com.example.program.Working_With_Clients.controller;

import com.example.program.Working_With_Clients.dto.ClientDTO;
import com.example.program.Working_With_Clients.entity.Client;
import com.example.program.Working_With_Clients.mapper.ClientMapper;
import com.example.program.Working_With_Clients.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.http.MediaType;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientController clientController;

    private ObjectMapper objectMapper;

    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
        objectMapper = new ObjectMapper();

        client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setName("John Doe");
        clientDTO.setEmail("john.doe@example.com");
    }

    @Test
    void getAllClients_shouldReturnClientList() throws Exception {
        when(clientService.findAllClients()).thenReturn(Collections.singletonList(client));
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        mockMvc.perform(get("/api/clients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));

        verify(clientService, times(1)).findAllClients();
        verify(clientMapper, times(1)).toDTO(client);
    }

    @Test
    void createClient_shouldReturnCreatedClient() throws Exception {
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);
        when(clientService.saveClient(client)).thenReturn(client);
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        mockMvc.perform(post("/api/clients/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(clientService, times(1)).saveClient(client);
        verify(clientMapper, times(1)).toDTO(client);
    }

    @Test
    void updateClient_shouldReturnUpdatedClient() throws Exception {
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);
        when(clientService.updateClient(eq(1L), any(Client.class))).thenReturn(client);
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        mockMvc.perform(put("/api/clients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(clientService, times(1)).updateClient(eq(1L), any(Client.class));
        verify(clientMapper, times(1)).toDTO(client);
    }

    @Test
    void deleteClient_shouldReturnNoContent() throws Exception {
        doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/api/clients/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClient(1L);
    }

}

