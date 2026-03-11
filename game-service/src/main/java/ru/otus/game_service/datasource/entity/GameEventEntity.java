package ru.otus.game_service.datasource.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.validation.constraints.NotNull;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Getter
@Setter
@Entity
@Table(name = "t_game_event", schema = "person_game")
public class GameEventEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition="serial")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_game", nullable = false)
    private GameEntity game;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

}
