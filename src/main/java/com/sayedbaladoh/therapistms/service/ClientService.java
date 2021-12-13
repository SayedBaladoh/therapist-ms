package com.sayedbaladoh.therapistms.service;

import java.util.List;
import java.util.UUID;

import com.sayedbaladoh.therapistms.dto.ClientRequestDto;
import com.sayedbaladoh.therapistms.dto.ClientResponseDto;
import com.sayedbaladoh.therapistms.dto.ClientUpdateRequestDto;

public interface ClientService {

	ClientResponseDto addClient(UUID therapistId, ClientRequestDto client);

	List<ClientResponseDto> getAllClients(UUID therapistId);

	ClientResponseDto getClient(UUID therapistId, UUID clientId);

	ClientResponseDto updateClient(UUID therapistId, UUID clientId, ClientUpdateRequestDto clientDto);

	void removeClient(UUID therapistId, UUID clientId);

	void removeAllClients(UUID therapistId);
}
