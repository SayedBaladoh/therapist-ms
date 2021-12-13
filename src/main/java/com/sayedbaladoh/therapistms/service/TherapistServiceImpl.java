package com.sayedbaladoh.therapistms.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sayedbaladoh.therapistms.dto.TherapistRequestDto;
import com.sayedbaladoh.therapistms.dto.TherapistResponseDto;
import com.sayedbaladoh.therapistms.dto.TherapistUpdateRequestDto;
import com.sayedbaladoh.therapistms.exception.ResourceNotFoundException;
import com.sayedbaladoh.therapistms.model.Therapist;
import com.sayedbaladoh.therapistms.repository.TherapistRepository;
import com.sayedbaladoh.therapistms.util.ObjectMapperHelper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TherapistServiceImpl implements TherapistService {

	private final TherapistRepository therapistRepository;
	private final ObjectMapperHelper objectMapperHelper;

	@Override
	public TherapistResponseDto addTherapist(TherapistRequestDto therapistDto) {
		Therapist therapist = objectMapperHelper.map(therapistDto, Therapist.class);
		return save(therapist);
	}

	@Override
	public List<TherapistResponseDto> getAllTherapists() {
		return objectMapperHelper.mapAll(therapistRepository.findAll(), TherapistResponseDto.class);
	}

	@Override
	public TherapistResponseDto getTherapist(UUID id) {
		return objectMapperHelper.map(get(id), TherapistResponseDto.class);
	}

	@Override
	public TherapistResponseDto updateTherapist(UUID id, TherapistUpdateRequestDto therapistDto) {
		Therapist therapist = get(id);
		objectMapperHelper.map(therapistDto, therapist);
		return save(therapist);
	}

	private Therapist get(UUID id) {
		return therapistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
				String.format("Therapist with id: %s is not found.", id.toString())));
	}

	private TherapistResponseDto save(Therapist therapist) {
		return objectMapperHelper.map(therapistRepository.saveOrUpdate(therapist), TherapistResponseDto.class);
	}
}