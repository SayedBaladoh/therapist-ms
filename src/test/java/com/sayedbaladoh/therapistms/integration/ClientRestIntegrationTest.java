package com.sayedbaladoh.therapistms.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sayedbaladoh.therapistms.TherapistApplication;
import com.sayedbaladoh.therapistms.dto.ClientRequestDto;
import com.sayedbaladoh.therapistms.dto.ClientResponseDto;
import com.sayedbaladoh.therapistms.dto.ClientUpdateRequestDto;
import com.sayedbaladoh.therapistms.model.Client;
import com.sayedbaladoh.therapistms.repository.ClientRepository;
import com.sayedbaladoh.therapistms.util.JsonUtil;

/**
 * Client APIs Integration tests
 * 
 * Test the Client rest APIs integration tests
 * 
 * @author Sayed Baladoh
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TherapistApplication.class})
@AutoConfigureMockMvc
public class ClientRestIntegrationTest {

	private final String API_URL = "/therapists/{therapistId}/clients";
	private final UUID INVALID_ID = UUID.randomUUID();
	
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ClientRepository clientRepository;

	@AfterEach
	public void cleanUp() {
		clientRepository.deleteAll();
	}

	/**
	 * Validate all clients details for a valid therapist Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#getAllClients(UUID)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenClientsAndValidTherapistId_whenGetAllClients_thenReturnClientsWithStatus200()
			throws Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client1 = createClient(therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");
		Client client2 = createClient(therapistId, "Mariam", "mariam@test.com", "female", "+2012345987");
		Client client3 = createClient(therapistId, "Mohamed", "mohamed@test.com", "male", "+0212345879");

		// API call and Verification
		MvcResult mvcResult = mvc.perform(get(API_URL, therapistId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))				
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id").exists())
				.andExpect(jsonPath("$[1].id").exists())
				.andExpect(jsonPath("$[2].id").exists())
				.andExpect(jsonPath("$[0].id").isNotEmpty())
				.andExpect(jsonPath("$[1].id").isNotEmpty())
				.andExpect(jsonPath("$[2].id").isNotEmpty())
				.andReturn();
		
		assertEquals("application/json", mvcResult.getResponse().getContentType());
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
	public void givenInvalidTherapistId_whenGetAllClients_thenReturn404NotFound()
			throws Exception {		
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client1 = createClient(therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");
	
		// API call and Verification
		MvcResult mvcResult = mvc.perform(get(API_URL, UUID.randomUUID())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();		
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
		Client client = createClient(therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");
		
		//API call and Verification
		MvcResult mvcResult = mvc.perform(get(API_URL+"/{clientId}", therapistId, client.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.therapistId").exists())
				.andExpect(jsonPath("$.id").value(client.getId().toString()))
				.andExpect(jsonPath("$.name").value(client.getName()))
				.andExpect(jsonPath("$.email").value(client.getEmail()))
				.andExpect(jsonPath("$.therapistId", is(client.getTherapistId().toString())))
				.andDo(print())
				.andReturn();

		assertEquals("application/json", mvcResult.getResponse().getContentType());
		 ClientResponseDto clientResponseDto =
		            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ClientResponseDto.class);
		 
		 assertClient(clientResponseDto, client);				 
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
		
		// API call and Verification
		mvc.perform(get(API_URL+"/{clientId}", UUID.randomUUID(), INVALID_ID)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
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
	public void givenValidClientRequestDto_whenAddClient_thenClientAdded() throws IOException, Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		ClientRequestDto clientRequestDto = mockClientRequestDto("Ahmed", "ahmed@test.com", "male", "+2012345789");
		
		// API call and Verification
		MvcResult mvcResult = mvc.perform(post(API_URL, therapistId)				
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(clientRequestDto)))
				.andExpect(status().isCreated())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.therapistId").exists())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.name").value(clientRequestDto.getName()))
				.andExpect(jsonPath("$.email", is(clientRequestDto.getEmail())))
				.andExpect(jsonPath("$.therapistId", is(therapistId.toString())))
				.andReturn();
		
		assertEquals("application/json", mvcResult.getResponse().getContentType());
		ClientResponseDto clientResponseDto =
		            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ClientResponseDto.class);
		 
		 assertClient(clientResponseDto, clientRequestDto);	
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
	public void givenInvalidClientRequestDto_whenAddClient_thenReturnBadRequest() throws IOException, Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		ClientRequestDto clientRequestDto = mockClientRequestDto("Ahmed", "ahmed.com", "male", "+2012345789");

		// API call and Verification
		mvc.perform(post(API_URL, therapistId)				
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(clientRequestDto)))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.id").doesNotExist());
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
	public void givenValidClientRequest_whenUpdateClient_thenClientUpdated() throws IOException, Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client = createClient(therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");
	
		client.setName("Mohamed");
		client.setEmail("mohamed@test.com");

		// Method call and Verification
		mvc.perform(put(API_URL + "/{clientId}", therapistId, client.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(client)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.therapistId").exists())
				.andExpect(jsonPath("$.id").value(client.getId().toString()))
				.andExpect(jsonPath("$.name").value(client.getName()))
				.andExpect(jsonPath("$.email", is(client.getEmail())));
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
	public void givenInvalidClientId_whenUpdateClient_thenReturn404NotFound() throws IOException, Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client = createClient(therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");

		client.setName("Mohamed");
		client.setEmail("mohamed@test.com");

		// Method call and Verification
		mvc.perform(put(API_URL + "/{clientId}", therapistId,  INVALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(client)))
				.andExpect(status().isNotFound());
	}
	
	/**
	 * Verify delete client with valid id and therapist Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#deleteClient(UUID, UUID)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenValidId_whenDeleteClient_thenClientDeleted() throws IOException, Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client = createClient(therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");

		// Method call and Verification
		mvc.perform(delete(API_URL + "/{clientId}", therapistId, client.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(client)))
				.andExpect(status().isOk());
	}

	/**
	 * Verify delete client with invalid Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.ClientController#deleteClient(UUID, UUID)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenInvalidClientId_whenDeleteClient_thenReturn404NotFound() throws IOException, Exception {
		// Data preparation
		UUID therapistId = UUID.randomUUID();
		Client client = createClient(therapistId, "Ahmed", "ahmed@test.com", "male", "+2012345789");

		// Method call and Verification
		mvc.perform(delete(API_URL + "/{clientId}", therapistId,  INVALID_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(client)))
				.andExpect(status().isNotFound());
	}
	
	/**
	 * Save Client
	 * 
	 * @param therapistId
	 * @param name
	 * @param email
	 * @param gender
	 * @param phoneNumber
	 * @return
	 */
	private Client createClient(UUID therapistId, String name, String email, String gender, String phoneNumber) {

		Client client = new Client();
		client.setTherapistId(therapistId);
		client.setName(name);
		client.setEmail(email);
		client.setGender(gender);
		client.setPhoneNumber(phoneNumber);

		return clientRepository.save(client.getTherapistId(), client);
	}
	
	private ClientRequestDto mockClientRequestDto(String name, String email, String gender, String phoneNumber) {

		ClientRequestDto clientRequestDto = new ClientRequestDto();
		clientRequestDto.setName(name);
		clientRequestDto.setEmail(email);
		clientRequestDto.setGender(gender);
		clientRequestDto.setPhoneNumber(phoneNumber);
		
		return clientRequestDto;
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
	
	private void assertClient(ClientResponseDto clientResponseDto, ClientRequestDto clientRequestDto) {
		assertNotNull(clientResponseDto);
		assertNotNull(clientResponseDto.getId());
		assertNotNull(clientResponseDto.getName());
	    assertEquals(clientResponseDto.getName(), clientRequestDto.getName());
	    assertNotNull(clientResponseDto.getEmail());
		assertEquals(clientResponseDto.getEmail(), clientRequestDto.getEmail());
		assertNotNull(clientResponseDto.getTherapistId());
	  }
}
