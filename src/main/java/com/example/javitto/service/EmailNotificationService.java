package com.example.javitto.service;

import com.example.javitto.DTO.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {
    private JavaMailSender mailSender;
    @Override
    public void send(NotificationDTO notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getRecipient());
        message.setSubject(notification.getSubject());
        message.setText(notification.getText());

        mailSender.send(message);
    }

    public void sendRegistrationEmail(String email, String username) {
        NotificationDTO notification = new NotificationDTO();
        notification.setRecipient(email);
        notification.setSubject("Добро пожаловать в Javitto!");
        notification.setText(String.format("Здравствуйте, %s!\n\n " + username +
                "Ваша регистрация успешно завершена. Добро пожаловать на нашу платформу Javitto!"));

        send(notification);
    }


}
