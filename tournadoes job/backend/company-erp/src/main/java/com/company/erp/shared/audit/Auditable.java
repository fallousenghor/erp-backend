package com.company.erp.shared.audit;

import java.lang.annotation.*;

/**
 * Marks a service method for audit logging.
 * The AuditAspect intercepts annotated methods and persists
 * an AuditLog entry automatically.
 *
 * Usage:
 *   @Auditable(action = "CREATE_EMPLOYEE", entity = "Employee")
 *   public EmployeeResponse createEmployee(...) { ... }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {

    /** Human-readable action name (e.g. "CREATE_INVOICE") */
    String action();

    /** The entity type being acted upon (e.g. "Invoice") */
    String entity() default "";

    /** Whether to capture old/new values (may be expensive) */
    boolean captureValues() default false;
}
