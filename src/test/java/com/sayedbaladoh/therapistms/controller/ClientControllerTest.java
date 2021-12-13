package com.sayedbaladoh.therapistms.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sayedbaladoh.therapistms.dto.ClientRequestDto;
import com.sayedbaladoh.therapistms.dto.ClientResponseDto;
import com.sayedbaladoh.therapistms.dto.ClientUpdateRequestDto;
import com.sayedbaladoh.therapistms.exception.ResourceNotFoundException;
import com.sayedbaladoh.therapistms.model.Client;
import com.sayedbaladoh.therapistms.service.ClientService;
import com.sayedbaladoh.therapistms.util.JsonUtil;

/**
 * Therapist controller unit tests
 * 
 * Test the Therapist rest APIs unit tests
 * 
 * @author Sayed Baladoh
 */
@WebMvcTest(value = ClientController.class)
class ClientControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ClientService clientService;
	
	private final ObjectMapper objectMapper = new ObjectMapper();

	@AfterEach
	void tearDown() throws Exception {
		reset(clientService);
	}

	/**
	 * Validate get all clients details for a valid therapist Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#getAllClients(UUID)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenClientsList_whenGetAllClients_thenReturnClientsResponseDtoList() throws Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client1 = mockClient(UUID.randomUUID(), therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");
		Client client2 = mockClient(UUID.randomUUID(), therapistId, "Mariam", "mariam@test.com", "female", "+2012345987");
		Client client3 = mockClient(UUID.randomUUID(), therapistId, "Mohamed", "mohamed@test.com", "male", "+0212345879");

		List<Client> clients = Arrays.asList(client1, client2, client3);	
		List<ClientResponseDto> mockedClientsResponseDto = clients
				.stream()
				.map(this::mockClientResponseDto)
				.collect(Collectors.toList());
		
		given(clientService.getAllClients(therapistId))
				.willReturn(mockedClientsResponseDto);

		//API call and Verification
		MvcResult mvcResult = mvc.perform(get("/therapists/{therapistId}/clients", therapistId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", is(client1.getId().toString())))
				.andExpect(jsonPath("$[1].id", is(client2.getId().toString())))
				.andExpect(jsonPath("$[2].id", is(client3.getId().toString())))
				.andExpect(jsonPath("$[0].name", is(client1.getName())))
				.andExpect(jsonPath("$[1].name", is(client2.getName())))
				.andExpect(jsonPath("$[2].name", is(client3.getName())))
				.andReturn();
		
		assertEquals("application/json", mvcResult.getResponse().getContentType());
		verify(clientService, times(1)).getAllClients(therapistId);
		Mockito.verifyNoMoreInteractions(clientService);
	}
	
	/**
	 * Validate get all clients for an invalid therapist Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#getAllClients(UUID)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenInvalidTherapistId_whenGetAllClients_thenReturn404NotFound() throws Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
	
		given(clientService.getAllClients(therapistId))
				.willThrow(new ResourceNotFoundException());

		//API call and Verification		
		MvcResult mvcResult = mvc.perform(get("/therapists/{therapistId}/clients", therapistId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();
		
		assertEquals("application/json", mvcResult.getResponse().getContentType());
		verify(clientService, times(1)).getAllClients(therapistId);
		Mockito.verifyNoMoreInteractions(clientService);
	}
	
	/**
	 * Verify get client with valid Id and therapist Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#getClient(UUID, UUID)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenClient_whenGetClient_thenReturnClientResponse() throws Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client = mockClient(UUID.randomUUID(), therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");

		given(clientService.getClient(therapistId, client.getId()))
				.willReturn(mockClientResponseDto(client));

		//API call and Verification
		MvcResult mvcResult = mvc.perform(get("/therapists/{therapistId}/clients/{clientId}" , therapistId, client.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.therapistId").exists())
				.andExpect(jsonPath("$.id").value(client.getId().toString()))
				.andExpect(jsonPath("$.name").value(client.getName()))
				.andExpect(jsonPath("$.email", is(client.getEmail())))
				.andExpect(jsonPath("$.therapistId", is(client.getTherapistId().toString())))
				.andDo(print())
				.andReturn();

		assertEquals("application/json", mvcResult.getResponse().getContentType());
		ClientResponseDto clientResponseDto =
		            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ClientResponseDto.class);
		 
		 assertClient(clientResponseDto, client);
		
		verify(clientService, times(1)).getClient(therapistId, client.getId());
		Mockito.verifyNoMoreInteractions(clientService);
	}
	
	/**
	 * Verify get client with invalid Id and therapist Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#getClient(UUID, UUID)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenInavlidClientId_whenGetClient_thenReturn404NotFound() throws Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		UUID invalidClientId = UUID.randomUUID();

		given(clientService.getClient(therapistId, invalidClientId))
				.willThrow(new ResourceNotFoundException());

		// Verification
		this.mvc.perform(get("/therapists/{therapistId}/clients/{clientId}", therapistId, invalidClientId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andDo(print());
		verify(clientService, times(1)).getClient(therapistId, invalidClientId);
		Mockito.verifyNoMoreInteractions(clientService);
	}
	
	/**
	 * Verify add a valid Client to an therapist client’s list
	 *
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#addClient(UUID, ClientRequestDto)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenValidClientRequestDto_whenAddClient_thenClientAdded() throws Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client = mockClient(UUID.randomUUID(), therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");
		ClientRequestDto clientRequest = mockClientRequestDto("Ahmed", "ahmed@test.com", "male", "+2012345789");

		given(clientService.addClient(eq(therapistId), any(ClientRequestDto.class)))
				.willReturn(mockClientResponseDto(client));

		// API call and Verification
		this.mvc.perform(post("/therapists/{therapistId}/clients",therapistId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(clientRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.therapistId").exists())
				.andExpect(jsonPath("$.id").value(client.getId().toString()))
				.andExpect(jsonPath("$.name").value(client.getName()))
				.andExpect(jsonPath("$.email", is(client.getEmail())))
				.andExpect(jsonPath("$.therapistId", is(client.getTherapistId().toString())));

		verify(clientService, times(1)).addClient(eq(therapistId), any(ClientRequestDto.class));
		Mockito.verifyNoMoreInteractions(clientService);
	}

	/**
	 * Verify add an invalid Client to an therapist client’s list
	 *
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#addClient(UUID, ClientRequestDto)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenInvalidClientRequestDto_whenAddClient_thenReturnBadRequest() throws Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		ClientRequestDto clientRequest = mockClientRequestDto("Ahmed", "ahmed.com", "male", "+2012345789");

		// API call and Verification
		mvc.perform(post("/therapists/{therapistId}/clients",therapistId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(clientRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").exists())
				.andExpect(jsonPath("$.errors[0].code").value("email"))
				.andExpect(jsonPath("$.errors[0].message").value("must be a well-formed email address"));

		Mockito.verifyNoInteractions(clientService);
	}
	
	/**
	 * Verify update valid client
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#updateClient(UUID, UUID, ClientUpdateRequestDto)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenValidClientRequest_whenUpdateClient_thenClientUpdated() throws Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client = mockClient(UUID.randomUUID(), therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");

		ClientUpdateRequestDto clientUpdateRequest = new ClientUpdateRequestDto();
		clientUpdateRequest.setName("Ahmed Mohamed");
		
		client.setName(clientUpdateRequest.getName());

		given(clientService.updateClient(eq(therapistId), eq(client.getId()), any(ClientUpdateRequestDto.class)))
				.willReturn(mockClientResponseDto(client));

		//API call and Verification
		mvc.perform(put("/therapists/{therapistId}/clients/{clientId}", therapistId, client.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(clientUpdateRequest)))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.therapistId").exists())
				.andExpect(jsonPath("$.id").value(client.getId().toString()))
				.andExpect(jsonPath("$.name").value(client.getName()))
				.andExpect(jsonPath("$.email", is(client.getEmail())))
				.andExpect(jsonPath("$.therapistId", is(client.getTherapistId().toString())))
				.andDo(print());
		
		verify(clientService, times(1)).updateClient(eq(therapistId), eq(client.getId()), any(ClientUpdateRequestDto.class));
		Mockito.verifyNoMoreInteractions(clientService);
	}

	/**
	 * Verify update client with invalid Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#updateClient(UUID, UUID, ClientUpdateRequestDto)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenInvalidClientId_whenUpdateClient_thenReturn404NotFound() throws Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		UUID invalidClientId = UUID.randomUUID();		
		ClientUpdateRequestDto clientUpdateRequest = new ClientUpdateRequestDto();
		clientUpdateRequest.setName("Ahmed Mohamed");
		
		given(clientService.updateClient(eq(therapistId), eq(invalidClientId), any(ClientUpdateRequestDto.class)))
		.willThrow(new ResourceNotFoundException());
		
		//API call and Verification
		mvc.perform(put("/therapists/{therapistId}/clients/{clientId}", therapistId, invalidClientId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(clientUpdateRequest)))
				.andExpect(status().isNotFound())
				.andDo(print());
		
		verify(clientService, times(1)).updateClient(eq(therapistId), eq(invalidClientId), any(ClientUpdateRequestDto.class));
		Mockito.verifyNoMoreInteractions(clientService);
	}
	
	/**
	 * Verify delete client with valid Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#deleteClient(UUID, UUID)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenValidClientId_whenDeleteClient_thenClientUpdated() throws Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client = mockClient(UUID.randomUUID(), therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");

	    doNothing().when(clientService).removeClient(therapistId, client.getId());

		//API call and Verification
		mvc.perform(delete("/therapists/{therapistId}/clients/{clientId}", therapistId, client.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200))
				.andDo(print());
		
		verify(clientService, times(1)).removeClient(therapistId, client.getId());
		Mockito.verifyNoMoreInteractions(clientService);
	}

	/**
	 * Verify delete client with invalid Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#updateClient(UUID, UUID, ClientUpdateRequestDto)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenInvalidClientId_whendeleteClient_thenReturn404NotFound() throws Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		UUID invalidClientId = UUID.randomUUID();		
		ClientUpdateRequestDto clientUpdateRequest = new ClientUpdateRequestDto();
		clientUpdateRequest.setName("Ahmed Mohamed");
		
		doThrow(new ResourceNotFoundException()).when(clientService).removeClient(therapistId, invalidClientId);
		
		//API call and Verification
		mvc.perform(delete("/therapists/{therapistId}/clients/{clientId}", therapistId, invalidClientId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(clientUpdateRequest)))
				.andExpect(status().isNotFound())
				.andDo(print());
		
		verify(clientService, times(1)).removeClient(therapistId, invalidClientId);
		Mockito.verifyNoMoreInteractions(clientService);
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

	private ClientRequestDto mockClientRequestDto(String name, String email, String gender, String phoneNumber) {

		ClientRequestDto clientRequestDto = new ClientRequestDto();
		clientRequestDto.setName(name);
		clientRequestDto.setEmail(email);
		clientRequestDto.setGender(gender);
		clientRequestDto.setPhoneNumber(phoneNumber);
		
		return clientRequestDto;
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

	private void assertClient(ClientResponseDto clientDto, Client client) {
		assertNotNull(clientDto);
		assertNotNull(clientDto.getId());
		assertEquals(clientDto.getId(), client.getId());
		assertNotNull(clientDto.getName());
		assertEquals(clientDto.getName(), client.getName());
		assertNotNull(clientDto.getEmail());
		assertEquals(clientDto.getEmail(), client.getEmail());
		assertNotNull(clientDto.getTherapistId());
		assertEquals(clientDto.getTherapistId(), client.getTherapistId());
	}

}
