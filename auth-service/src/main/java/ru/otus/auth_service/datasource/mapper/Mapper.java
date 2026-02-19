package ru.otus.auth_service.datasource.mapper;

public interface Mapper<D, E> {

    E mapToEntity(D dto);

    D mapToDto(E entity);
}
