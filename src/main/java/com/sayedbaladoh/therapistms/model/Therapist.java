package com.sayedbaladoh.therapistms.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Therapist extends User {

	private UUID practiceId;

	@Override
	public String toString() {
		return "Therapist [practiceId=" + practiceId + ", getId()=" + getId() + ", getName()=" + getName()
				+ ", getEmail()=" + getEmail() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
}
