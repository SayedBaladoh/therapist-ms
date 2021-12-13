package com.sayedbaladoh.therapistms.model;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

	private UUID id;

	@NotBlank
	private String name;

	@NotBlank
	@Email
	private String email;
}
