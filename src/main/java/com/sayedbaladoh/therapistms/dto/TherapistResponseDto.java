package com.sayedbaladoh.therapistms.dto;

import java.util.UUID;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TherapistResponseDto {

	@ApiModelProperty(readOnly = true)
	private UUID id;
	private UUID practiceId;
	private String name;
	private String email;
}