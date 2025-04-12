package com.example.javitto.service;

import com.example.javitto.DTO.NotificationDTO;
import com.example.javitto.service.spi.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void send(NotificationDTO notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getRecipient());
        message.setSubject(notification.getSubject());
        message.setText(notification.getText());

        mailSender.send(message);
        log.info("Отправка уведомления... ");
    }

    @Async
    public void sendRegistrationEmail(String email, String username) {
        NotificationDTO notification = new NotificationDTO();
        notification.setRecipient(email);
        notification.setSubject("Добро пожаловать в Javitto!");
        notification.setText("Здравствуйте, " + username +
                "Ваша регистрация успешно завершена. Добро пожаловать на нашу платформу Javitto!");

        send(notification);
        log.info("Отправка уведомления после регистрации пользователю : {}", username);
    }

    @Async
    public void sendAdvertisementEmail(String email, String title, String username) {
        NotificationDTO notification = new NotificationDTO();
        notification.setRecipient(email);
        notification.setSubject("Спасибо за то что пользуетесь Javitto!");
        notification.setText("Привет, " + username + "! Вы выставили объявление на продажу " + title);

        send(notification);
        log.info("Отправка уведомления после регистрации пользователю : {}", username);
    }

}
