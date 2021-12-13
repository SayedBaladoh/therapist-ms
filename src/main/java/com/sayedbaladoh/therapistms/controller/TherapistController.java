package com.sayedbaladoh.therapistms.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sayedbaladoh.therapistms.dto.TherapistRequestDto;
import com.sayedbaladoh.therapistms.dto.TherapistResponseDto;
import com.sayedbaladoh.therapistms.dto.TherapistUpdateRequestDto;
import com.sayedbaladoh.therapistms.service.TherapistService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * Therapist Controller provides APIs for <code>Therapist<code> CRUD operations.
 * 
 * @author SayedBaladoh
 */
@Api(value = "Therapists", description = "Therapist's operations APIs", tags = { "Therapists" })
@RequiredArgsConstructor
@RestController
@RequestMapping("/therapists")
public class TherapistController {

	private final TherapistService therapistService;

	/**
	 * Add a new therapist.
	 * 
	 * @param therapistDto The therapist details.
	 * @return the saved therapist.
	 */
	@ApiOperation(value = "Add a new therapist", nickname = "addTherapist", notes = "Insert a new therapist", tags = {
			"Therapists" }, response = TherapistResponseDto.class)
	@PostMapping(consumes = { "application/json" })
	public ResponseEntity<TherapistResponseDto> addTherapist(@Valid @RequestBody TherapistRequestDto therapistDto) {

		TherapistResponseDto therapist = therapistService.addTherapist(therapistDto);
		String uri = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/therapists/{id}")
				.buildAndExpand(therapist.getId()).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", uri);

		return new ResponseEntity<>(therapist, headers, HttpStatus.CREATED);
	}

	/**
	 * Get all therapists .
	 * 
	 * @return The list of therapists.
	 */
	@ApiOperation(value = "Return list of therapists", nickname = "getAllTherapists", notes = "Get a list of therapists", tags = {
			"Therapists" }, response = List.class)
	@GetMapping(produces = { "application/json" })
	public ResponseEntity<List<TherapistResponseDto>> getAllTherapists() {

		List<TherapistResponseDto> therapists = therapistService.getAllTherapists();
		return new ResponseEntity<>(therapists, HttpStatus.OK);
	}

	/**
	 * Get the therapist details by Id.
	 * 
	 * @param therapistId The therapist Id.
	 * @return The therapist details.
	 */
	@ApiOperation(value = "Return a therapist details", nickname = "getTherapist", notes = "Get a therapist details", tags = {
			"Therapists" }, response = TherapistResponseDto.class)
	@GetMapping(value = "/{therapistID}", produces = { "application/json" })
	public ResponseEntity<TherapistResponseDto> getTherapist(@PathVariable("therapistID") UUID therapistID) {

		return new ResponseEntity<>(therapistService.getTherapist(therapistID), HttpStatus.OK);
	}

	/**
	 * Update an existing therapist.
	 * 
	 * @param therapistId  The therapist Id.
	 * @param therapistDto The therapist details.
	 * @return The updated therapist.
	 */
	@ApiOperation(value = "Edit a therapist details", nickname = "updateTherapist", notes = "Update a therapist details", tags = {
			"Therapists" }, response = TherapistResponseDto.class)
	@PutMapping(value = "/{therapistID}", consumes = { "application/json" })
	public ResponseEntity<TherapistResponseDto> updateTherapist(@PathVariable("therapistID") UUID therapistID,
			@RequestBody @Valid TherapistUpdateRequestDto therapistDto) {

		return new ResponseEntity<>(therapistService.updateTherapist(therapistID, therapistDto), HttpStatus.OK);
	}

}
