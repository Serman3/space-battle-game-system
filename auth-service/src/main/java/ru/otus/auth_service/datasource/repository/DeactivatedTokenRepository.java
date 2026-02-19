package ru.otus.auth_service.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.auth_service.datasource.entity.DeactivatedTokenEntity;

import java.util.UUID;

@Repository
public interface DeactivatedTokenRepository extends JpaRepository<DeactivatedTokenEntity, UUID> {

    boolean existsById(UUID id);

    @Query(value = """
            select exists(select id from person.t_deactivated_token where id = :id
            """, nativeQuery = true)
    boolean existsDeactivateTokenById(@Param("id") UUID id);
}
