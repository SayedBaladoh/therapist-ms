package com.sayedbaladoh.therapistms.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperHelper {

	private final ModelMapper modelMapper;

	@Autowired
	public ObjectMapperHelper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@PostConstruct
	public void init() {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
	}

	public <S, D> D map(final S source, Class<D> outClass) {
		return modelMapper.map(source, outClass);
	}

	public <S, D> D map(final S source, D destination) {
		modelMapper.map(source, destination);
		return destination;
	}

	public <S, D> List<D> mapAll(final Collection<S> sourceList, Class<D> outClass) {
		return sourceList.stream().map(entity -> map(entity, outClass)).collect(Collectors.toList());
	}

}
