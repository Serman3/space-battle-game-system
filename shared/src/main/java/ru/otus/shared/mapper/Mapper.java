package ru.otus.shared.mapper;

public interface Mapper<D, E> {

    E mapToEntity(D dto);

    D mapToDto(E entity);
}
