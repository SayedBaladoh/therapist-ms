package com.sayedbaladoh.therapistms.model;

import java.util.Date;
import java.util.UUID;

import com.sayedbaladoh.therapistms.validation.Gender;

//import com.sayedbaladoh.ems.validator.Gender;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * The Client entity. All details for the client.
 * 
 * Extends <code>User</code> entity.
 * 
 * @author Sayed Baladoh
 *
 */
@ApiModel(description = "The Client entity has all details about the client.")
@Getter
@Setter
public class Client extends User {

	private UUID therapistId;
	private String phoneNumber;
	private String address;
	private String job;
	private Date birthDate;
	
	@Gender
	private String gender;

}
