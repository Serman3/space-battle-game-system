package ru.otus.auth_service.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.auth_service.datasource.entity.UserEntity;
import ru.otus.shared.utils.UserStatus;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsername(String username);

    @Modifying
    @Query(value = """
            UPDATE person.t_user SET status = :status, active = false WHERE id = :id
            """, nativeQuery = true)
    void updateStatus(@Param(value = "id") Integer id, @Param(value = "status") UserStatus userStatus);
}
