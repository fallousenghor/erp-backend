package com.ceremony.ceremony_backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ceremony.ceremony_backend.dto.request.CreateMemberRequest;
import com.ceremony.ceremony_backend.entity.Member;
import com.ceremony.ceremony_backend.entity.User;
import com.ceremony.ceremony_backend.enums.MemberStatus;
import com.ceremony.ceremony_backend.enums.Role;
import com.ceremony.ceremony_backend.exception.BadRequestException;
import com.ceremony.ceremony_backend.repository.MemberRepository;
import com.ceremony.ceremony_backend.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final BadgeGeneratorService badgeGeneratorService;
    private final PasswordGeneratorService passwordGeneratorService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public Member createMember(CreateMemberRequest request) {
        // Vérifier si l'email existe déjà
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Un membre avec cet email existe déjà");
        }
        
        if (memberRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Un membre avec ce numéro de téléphone existe déjà");
        }
        
        // Générer le badge
        String badgeNumber = badgeGeneratorService.generateBadgeNumber();
        
        // Générer le mot de passe temporaire
        String temporaryPassword = passwordGeneratorService.generateTemporaryPassword();
        
        // Créer le compte utilisateur
        String username = generateUsername(request.getFirstName(), request.getLastName());
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(temporaryPassword))
                .role(Role.MEMBER)
                .isPasswordChangeable(true)
                .isTemporaryPassword(true)
                .enabled(true)
                .build();
        
        user = userRepository.save(user);
        
        // Créer le membre
        Member member = Member.builder()
                .badgeNumber(badgeNumber)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .status(MemberStatus.ACTIVE)
                .user(user)
                .build();
        
        member = memberRepository.save(member);
        
        // Envoyer l'email de bienvenue avec APK et mot de passe
        try {
            emailService.sendMemberWelcomeEmail(
                member.getEmail(),
                member.getFullName(),
                member.getBadgeNumber(),
                username,
                temporaryPassword
            );
            log.info("Email de bienvenue envoyé à {}", member.getEmail());
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email à {}", member.getEmail(), e);
            // On ne bloque pas la création du membre si l'email échoue
        }
        
        return member;
    }
    
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
    
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Membre non trouvé"));
    }
    
    public Member updateMember(Long id, CreateMemberRequest request) {
        Member member = getMemberById(id);
        
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setPhone(request.getPhone());
        member.setDateOfBirth(request.getDateOfBirth());
        member.setAddress(request.getAddress());
        
        return memberRepository.save(member);
    }
    
    public void deleteMember(Long id) {
        Member member = getMemberById(id);
        member.setStatus(MemberStatus.INACTIVE);
        memberRepository.save(member);
    }
    
    private String generateUsername(String firstName, String lastName) {
        String baseUsername = (firstName.substring(0, 1) + lastName).toLowerCase().replaceAll("\\s+", "");
        String username = baseUsername;
        int counter = 1;
        
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }
        
        return username;
    }
}