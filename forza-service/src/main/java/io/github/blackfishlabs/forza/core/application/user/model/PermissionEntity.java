package io.github.blackfishlabs.forza.core.application.user.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "APP_PERMISSION")
@EqualsAndHashCode(callSuper = true)
public class PermissionEntity extends BasicEntity<Long> {

    @NotNull
    @Size(max = 45)
    @Column(name = "role", length = 45, nullable = false, unique = true)
    private String role;

}
