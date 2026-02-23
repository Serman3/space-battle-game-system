package ru.otus.auth_service.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.auth_service.datasource.entity.UserAuthorityEntity;

import java.util.List;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthorityEntity, Integer> {

    @Query(value = """
            select c_authority from person.t_user_authority where id_user = :userId
            """, nativeQuery = true)
    List<String> findAllUserRolesByUserId(@Param("userId") Long userId);

    @Query(value = """
            select c_authority
            from person.t_user_authority ua
            join person.t_user u on u.id = ua.id_user
            where u.username = :username
            """, nativeQuery = true)
    List<String> findAllUserRolesByUserName(@Param("username") String username);

}
