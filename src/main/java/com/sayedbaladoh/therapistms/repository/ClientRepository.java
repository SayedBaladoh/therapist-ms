package com.sayedbaladoh.therapistms.repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.sayedbaladoh.therapistms.model.Client;

@Repository
public class ClientRepository extends InMemoryRepository<UUID, Map<UUID, Client>> {

	public Client save(UUID therapistId, Client client) {
		if (client.getId() == null)
			client.setId(UUID.randomUUID());

		Map<UUID, Client> clients = findById(therapistId).orElse(new ConcurrentHashMap<>());
		clients.put(client.getId(), client);
		save(therapistId, clients);

		return client;
	}
}
