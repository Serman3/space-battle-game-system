package ru.otus.auth_service.datasource.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import ru.otus.datasource.entity.BaseEntity;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Setter
@Getter
@Entity
@Table(name = "t_deactivated_token", schema = "person")
public class DeactivatedTokenEntity extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "c_keep_until", nullable = false)
    private Instant keepUntil;

}