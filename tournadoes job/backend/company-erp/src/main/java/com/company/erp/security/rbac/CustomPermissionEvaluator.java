package com.company.erp.security.rbac;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

/**
 * Custom permission evaluator enabling expressions like:
 *   @PreAuthorize("hasPermission(null, 'employee:create')")
 *   @PreAuthorize("hasPermission(#id, 'Employee', 'employee:read')")
 * 
 * Also grants access to:
 *   - Admin users (ROLE_ADMIN)
 *   - HR users (ROLE_HR)
 *   - Manager users (ROLE_MANAGER) - for viewing performance data
 *   - Employee users (ROLE_EMPLOYEE) - for viewing their own performance data
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    // Roles that can access performance data for viewing
    private static final Set<String> PERFORMANCE_VIEW_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_MANAGER",
        "ROLE_EMPLOYEE",
        "ROLE_FINANCE",
        "ROLE_USER",
        "ROLE_TEACHER"
    );

    // Roles that can create/update performance data
    private static final Set<String> PERFORMANCE_MANAGE_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER"
    );

    // Roles that can create/update/delete projects
    private static final Set<String> PROJECT_MANAGE_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_MANAGER",
        "ROLE_FINANCE",
        "ROLE_USER",
        "ROLE_TEACHER"
    );

    // Roles that can view projects
    private static final Set<String> PROJECT_VIEW_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_MANAGER",
        "ROLE_EMPLOYEE",
        "ROLE_FINANCE",
        "ROLE_USER",
        "ROLE_TEACHER"
    );

    // Roles that can view attendance data
    private static final Set<String> ATTENDANCE_VIEW_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_MANAGER",
        "ROLE_EMPLOYEE",
        "ROLE_FINANCE",
        "ROLE_USER",
        "ROLE_TEACHER"
    );

    // Roles that can create/update/delete attendance data
    private static final Set<String> ATTENDANCE_MANAGE_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_MANAGER"
    );

    // Roles that can view leave data
    private static final Set<String> LEAVE_VIEW_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_MANAGER",
        "ROLE_EMPLOYEE",
        "ROLE_FINANCE",
        "ROLE_USER",
        "ROLE_TEACHER"
    );

    // Roles that can request leave
    private static final Set<String> LEAVE_REQUEST_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_MANAGER",
        "ROLE_EMPLOYEE",
        "ROLE_FINANCE",
        "ROLE_USER",
        "ROLE_TEACHER"
    );

    // Roles that can approve/reject leave
    private static final Set<String> LEAVE_APPROVE_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_MANAGER"
    );

    // Roles that can create/update/delete departments
    private static final Set<String> DEPARTMENT_MANAGE_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_MANAGER"
    );

    // Roles that can view departments
    private static final Set<String> DEPARTMENT_VIEW_ROLES = Set.of(
        "ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_MANAGER",
        "ROLE_EMPLOYEE",
        "ROLE_FINANCE",
        "ROLE_USER",
        "ROLE_TEACHER"
    );

    @Override
    public boolean hasPermission(Authentication authentication,
                                  Object targetDomainObject,
                                  Object permission) {
        if (authentication == null || permission == null) return false;
        
        // Grant access to admins and HR users
        if (isAdminOrHR(authentication)) {
            return true;
        }
        
        // Grant access for performance:view based on roles in PERFORMANCE_VIEW_ROLES
        if ("performance:view".equals(permission.toString()) && hasAnyRole(authentication, PERFORMANCE_VIEW_ROLES)) {
            return true;
        }
        
        // Grant access for project:create, project:update, project:delete based on PROJECT_MANAGE_ROLES
        if (("project:create".equals(permission.toString()) || 
             "project:update".equals(permission.toString()) || 
             "project:delete".equals(permission.toString())) && 
            hasAnyRole(authentication, PROJECT_MANAGE_ROLES)) {
            return true;
        }
        
        // Grant access for project:read based on PROJECT_VIEW_ROLES
        if ("project:read".equals(permission.toString()) && hasAnyRole(authentication, PROJECT_VIEW_ROLES)) {
            return true;
        }

        // Grant access for attendance:read based on ATTENDANCE_VIEW_ROLES
        if ("attendance:read".equals(permission.toString()) && hasAnyRole(authentication, ATTENDANCE_VIEW_ROLES)) {
            return true;
        }

        // Grant access for attendance:create, attendance:update, attendance:delete based on ATTENDANCE_MANAGE_ROLES
        if (("attendance:create".equals(permission.toString()) ||
             "attendance:update".equals(permission.toString()) ||
             "attendance:delete".equals(permission.toString())) &&
            hasAnyRole(authentication, ATTENDANCE_MANAGE_ROLES)) {
            return true;
        }

        // Grant access for leave:read based on LEAVE_VIEW_ROLES
        if ("leave:read".equals(permission.toString()) && hasAnyRole(authentication, LEAVE_VIEW_ROLES)) {
            return true;
        }

        // Grant access for leave:request based on LEAVE_REQUEST_ROLES
        if ("leave:request".equals(permission.toString()) && hasAnyRole(authentication, LEAVE_REQUEST_ROLES)) {
            return true;
        }

        // Grant access for leave:approve based on LEAVE_APPROVE_ROLES
        if ("leave:approve".equals(permission.toString()) && hasAnyRole(authentication, LEAVE_APPROVE_ROLES)) {
            return true;
        }

        // Grant access for department:create, department:update, department:delete based on DEPARTMENT_MANAGE_ROLES
        if (("department:create".equals(permission.toString()) ||
             "department:update".equals(permission.toString()) ||
             "department:delete".equals(permission.toString())) &&
            hasAnyRole(authentication, DEPARTMENT_MANAGE_ROLES)) {
            return true;
        }

        // Grant access for department:read based on DEPARTMENT_VIEW_ROLES
        if ("department:read".equals(permission.toString()) && hasAnyRole(authentication, DEPARTMENT_VIEW_ROLES)) {
            return true;
        }

        return hasAuthority(authentication, permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                  Serializable targetId,
                                  String targetType,
                                  Object permission) {
        if (authentication == null || permission == null) return false;
        
        // Grant access to admins and HR users
        if (isAdminOrHR(authentication)) {
            return true;
        }
        
        // Grant access for performance:view based on roles in PERFORMANCE_VIEW_ROLES
        if ("performance:view".equals(permission.toString()) && hasAnyRole(authentication, PERFORMANCE_VIEW_ROLES)) {
            return true;
        }
        
        // Grant access for project:create, project:update, project:delete based on PROJECT_MANAGE_ROLES
        if (("project:create".equals(permission.toString()) || 
             "project:update".equals(permission.toString()) || 
             "project:delete".equals(permission.toString())) && 
            hasAnyRole(authentication, PROJECT_MANAGE_ROLES)) {
            return true;
        }
        
        // Grant access for project:read based on PROJECT_VIEW_ROLES
        if ("project:read".equals(permission.toString()) && hasAnyRole(authentication, PROJECT_VIEW_ROLES)) {
            return true;
        }

        // Grant access for attendance:read based on ATTENDANCE_VIEW_ROLES
        if ("attendance:read".equals(permission.toString()) && hasAnyRole(authentication, ATTENDANCE_VIEW_ROLES)) {
            return true;
        }

        // Grant access for attendance:create, attendance:update, attendance:delete based on ATTENDANCE_MANAGE_ROLES
        if (("attendance:create".equals(permission.toString()) ||
             "attendance:update".equals(permission.toString()) ||
             "attendance:delete".equals(permission.toString())) &&
            hasAnyRole(authentication, ATTENDANCE_MANAGE_ROLES)) {
            return true;
        }

        // Grant access for leave:read based on LEAVE_VIEW_ROLES
        if ("leave:read".equals(permission.toString()) && hasAnyRole(authentication, LEAVE_VIEW_ROLES)) {
            return true;
        }

        // Grant access for leave:request based on LEAVE_REQUEST_ROLES
        if ("leave:request".equals(permission.toString()) && hasAnyRole(authentication, LEAVE_REQUEST_ROLES)) {
            return true;
        }

        // Grant access for leave:approve based on LEAVE_APPROVE_ROLES
        if ("leave:approve".equals(permission.toString()) && hasAnyRole(authentication, LEAVE_APPROVE_ROLES)) {
            return true;
        }

        // Grant access for department:create, department:update, department:delete based on DEPARTMENT_MANAGE_ROLES
        if (("department:create".equals(permission.toString()) ||
             "department:update".equals(permission.toString()) ||
             "department:delete".equals(permission.toString())) &&
            hasAnyRole(authentication, DEPARTMENT_MANAGE_ROLES)) {
            return true;
        }

        // Grant access for department:read based on DEPARTMENT_VIEW_ROLES
        if ("department:read".equals(permission.toString()) && hasAnyRole(authentication, DEPARTMENT_VIEW_ROLES)) {
            return true;
        }

        return hasAuthority(authentication, permission.toString());
    }

    private boolean hasAuthority(Authentication authentication, String permission) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(permission));
    }
    
    /**
     * Check if user has any of the specified roles
     */
    private boolean hasAnyRole(Authentication authentication, Set<String> roles) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(roles::contains);
    }
    
    /**
     * Check if user has ADMIN or HR role
     */
    private boolean isAdminOrHR(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_HR_MANAGER"));
    }
    
    /**
     * Check if user has MANAGER role
     */
    private boolean isManager(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_MANAGER"));
    }
    
    /**
     * Check if user has EMPLOYEE role
     */
    private boolean isEmployee(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_EMPLOYEE"));
    }
}
