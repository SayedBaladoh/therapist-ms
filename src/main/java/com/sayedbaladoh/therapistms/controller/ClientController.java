package com.sayedbaladoh.therapistms.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sayedbaladoh.therapistms.dto.ClientRequestDto;
import com.sayedbaladoh.therapistms.dto.ClientResponseDto;
import com.sayedbaladoh.therapistms.dto.ClientUpdateRequestDto;
import com.sayedbaladoh.therapistms.service.ClientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * Client Rest Controller provides APIs for <code>Client<code> CRUD operations.
 * 
 * @author SayedBaladoh
 */
@Api(value = "Clients", description = "Client's operations APIs", tags = { "Clients" })
@RequiredArgsConstructor
@RestController
@RequestMapping("/therapists/{therapistId}/clients")
public class ClientController {

	private final ClientService clientService;

	/**
	 * Add a new client to an therapist client’s list.
	 * 
	 * @param therapistId The therapist Id.
	 * @param clientDto   The client details.
	 * @return the saved client.
	 */
	@ApiOperation(value = "Add a new client to therapist client’s list", nickname = "addClient", notes = "Insert a new client", tags = {
			"Clients" }, response = ClientResponseDto.class)
	@PostMapping(consumes = { "application/json" })
	public ResponseEntity<ClientResponseDto> addClient(@PathVariable("therapistId") UUID therapistId,
			@Valid @RequestBody ClientRequestDto clientDto) {

		ClientResponseDto client = clientService.addClient(therapistId, clientDto);

		String uri = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/therapists/{therapistId}/clients/{id}")
				.buildAndExpand(therapistId, client.getId()).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", uri);

		return new ResponseEntity<>(client, headers, HttpStatus.CREATED);
	}

	/**
	 * Get all clients details for an therapist.
	 * 
	 * @param therapistId The therapist Id.
	 * @return The list of clients for therapist.
	 */
	@ApiOperation(value = "Return list of clients for therapist", nickname = "getAllClients", notes = "Get a list of clients", tags = {
			"Clients" }, response = List.class)
	@GetMapping(produces = { "application/json" })
	public ResponseEntity<List<ClientResponseDto>> getAllClients(@PathVariable("therapistId") UUID therapistId) {

		List<ClientResponseDto> clients = clientService.getAllClients(therapistId);
		return new ResponseEntity<>(clients, HttpStatus.OK);
	}

	/**
	 * Get the client details by therapist Id and client Id.
	 * 
	 * @param therapistId The therapist Id.
	 * @param clientId    The client Id.
	 * @return The client details.
	 */
	@ApiOperation(value = "Return a client details", nickname = "getClient", notes = "Get a client details", tags = {
			"Clients" }, response = ClientResponseDto.class)
	@GetMapping(value = "/{clientId}", produces = { "application/json" })
	public ResponseEntity<ClientResponseDto> getClient(@PathVariable("therapistId") UUID therapistId,
			@PathVariable("clientId") UUID clientId) {

		return new ResponseEntity<>(clientService.getClient(therapistId, clientId), HttpStatus.OK);
	}

	/**
	 * Update an existing client.
	 * 
	 * @param therapistId The therapist Id.
	 * @param clientId    The client Id.
	 * @param clientDto   The client details.
	 * @return The updated client.
	 */
	@ApiOperation(value = "Edit a client details", nickname = "updateClient", notes = "Update a client details", tags = {
			"Clients" }, response = ClientResponseDto.class)
	@PutMapping(value = "/{clientId}", consumes = { "application/json" })
	public ResponseEntity<ClientResponseDto> updateClient(@PathVariable("therapistId") UUID therapistId,
			@PathVariable("clientId") UUID clientId, @RequestBody @Valid ClientUpdateRequestDto clientDto) {

		return new ResponseEntity<>(clientService.updateClient(therapistId, clientId, clientDto), HttpStatus.OK);
	}

	/**
	 * Delete an existing client.
	 * 
	 * @param therapistId The therapist Id.
	 * @param clientId    The client Id.
	 * @return
	 */
	@ApiOperation(value = "Delete an existing client by Id", nickname = "deleteClient", notes = "Delete an existing client by Id", tags = {
			"Clients" })
	@DeleteMapping("/{clientId}")
	public ResponseEntity<?> deleteClient(@PathVariable("therapistId") UUID therapistId, @PathVariable UUID clientId) {

		clientService.removeClient(therapistId, clientId);
		return ResponseEntity.ok().build();
	}

	/**
	 * Delete all existing client by therapist Id.
	 * 
	 * @param therapistId The therapist Id.
	 * @return
	 */
	@ApiOperation(value = "Delete all existing client by therapist Id", nickname = "deleteAllClient", notes = "Delete all existing client by therapist Id", tags = {
			"Clients" })
	@DeleteMapping
	public ResponseEntity<?> deleteAllClients(@PathVariable("therapistId") UUID therapistId) {

		clientService.removeAllClients(therapistId);
		return ResponseEntity.ok().build();
	}

}
