package ru.otus.auth_service.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.auth_service.datasource.entity.UserOutboxEventEntity;

import java.util.UUID;

@Repository
public interface UserOutboxRepository extends JpaRepository<UserOutboxEventEntity, UUID> {

}
