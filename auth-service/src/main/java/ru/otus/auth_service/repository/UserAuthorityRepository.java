package ru.otus.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.auth_service.entity.UserAuthorityEntity;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthorityEntity, Long> {
}
