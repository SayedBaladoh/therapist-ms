package com.sayedbaladoh.therapistms.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
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
import com.sayedbaladoh.therapistms.dto.TherapistRequestDto;
import com.sayedbaladoh.therapistms.dto.TherapistResponseDto;
import com.sayedbaladoh.therapistms.dto.TherapistUpdateRequestDto;
import com.sayedbaladoh.therapistms.exception.ResourceNotFoundException;
import com.sayedbaladoh.therapistms.model.Therapist;
import com.sayedbaladoh.therapistms.service.TherapistService;
import com.sayedbaladoh.therapistms.util.JsonUtil;

/**
 * Therapist controller unit tests
 * 
 * Test the Therapist rest APIs unit tests
 * 
 * @author Sayed Baladoh
 */
@WebMvcTest(value = TherapistController.class)
class TherapistControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TherapistService therapistService;
	
	private final ObjectMapper objectMapper = new ObjectMapper();

	@AfterEach
	void tearDown() throws Exception {
		reset(therapistService);
	}

	/**
	 * Validate get all therapists with list of therapists
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.TherapistController#getAllTherapists()}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenTherapistsList_whenGetAllTherapists_thenReturnTherapistList() throws Exception {
		// Data preparation
		UUID practiceId1 = UUID.randomUUID();
		UUID practiceId2 = UUID.randomUUID();
		Therapist therapist1 = mockTherapist(UUID.randomUUID(), "Ahmed", "ahmed@test.com", practiceId1);
		Therapist therapist2 = mockTherapist(UUID.randomUUID(), "Mohamed", "mohamed@test.com", practiceId2);
		Therapist therapist3 = mockTherapist(UUID.randomUUID(), "Ali", "ali@test.com", practiceId1);

		List<Therapist> therapists = Arrays.asList(therapist1, therapist2, therapist3);	
		List<TherapistResponseDto> mockedTherapistsResponseDto = therapists
				.stream()
				.map(this::mockTherapistResponseDto)
				.collect(Collectors.toList());
		
		given(therapistService.getAllTherapists())
				.willReturn(mockedTherapistsResponseDto);

		//API call and Verification
		MvcResult mvcResult = mvc.perform(get("/therapists")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", is(therapist1.getId().toString())))
				.andExpect(jsonPath("$[1].id", is(therapist2.getId().toString())))
				.andExpect(jsonPath("$[2].id", is(therapist3.getId().toString())))
				.andExpect(jsonPath("$[0].name", is(therapist1.getName())))
				.andExpect(jsonPath("$[1].name", is(therapist2.getName())))
				.andExpect(jsonPath("$[2].name", is(therapist3.getName())))
				.andReturn();
		
		assertEquals("application/json", mvcResult.getResponse().getContentType());
		verify(therapistService, times(1)).getAllTherapists();
		Mockito.verifyNoMoreInteractions(therapistService);
	}
	
	/**
	 * Validate get all therapists with empty list
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.TherapistController#getAllTherapists()}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenEmptyTherapistsList_whenGetAllTherapists_thenReturnEmptyList() throws Exception {
		// Data preparation
		List<TherapistResponseDto> mockedTherapistsResponseDto = Collections.emptyList();
		
		given(therapistService.getAllTherapists())
				.willReturn(mockedTherapistsResponseDto);

		//API call and Verification		
		MvcResult mvcResult = mvc.perform(get("/therapists")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
				.andReturn();
		
		assertEquals("application/json", mvcResult.getResponse().getContentType());
		verify(therapistService, times(1)).getAllTherapists();
		Mockito.verifyNoMoreInteractions(therapistService);
	}
	
	/**
	 * Verify get therapist with valid Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.TherapistController#getTherapist(UUID)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenTherapist_whenGetTherapist_thenReturnTherapistResponse() throws Exception {
		// Data preparation
		UUID practiceId = UUID.randomUUID();
		Therapist therapist = mockTherapist(UUID.randomUUID(), "Ahmed", "ahmed@test.com", practiceId);

		given(therapistService.getTherapist(therapist.getId()))
				.willReturn(mockTherapistResponseDto(therapist));

		//API call and Verification
		MvcResult mvcResult = mvc.perform(get("/therapists/{therapistId}" , therapist.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.practiceId").exists())
				.andExpect(jsonPath("$.id").value(therapist.getId().toString()))
				.andExpect(jsonPath("$.name").value(therapist.getName()))
				.andExpect(jsonPath("$.email", is(therapist.getEmail())))
				.andExpect(jsonPath("$.practiceId", is(therapist.getPracticeId().toString())))
				.andDo(print())
				.andReturn();

		assertEquals("application/json", mvcResult.getResponse().getContentType());
		TherapistResponseDto therapistResponseDto =
		            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TherapistResponseDto.class);
		 
		 assertTherapist(therapistResponseDto, therapist);
		
		verify(therapistService, times(1)).getTherapist(therapist.getId());
		Mockito.verifyNoMoreInteractions(therapistService);
	}
	
	/**
	 * Verify get therapist with invalid Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.TherapistController#getTherapist(UUID)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenTherapist_whenGetTherapistWithInavlidTherapistId_thenReturn404NotFound() throws Exception {
		// Data preparation
		UUID invalidTherapistId = UUID.randomUUID();

		given(therapistService.getTherapist(invalidTherapistId))
				.willThrow(new ResourceNotFoundException());

		// Verification
		this.mvc.perform(get("/therapists/{therapistId}", invalidTherapistId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andDo(print());
		verify(therapistService, times(1)).getTherapist(invalidTherapistId);
		Mockito.verifyNoMoreInteractions(therapistService);
	}
	
	/**
	 * Verify add a valid Therapist
	 *
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.TherapistController#addTherapist(com.sayedbaladoh.therapistms.dto.TherapistRequestDto)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenValidTherapistRequestDto_whenAddTherapist_thenTherapistAdded() throws Exception {
		// Data preparation
		UUID practiceId = UUID.randomUUID();
		Therapist therapist = mockTherapist(UUID.randomUUID(), "Ahmed", "ahmed@test.com", practiceId);
		TherapistRequestDto therapistRequest = mockTherapistRequestDto("Ahmed", "ahmed@test.com", practiceId);

		given(therapistService.addTherapist(any(TherapistRequestDto.class)))
				.willReturn(mockTherapistResponseDto(therapist));

		// API call and Verification
		mvc.perform(
				post("/therapists").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(therapistRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.practiceId").exists())
				.andExpect(jsonPath("$.id").value(therapist.getId().toString()))
				.andExpect(jsonPath("$.name").value(therapist.getName()))
				.andExpect(jsonPath("$.email", is(therapist.getEmail())))
				.andExpect(jsonPath("$.practiceId", is(therapist.getPracticeId().toString())));

		verify(therapistService, times(1)).addTherapist(any(TherapistRequestDto.class));
		Mockito.verifyNoMoreInteractions(therapistService);
	}

	/**
	 * Verify add an invalid Therapist
	 *
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.TherapistController#addTherapist(com.sayedbaladoh.therapistms.dto.TherapistRequestDto)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenInvalidTherapistRequestDto_whenAddTherapist_thenReturnBadRequest() throws Exception {
		// Data preparation
		UUID practiceId = UUID.randomUUID();
		TherapistRequestDto therapistRequest = mockTherapistRequestDto("Ahmed", "ahmed", practiceId);

		// API call and Verification
		mvc.perform(
				post("/therapists").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(therapistRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").exists())
				.andExpect(jsonPath("$.errors[0].code").value("email"))
				.andExpect(jsonPath("$.errors[0].message").value("must be a well-formed email address"));

		Mockito.verifyNoInteractions(therapistService);
	}
	
	/**
	 * Verify update valid therapist
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.TherapistController#updateTherapist(UUID, com.sayedbaladoh.therapistms.dto.TherapistUpdateRequestDto)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenValidTherapistRequest_whenUpdateTherapist_thenTherapistUpdated() throws Exception {
		// Data preparation
		UUID practiceId = UUID.randomUUID();
		Therapist therapist = mockTherapist(UUID.randomUUID(), "Ahmed", "ahmed@test.com", practiceId);
		TherapistUpdateRequestDto therapistRequest = new TherapistUpdateRequestDto(practiceId, "Mohamed", "ahmed@test.com");
		therapist.setName(therapistRequest.getName());

		given(therapistService.updateTherapist(eq(therapist.getId()), any(TherapistUpdateRequestDto.class)))
				.willReturn(mockTherapistResponseDto(therapist));

		//API call and Verification
		mvc.perform(put("/therapists/{therapistId}", therapist.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(therapistRequest)))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.practiceId").exists())
				.andExpect(jsonPath("$.id").value(therapist.getId().toString()))
				.andExpect(jsonPath("$.name").value(therapist.getName()))
				.andExpect(jsonPath("$.email", is(therapist.getEmail())))
				.andExpect(jsonPath("$.practiceId", is(therapist.getPracticeId().toString())))
				.andDo(print());
		
		verify(therapistService, times(1)).updateTherapist(eq(therapist.getId()), any(TherapistUpdateRequestDto.class));
		Mockito.verifyNoMoreInteractions(therapistService);
	}

	/**
	 * Verify update therapist with invalid Id
	 * 
	 * Test method for
	 * {@link com.sayedbaladoh.therapistms.controller.TherapistController#updateTherapist(UUID, com.sayedbaladoh.therapistms.dto.TherapistUpdateRequestDto)}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenInvalidTherapistId_whenUpdateTherapist_thenReturn404NotFound() throws Exception {
		// Data preparation
		UUID invalidTherapistId = UUID.randomUUID();
		
		UUID practiceId = UUID.randomUUID();
		TherapistUpdateRequestDto therapistRequest = new TherapistUpdateRequestDto(practiceId, "Mohamed", "ahmed@test.com");
		
		given(therapistService.updateTherapist(eq(invalidTherapistId), any(TherapistUpdateRequestDto.class)))
		.willThrow(new ResourceNotFoundException());
		
		//API call and Verification
		mvc.perform(put("/therapists/{therapistId}", invalidTherapistId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(therapistRequest)))
				.andExpect(status().isNotFound())
				.andDo(print());
		
		verify(therapistService, times(1)).updateTherapist(eq(invalidTherapistId), any(TherapistUpdateRequestDto.class));
		Mockito.verifyNoMoreInteractions(therapistService);
	}
	
	private Therapist mockTherapist(UUID id, String name, String email, UUID practiceId) {

		Therapist therapist = new Therapist();
		therapist.setId(id);
		therapist.setName(name);
		therapist.setEmail(email);
		therapist.setPracticeId(practiceId);

		return therapist;
	}

	private TherapistRequestDto mockTherapistRequestDto(String name, String email, UUID practiceId) {

		return new TherapistRequestDto(practiceId, name, email);
	}

	private TherapistResponseDto mockTherapistResponseDto(Therapist therapist) {

		TherapistResponseDto therapistResponse = new TherapistResponseDto();
		therapistResponse.setId(therapist.getId());
		therapistResponse.setName(therapist.getName());
		therapistResponse.setEmail(therapist.getEmail());
		therapistResponse.setPracticeId(therapist.getPracticeId());
		return therapistResponse;
	}

	private void assertTherapist(TherapistResponseDto therapistDto, Therapist therapist) {
		assertNotNull(therapistDto);
		assertNotNull(therapistDto.getId());
		assertEquals(therapistDto.getId(), therapist.getId());
		assertNotNull(therapistDto.getName());
		assertEquals(therapistDto.getName(), therapist.getName());
		assertNotNull(therapistDto.getEmail());
		assertEquals(therapistDto.getEmail(), therapist.getEmail());
		assertNotNull(therapistDto.getPracticeId());
		assertEquals(therapistDto.getPracticeId(), therapist.getPracticeId());
	}

}
