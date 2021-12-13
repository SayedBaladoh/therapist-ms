package com.sayedbaladoh.therapistms.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.sayedbaladoh.therapistms.validation.Gender;

import lombok.Data;

@Data
public class ClientUpdateRequestDto {


	private String name;

	@Email
	private String email;

	private String phoneNumber;
	private String address;
	private String job;
	private Date birthDate;
	
	@Gender
	private String gender;
}