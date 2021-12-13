package com.sayedbaladoh.therapistms.dto;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.sayedbaladoh.therapistms.validation.Uuid;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TherapistUpdateRequestDto {

	@Uuid
	private UUID practiceId;

	private String name;

	@Email
	private String email;

}