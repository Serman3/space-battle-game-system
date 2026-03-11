package ru.otus.game_service.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.game_service.datasource.entity.GameEntity;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Integer> {

    @Query(value = """
            select ag.id_user
            from person_game.t_active_game ag
            join person_game.t_game g on g.id = ag.id_game
            where g.id_game = :gameId
            """, nativeQuery = true)
    List<String> findAllUsersByGameId(@Param("gameId") String gameId);
}
