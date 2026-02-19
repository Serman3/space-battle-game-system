package ru.otus.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.datasource.entity.ActiveGameEntity;

@Repository
public interface ActiveGameRepository extends JpaRepository<ActiveGameEntity, Long> {

    @Query(value = """
            insert into t_active_game (id_game, id_user, created_date)
            select (select id from t_game where id_game = :gameId), u.id, now()
            from t_user u
            where u.username = :username
            """, nativeQuery = true)
    void insertActiveGameByGameIdAndUsername(@Param("gameId") String gameId, @Param("username") String username);
}
