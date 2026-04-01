package com.ceremonie.demo.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.ceremonie.demo.dto.response.ApiResponse;
import com.ceremonie.demo.dto.response.DashboardStatsResponse;
import com.ceremonie.demo.services.interfaces.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Statistiques globales")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "Obtenir les statistiques du dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
      DashboardStatsResponse stats = dashboardService.getDashboardStats();
      return ResponseEntity.ok(ApiResponse.success("Statistiques du dashboard", stats));
    }
  
    @GetMapping("/chart-data")
    @Operation(summary = "Obtenir les données du graphique revenus/dépenses")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getChartData() {
      // Données d'exemple pour le graphique (à remplacer par des vraies données)
      Map<String, Object> chartData = new HashMap<>();
      chartData.put("labels", Arrays.asList("Jan", "Fév", "Mar", "Avr", "Mai", "Jun", "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc"));
      chartData.put("incomeData", Arrays.asList(12000, 15000, 18000, 22000, 25000, 28000, 30000, 32000, 35000, 38000, 40000, 42000));
      chartData.put("expenseData", Arrays.asList(8000, 10000, 12000, 15000, 18000, 20000, 22000, 24000, 26000, 28000, 30000, 32000));
  
      return ResponseEntity.ok(ApiResponse.success("Données du graphique", chartData));
    }
}

