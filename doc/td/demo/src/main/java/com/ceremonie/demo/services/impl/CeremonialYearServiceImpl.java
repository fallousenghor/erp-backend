package com.ceremonie.demo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceremonie.demo.dto.request.CreateCeremonialYearRequest;
import com.ceremonie.demo.dto.response.CeremonialYearResponse;
import com.ceremonie.demo.entity.CeremonialYear;
import com.ceremonie.demo.exceptions.DuplicateResourceException;
import com.ceremonie.demo.exceptions.ResourceNotFoundException;
import com.ceremonie.demo.repository.CeremonialYearRepository;
import com.ceremonie.demo.repository.EventRepository;
import com.ceremonie.demo.repository.MediaRepository;
import com.ceremonie.demo.repository.TransactionRepository;
import com.ceremonie.demo.services.interfaces.CeremonialYearService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CeremonialYearServiceImpl implements CeremonialYearService {

    private final CeremonialYearRepository ceremonialYearRepository;
    private final MediaRepository mediaRepository;
    private final EventRepository eventRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public CeremonialYearResponse createCeremonialYear(CreateCeremonialYearRequest request) {
        if (ceremonialYearRepository.existsByYear(request.getYear())) {
            throw new DuplicateResourceException("L'année cérémoniale existe déjà");
        }

        CeremonialYear ceremonialYear = CeremonialYear.builder()
                .year(request.getYear())
                .theme(request.getTheme())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .initialBudget(request.getInitialBudget())
                .active(false)
                .totalIncome(BigDecimal.ZERO)
                .totalExpense(BigDecimal.ZERO)
                .totalContributions(BigDecimal.ZERO)
                .build();

        CeremonialYear saved = ceremonialYearRepository.save(ceremonialYear);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public CeremonialYearResponse updateCeremonialYear(Long id, CreateCeremonialYearRequest request) {
        CeremonialYear ceremonialYear = ceremonialYearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Année cérémoniale non trouvée"));

        ceremonialYear.setTheme(request.getTheme());
        ceremonialYear.setDescription(request.getDescription());
        ceremonialYear.setStartDate(request.getStartDate());
        ceremonialYear.setEndDate(request.getEndDate());
        ceremonialYear.setInitialBudget(request.getInitialBudget());

        CeremonialYear updated = ceremonialYearRepository.save(ceremonialYear);
        return mapToResponse(updated);
    }

    @Override
    public CeremonialYearResponse getCeremonialYearById(Long id) {
        CeremonialYear ceremonialYear = ceremonialYearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Année cérémoniale non trouvée"));
        return mapToResponse(ceremonialYear);
    }

    @Override
    public CeremonialYearResponse getActiveCeremonialYear() {
        CeremonialYear ceremonialYear = ceremonialYearRepository.findActiveCeremonialYear()
                .orElseThrow(() -> new ResourceNotFoundException("Aucune année cérémoniale active"));
        return mapToResponse(ceremonialYear);
    }

    @Override
    public List<CeremonialYearResponse> getAllCeremonialYears() {
        return ceremonialYearRepository.findAllActive().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void activateCeremonialYear(Long id) {
        // Désactiver toutes les années
        ceremonialYearRepository.findByActiveTrue().ifPresent(year -> {
            year.setActive(false);
            ceremonialYearRepository.save(year);
        });

        // Activer l'année sélectionnée
        CeremonialYear ceremonialYear = ceremonialYearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Année cérémoniale non trouvée"));
        ceremonialYear.setActive(true);
        ceremonialYearRepository.save(ceremonialYear);
    }

    @Override
    @Transactional
    public void deleteCeremonialYear(Long id) {
        CeremonialYear ceremonialYear = ceremonialYearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Année cérémoniale non trouvée"));
        
        if (ceremonialYear.getActive()) {
            throw new IllegalStateException("Impossible de supprimer l'année active");
        }
        
        ceremonialYear.setDeleted(true);
        ceremonialYearRepository.save(ceremonialYear);
    }

    private CeremonialYearResponse mapToResponse(CeremonialYear year) {
        Long mediaCount = mediaRepository.countMediasByYear(year.getId());
        Long eventCount = eventRepository.countEventsByYear(year.getId());
        Long transactionCount = transactionRepository.countTransactionsByYear(year.getId());
        
        BigDecimal balance = year.getTotalIncome().subtract(year.getTotalExpense());

        return CeremonialYearResponse.builder()
                .id(year.getId())
                .year(year.getYear())
                .theme(year.getTheme())
                .description(year.getDescription())
                .startDate(year.getStartDate())
                .endDate(year.getEndDate())
                .active(year.getActive())
                .initialBudget(year.getInitialBudget())
                .totalIncome(year.getTotalIncome())
                .totalExpense(year.getTotalExpense())
                .totalContributions(year.getTotalContributions())
                .balance(balance)
                .mediaCount(mediaCount)
                .eventCount(eventCount)
                .transactionCount(transactionCount)
                .build();
    }
}
