package com.sayedbaladoh.therapistms.dto;

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClientResponseDto {

	@ApiModelProperty(readOnly = true)
	private UUID id;
	private UUID therapistId;
	private String name;
	private String email;
	private String phoneNumber;
	private String address;
	private String job;
	private Date birthDate;
	private String gender;
}