package com.example.program.Working_With_Clients.mapper;

import com.example.program.Working_With_Clients.dto.ClientDTO;
import com.example.program.Working_With_Clients.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientMapper  INSTANCE = Mappers.getMapper(ClientMapper.class);

    // Конвертация Client -> ClientDTO
    ClientDTO toDTO(Client client);

    // Конвертация ClientDTO -> Client
    @Mapping(target = "id", ignore = true)
    Client toEntity(ClientDTO clientDTO);
}
