package com.company.erp.modules.auth.domain.model;

import com.company.erp.shared.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Permission entity — represents a fine-grained action (e.g. "employee:create").
 */
@Entity
@Table(name = "permissions",
        uniqueConstraints = @UniqueConstraint(name = "uk_permission_name", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;
}
