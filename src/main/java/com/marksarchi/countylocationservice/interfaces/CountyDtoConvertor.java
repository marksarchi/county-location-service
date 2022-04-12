package com.marksarchi.countylocationservice.interfaces;


import com.marksarchi.countylocationservice.Domain.CountyEntity;
import com.marksarchi.countylocationservice.dto.CountyDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CountyDtoConvertor {
    CountyDtoConvertor INSTANCE = Mappers.getMapper(CountyDtoConvertor.class);
    List<CountyDto> entityToDto(List<CountyEntity> countyEntities);
    List<CountyEntity> dtoToEntity(List<CountyDto> countyDtos);
    CountyDto entityToDto(CountyEntity countyEntity);

}
