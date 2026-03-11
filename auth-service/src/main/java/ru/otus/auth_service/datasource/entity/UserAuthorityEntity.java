package ru.otus.auth_service.datasource.entity;

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
@Table(name = "t_user_authority", schema = "person")
public class UserAuthorityEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition="serial")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity user;

    @NotNull
    @Column(name = "c_authority", nullable = false)
    private String authority;

}
