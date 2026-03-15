package ru.otus.game_service.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.game_service.datasource.entity.ActiveGameEntity;

@Repository
public interface ActiveGameRepository extends JpaRepository<ActiveGameEntity, Integer> {

}
