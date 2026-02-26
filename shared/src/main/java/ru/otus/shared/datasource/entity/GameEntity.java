package ru.otus.shared.datasource.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Getter
@Setter
@Entity
@Table(name = "t_game", schema = "person")
public class GameEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition="serial")
    private Integer id;

    @NotNull
    @Column(name = "id_game", nullable = false, unique = true)
    private String idGame;

    @NotNull
    @ColumnDefault("(now() AT TIME ZONE 'utc'::text)")
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;
}
