package ru.otus.shared.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.shared.datasource.entity.ActiveGameEntity;

import java.util.List;

@Repository
public interface ActiveGameRepository extends JpaRepository<ActiveGameEntity, Integer> {

    @Modifying
    @Query(value = """
            insert into person.t_active_game (id_game, id_user, created_date)
            select (select id from person.t_game where id_game = :gameId), u.id, now()
            from person.t_user u
            where u.username in :users
            """, nativeQuery = true)
    void insertActiveGameByGameIdAndUsername(@Param("gameId") String gameId, @Param("users") List<String> usernames);
}
