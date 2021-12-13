package com.sayedbaladoh.therapistms.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sayedbaladoh.therapistms.model.Therapist;

@Repository
public class TherapistRepository extends InMemoryRepository<UUID, Therapist> {

	public Therapist saveOrUpdate(Therapist therapist) {
		if (therapist.getId() == null) {
			therapist.setId(UUID.randomUUID());
		}
		save(therapist.getId(), therapist);
		return therapist;
	}
}
