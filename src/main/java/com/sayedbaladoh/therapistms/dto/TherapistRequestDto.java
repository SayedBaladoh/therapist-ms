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
public class TherapistRequestDto {

	@NotNull
	@Uuid
	private UUID practiceId;

	@NotBlank
	private String name;

	@NotBlank
	@Email
	private String email;

}