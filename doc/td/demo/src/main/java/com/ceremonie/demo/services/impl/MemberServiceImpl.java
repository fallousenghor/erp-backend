package com.ceremonie.demo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceremonie.demo.dto.request.CreateMemberRequest;
import com.ceremonie.demo.dto.request.UpdateMemberRequest;
import com.ceremonie.demo.dto.response.MemberResponse;
import com.ceremonie.demo.entity.Member;
import com.ceremonie.demo.exceptions.DuplicateResourceException;
import com.ceremonie.demo.exceptions.ResourceNotFoundException;
import com.ceremonie.demo.repository.MemberRepository;
import com.ceremonie.demo.services.interfaces.BadgeService;
import com.ceremonie.demo.services.interfaces.MemberService;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BadgeService badgeService;

    @Override
    @Transactional
    public MemberResponse createMember(CreateMemberRequest request) {
        if (request.getEmail() != null && memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("L'email existe déjà");
        }
        
        if (memberRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("Le numéro de téléphone existe déjà");
        }

        Member member = Member.builder()
                .memberNumber(generateMemberNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .secondaryPhone(request.getSecondaryPhone())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .registrationDate(LocalDate.now())
                .active(true)
                .emergencyContact(request.getEmergencyContact())
                .emergencyPhone(request.getEmergencyPhone())
                .notes(request.getNotes())
                .build();

        Member savedMember = memberRepository.save(member);
        
        // Générer automatiquement le badge
        badgeService.generateBadge(savedMember.getId());

        return mapToResponse(savedMember);
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long id, UpdateMemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé"));

        if (request.getEmail() != null && !request.getEmail().equals(member.getEmail())) {
            if (memberRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("L'email existe déjà");
            }
            member.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(member.getPhoneNumber())) {
            if (memberRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new DuplicateResourceException("Le numéro de téléphone existe déjà");
            }
            member.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getFirstName() != null) member.setFirstName(request.getFirstName());
        if (request.getLastName() != null) member.setLastName(request.getLastName());
        if (request.getSecondaryPhone() != null) member.setSecondaryPhone(request.getSecondaryPhone());
        if (request.getDateOfBirth() != null) member.setDateOfBirth(request.getDateOfBirth());
        if (request.getAddress() != null) member.setAddress(request.getAddress());
        if (request.getActive() != null) member.setActive(request.getActive());
        if (request.getEmergencyContact() != null) member.setEmergencyContact(request.getEmergencyContact());
        if (request.getEmergencyPhone() != null) member.setEmergencyPhone(request.getEmergencyPhone());
        if (request.getNotes() != null) member.setNotes(request.getNotes());

        Member updatedMember = memberRepository.save(member);
        return mapToResponse(updatedMember);
    }

    @Override
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé"));
        return mapToResponse(member);
    }

    @Override
    public MemberResponse getMemberByMemberNumber(String memberNumber) {
        Member member = memberRepository.findByMemberNumber(memberNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé"));
        return mapToResponse(member);
    }

    @Override
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MemberResponse> getActiveMembers() {
        return memberRepository.findAllActiveMembers().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MemberResponse> searchMembers(String search) {
        return memberRepository.searchMembers(search).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé"));
        member.setDeleted(true);
        memberRepository.save(member);
    }

    @Override
    public Long countActiveMembers() {
        return memberRepository.countActiveMembers();
    }

    @Override
    public String generateMemberNumber() {
        int currentYear = Year.now().getValue();
        Long count = memberRepository.count();
        return String.format("DTD%d%05d", currentYear, count + 1);
    }

    private MemberResponse mapToResponse(Member member) {
        MemberResponse response = MemberResponse.builder()
                .id(member.getId())
                .memberNumber(member.getMemberNumber())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .secondaryPhone(member.getSecondaryPhone())
                .dateOfBirth(member.getDateOfBirth())
                .address(member.getAddress())
                .registrationDate(member.getRegistrationDate())
                .photoUrl(member.getPhotoUrl())
                .active(member.getActive())
                .emergencyContact(member.getEmergencyContact())
                .emergencyPhone(member.getEmergencyPhone())
                .createdAt(member.getCreatedAt())
                .build();

        if (member.getBadge() != null) {
            response.setBadge(badgeService.getBadgeByMemberId(member.getId()));
        }

        return response;
    }
}