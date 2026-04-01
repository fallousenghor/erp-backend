package com.company.erp.modules.settings.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Settings Controller - System settings management
 */
@RestController
@RequestMapping("/api/v1/settings")
@Tag(name = "Settings", description = "System settings management")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class SettingsController {

    // In-memory settings (should be persisted to database in production)
    private final Map<String, Object> companySettings = new HashMap<>();
    private final Map<String, Object> notificationSettings = new HashMap<>();
    private final Map<String, Object> securitySettings = new HashMap<>();
    private final Map<String, Object> regionalSettings = new HashMap<>();

    @GetMapping("/company")
    @Operation(summary = "Get company information")
    public ResponseEntity<Map<String, Object>> getCompanyInfo() {
        if (companySettings.isEmpty()) {
            // Default values
            companySettings.put("name", "AEVUM Enterprise");
            companySettings.put("address", "Dakar, Sénégal");
            companySettings.put("phone", "+221 33 000 00 00");
            companySettings.put("email", "contact@aevum.sn");
            companySettings.put("website", "https://aevum.sn");
            companySettings.put("taxId", "NINEA: 0000000-00");
            companySettings.put("logo", null);
        }
        return ResponseEntity.ok(companySettings);
    }

    @PutMapping("/company")
    @Operation(summary = "Update company information")
    public ResponseEntity<Map<String, Object>> updateCompanyInfo(
            @RequestBody Map<String, Object> settings) {
        companySettings.putAll(settings);
        return ResponseEntity.ok(companySettings);
    }

    @GetMapping("/notifications")
    @Operation(summary = "Get notification settings")
    public ResponseEntity<Map<String, Object>> getNotificationSettings() {
        if (notificationSettings.isEmpty()) {
            notificationSettings.put("emailAlerts", true);
            notificationSettings.put("pushNotifications", true);
            notificationSettings.put("weeklyReport", false);
            notificationSettings.put("securityAlerts", true);
            notificationSettings.put("attendanceAlerts", true);
            notificationSettings.put("invoiceReminders", true);
        }
        return ResponseEntity.ok(notificationSettings);
    }

    @PutMapping("/notifications")
    @Operation(summary = "Update notification settings")
    public ResponseEntity<Map<String, Object>> updateNotificationSettings(
            @RequestBody Map<String, Object> settings) {
        notificationSettings.putAll(settings);
        return ResponseEntity.ok(notificationSettings);
    }

    @GetMapping("/security")
    @Operation(summary = "Get security settings")
    public ResponseEntity<Map<String, Object>> getSecuritySettings() {
        if (securitySettings.isEmpty()) {
            securitySettings.put("twoFactor", false);
            securitySettings.put("sessionTimeout", 30); // minutes
            securitySettings.put("passwordExpiry", 90); // days
            securitySettings.put("ipWhitelist", false);
            securitySettings.put("loginAlerts", true);
        }
        return ResponseEntity.ok(securitySettings);
    }

    @PutMapping("/security")
    @Operation(summary = "Update security settings")
    public ResponseEntity<Map<String, Object>> updateSecuritySettings(
            @RequestBody Map<String, Object> settings) {
        securitySettings.putAll(settings);
        return ResponseEntity.ok(securitySettings);
    }

    @GetMapping("/regional")
    @Operation(summary = "Get regional settings")
    public ResponseEntity<Map<String, Object>> getRegionalSettings() {
        if (regionalSettings.isEmpty()) {
            regionalSettings.put("language", "fr");
            regionalSettings.put("timezone", "Africa/Dakar");
            regionalSettings.put("currency", "XOF");
            regionalSettings.put("dateFormat", "DD/MM/YYYY");
            regionalSettings.put("timeFormat", "HH:mm");
        }
        return ResponseEntity.ok(regionalSettings);
    }

    @PutMapping("/regional")
    @Operation(summary = "Update regional settings")
    public ResponseEntity<Map<String, Object>> updateRegionalSettings(
            @RequestBody Map<String, Object> settings) {
        regionalSettings.putAll(settings);
        return ResponseEntity.ok(regionalSettings);
    }
}
