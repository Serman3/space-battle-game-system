package ru.otus.shared.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.shared.datasource.entity.GameEntity;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Integer> {

    @Query(value = """
            select u.username
            from t_user u
            join t_active_game ag ON ag.id_user = u.id
            join t_game g on g.id = ag.id_game
            where g.id_game = :gameId
            """, nativeQuery = true)
    List<String> findAllUsersByGameId(@Param("gameId") String gameId);
}
