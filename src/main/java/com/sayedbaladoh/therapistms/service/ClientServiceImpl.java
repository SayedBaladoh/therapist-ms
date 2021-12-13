package com.sayedbaladoh.therapistms.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sayedbaladoh.therapistms.dto.ClientRequestDto;
import com.sayedbaladoh.therapistms.dto.ClientResponseDto;
import com.sayedbaladoh.therapistms.dto.ClientUpdateRequestDto;
import com.sayedbaladoh.therapistms.exception.ResourceNotFoundException;
import com.sayedbaladoh.therapistms.model.Client;
import com.sayedbaladoh.therapistms.repository.ClientRepository;
import com.sayedbaladoh.therapistms.util.ObjectMapperHelper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
	private final ObjectMapperHelper objectMapperHelper;

	@Override
	public ClientResponseDto addClient(UUID therapistId, ClientRequestDto clientDto) {

		Client client = objectMapperHelper.map(clientDto, Client.class);
		client.setTherapistId(therapistId);

		return objectMapperHelper.map(clientRepository.save(therapistId, client), ClientResponseDto.class);
	}

	@Override
	public List<ClientResponseDto> getAllClients(UUID therapistId) {

		Collection<Client> clients = getAllByTherapistId(therapistId).values();
		return objectMapperHelper.mapAll(clients, ClientResponseDto.class);
	}

	@Override
	public ClientResponseDto getClient(UUID therapistId, UUID clientId) {

		Map<UUID, Client> clients = getAllByTherapistId(therapistId);
		Client client = get(clients, clientId);
		return objectMapperHelper.map(client, ClientResponseDto.class);
	}

	@Override
	public ClientResponseDto updateClient(UUID therapistId, UUID clientId, ClientUpdateRequestDto clientDto) {

		Map<UUID, Client> clients = getAllByTherapistId(therapistId);
		Client client = get(clients, clientId);
		client = objectMapperHelper.map(clientDto, client);

		clients.put(clientId, client);
		clientRepository.save(therapistId, clients);

		return objectMapperHelper.map(client, ClientResponseDto.class);
	}

	@Override
	public void removeClient(UUID therapistId, UUID clientId) {

		Map<UUID, Client> clients = getAllByTherapistId(therapistId);
		get(clients, clientId);
		clients.remove(clientId);
		clientRepository.save(therapistId, clients);
	}

	@Override
	public void removeAllClients(UUID therapistId) {

		if (clientRepository.existsById(therapistId))
			clientRepository.deleteById(therapistId);
		else
			throw new ResourceNotFoundException(
					String.format("No client found with therapist id: %s.", therapistId.toString()));
	}

	private Map<UUID, Client> getAllByTherapistId(UUID therapistId) {
		return clientRepository.findById(therapistId).orElseThrow(() -> new ResourceNotFoundException(
				String.format("No clients found with therapist id: %s.", therapistId.toString())));
	}

	private Client get(Map<UUID, Client> clients, UUID clientId) {
		Client client = Optional.ofNullable(clients.get(clientId)).orElseThrow(() -> new ResourceNotFoundException(
				String.format("No client found with id: %s.", clientId.toString())));
		return client;
	}
}