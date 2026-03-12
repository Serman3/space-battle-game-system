package ru.otus.auth_service.datasource.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import ru.otus.shared.utils.UserStatus;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@NotNull
@Setter
@Getter
@Entity
@Table(name = "t_user_outbox", schema = "person")
public class UserOutboxEventEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private UserStatus eventType;

    @Column(name = "event_payload", nullable = false)
    private String eventPayload;
}
