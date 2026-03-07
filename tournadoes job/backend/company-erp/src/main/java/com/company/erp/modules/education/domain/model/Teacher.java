package com.company.erp.modules.education.domain.model;

import com.company.erp.shared.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Teacher entity — can be internal (linked to Employee) or external.
 */
@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseAuditEntity {

    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "specialization", length = 200)
    private String specialization;

    @Column(name = "bio", length = 1000)
    private String bio;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    /** Optional — linked to Employee aggregate by ID */
    @Column(name = "employee_id")
    private java.util.UUID employeeId;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
