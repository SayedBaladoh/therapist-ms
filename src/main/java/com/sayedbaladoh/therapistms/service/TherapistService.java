package com.sayedbaladoh.therapistms.service;

import java.util.List;
import java.util.UUID;

import com.sayedbaladoh.therapistms.dto.TherapistRequestDto;
import com.sayedbaladoh.therapistms.dto.TherapistResponseDto;
import com.sayedbaladoh.therapistms.dto.TherapistUpdateRequestDto;

public interface TherapistService {

	TherapistResponseDto addTherapist(TherapistRequestDto therapist);

	List<TherapistResponseDto> getAllTherapists();

	TherapistResponseDto getTherapist(UUID id);

	TherapistResponseDto updateTherapist(UUID id, TherapistUpdateRequestDto therapistDto);
}
