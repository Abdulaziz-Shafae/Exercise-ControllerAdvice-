package com.example.loot.Service;

import com.example.loot.DTO.LowStockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String name) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("lo0ot.ksa@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Welcome to Loot!");

        message.setText(
                "Hi " + name + ",\n\n" +
                        "Welcome to Loot!\n\n" +
                        "Your account has been created successfully.\n" +
                        "You can now start managing your pantry and discovering recipes.\n\n" +
                        "Enjoy cooking!\n" +
                        "Loot Team"
        );

        mailSender.send(message);
    }

    public void sendVerificationCode(String toEmail, String name, Integer code) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("lo0ot.ksa@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Loot Password Reset Code");

        message.setText(
                "Hi " + name + ",\n\n" +
                        "We received a request to reset your Loot password.\n\n" +
                        "Your verification code is:\n\n" +
                        code + "\n\n" +
                        "Use this code to reset your password.\n" +
                        "If you did not request a password reset, you can ignore this email.\n\n" +
                        "Loot Team"
        );

        mailSender.send(message);
    }

    public void sendLowStockEmail(String toEmail, String name, List<LowStockDTO> list) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("lo0ot.ksa@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Low Stock List");

        StringBuilder lowStockList = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {

            lowStockList.append(list.get(i).getName())
                    .append("\nAvailable: ")
                    .append(list.get(i).getAvailable())
                    .append("\nLow Stock Threshold: ")
                    .append(list.get(i).getThreshold())
                    .append("\nNeed to buy: ")
                    .append(list.get(i).getNeedToBuy())
                    .append("\n\n");
        }

        message.setText(
                "Hi " + name + ",\n\n" +
                        "The following ingredients in your pantry are running low:\n\n" +
                        lowStockList +
                        "Consider restocking these ingredients soon.\n\n" +
                        "Loot Team"
        );

        mailSender.send(message);
    }


}