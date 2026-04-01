package com.ceremony.ceremony_backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendMemberWelcomeEmail(String to, String fullName, String badgeNumber, String username, String temporaryPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Bienvenue à la Cérémonie - Vos identifiants de connexion");
            message.setText(String.format(
                "Cher(e) %s,\n\n" +
                "Bienvenue à la Cérémonie !\n\n" +
                "Votre numéro de badge : %s\n" +
                "Nom d'utilisateur : %s\n" +
                "Mot de passe temporaire : %s\n\n" +
                "Veuillez changer votre mot de passe lors de votre première connexion.\n\n" +
                "Cordialement,\n" +
                "L'équipe Cérémonie",
                fullName, badgeNumber, username, temporaryPassword
            ));

            mailSender.send(message);
            log.info("Email de bienvenue envoyé à {}", to);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email à {}", to, e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }

    public void sendTreasurerCredentialsEmail(String to, String fullName, String username, String fixedPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Création de compte Trésorier - Vos identifiants de connexion");
            message.setText(String.format(
                "Cher(e) %s,\n\n" +
                "Votre compte Trésorier a été créé avec succès.\n\n" +
                "Nom d'utilisateur : %s\n" +
                "Mot de passe fixe : %s\n\n" +
                "Ce mot de passe est fixe et ne peut pas être modifié.\n\n" +
                "Cordialement,\n" +
                "L'équipe Cérémonie",
                fullName, username, fixedPassword
            ));

            mailSender.send(message);
            log.info("Email d'identification trésorier envoyé à {}", to);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email à {}", to, e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }
}