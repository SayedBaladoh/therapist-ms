package com.sayedbaladoh.therapistms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sayedbaladoh.therapistms.dto.ClientResponseDto;
import com.sayedbaladoh.therapistms.exception.ResourceNotFoundException;
import com.sayedbaladoh.therapistms.model.Client;
import com.sayedbaladoh.therapistms.repository.ClientRepository;
import com.sayedbaladoh.therapistms.util.ObjectMapperHelper;

/**
 * Client service unit tests
 * 
 * Test the Client service logic
 * 
 * @author Sayed Baladoh
 *
 */
@ExtendWith(SpringExtension.class)
public class ClientServiceImplTest {

	@Mock
	private ClientRepository clientRepository;
	@Mock
	private ObjectMapperHelper objectMapperHelper;
	@InjectMocks
	private ClientServiceImpl clientService;

	@AfterEach
	public void tearDown() {
		Mockito.reset(clientRepository);
		Mockito.reset(objectMapperHelper);
	}

	/**
	 * Validate get all clients
	 */
	@Test
	public void givenClientsList_whenGetAllClients_thenReturnClientsResponseDtoList() {

		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client1 = mockClient(UUID.randomUUID(), therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");
		Client client2 = mockClient(UUID.randomUUID(), therapistId, "Mariam", "mariam@test.com", "female",
				"+2012345987");
		Client client3 = mockClient(UUID.randomUUID(), therapistId, "Mohamed", "mohamed@test.com", "male",
				"+0212345879");

		List<Client> clients = Arrays.asList(client1, client2, client3);
		List<ClientResponseDto> mockedClientsResponseDto = clients.stream().map(this::mockClientResponseDto)
				.collect(Collectors.toList());
		Map<UUID, Client> clientsMap = clients.stream()
			      .collect(Collectors.toMap(Client::getId, Function.identity()));
		
		given(clientRepository.findById(therapistId)).willReturn(Optional.ofNullable(clientsMap));

		given(objectMapperHelper.mapAll(clientsMap.values(), ClientResponseDto.class))
				.willReturn(mockedClientsResponseDto);

		// Method call
		List<ClientResponseDto> clientsList = clientService.getAllClients(therapistId);

		// Verification
		assertThat(clientsList).isNotNull();
		assertThat(clientsList).hasSize(3).extracting(ClientResponseDto::getName).contains(clients.get(0).getName(),
				clients.get(1).getName(), clients.get(2).getName());

		Mockito.verify(clientRepository, Mockito.times(1)).findById(therapistId);
		Mockito.verifyNoMoreInteractions(clientRepository);
		Mockito.verify(objectMapperHelper, Mockito.times(1)).mapAll(clientsMap.values(), ClientResponseDto.class);
		Mockito.verifyNoMoreInteractions(objectMapperHelper);
	}

	@Test
	public void givenInvalidTherapistId_whenGetAllClients_thenThrowResourceNotFoundException() {
		// Data preparation
		UUID therapistId = UUID.randomUUID();

		given(clientRepository.findById(therapistId)).willThrow(new ResourceNotFoundException(String.format("No clients found with therapist id: %s.", therapistId.toString())));

		// Method call and verification
		ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
			clientService.getAllClients(therapistId);
		});
		assertTrue(thrown.getMessage()
				.contains(String.format("No clients found with therapist id: %s.", therapistId.toString())));

		Mockito.verify(clientRepository, Mockito.times(1)).findById(therapistId);
		Mockito.verifyNoMoreInteractions(clientRepository);
		Mockito.verifyNoInteractions(objectMapperHelper);
	}

	private Client mockClient(UUID id, UUID therapistId, String name, String email, String gender, String phoneNumber) {

		Client client = new Client();
		client.setId(id);
		client.setTherapistId(therapistId);
		client.setName(name);
		client.setEmail(email);
		client.setGender(gender);
		client.setPhoneNumber(phoneNumber);

		return client;
	}
	
	private ClientResponseDto mockClientResponseDto(Client client) {

		ClientResponseDto clientResponse = new ClientResponseDto();
		clientResponse.setId(client.getId());
		clientResponse.setName(client.getName());
		clientResponse.setEmail(client.getEmail());
		clientResponse.setTherapistId(client.getTherapistId());
		clientResponse.setGender(client.getGender());
		clientResponse.setPhoneNumber(client.getPhoneNumber());

		return clientResponse;
	}
}
