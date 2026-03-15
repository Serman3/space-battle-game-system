package ru.otus.auth_service.datasource.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.auth_service.datasource.dto.DeactivateTokenDto;
import ru.otus.auth_service.datasource.entity.DeactivatedTokenEntity;
import ru.otus.shared.mapper.Mapper;

@Component
public class DeactivateTokenMapper implements Mapper<DeactivateTokenDto, DeactivatedTokenEntity> {

    private final ModelMapper modelMapper;

    @Autowired
    public DeactivateTokenMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DeactivatedTokenEntity mapToEntity(DeactivateTokenDto dto) {
        return modelMapper.map(dto, DeactivatedTokenEntity.class);
    }

    @Override
    public DeactivateTokenDto mapToDto(DeactivatedTokenEntity entity) {
        return modelMapper.map(entity, DeactivateTokenDto.class);
    }
}
