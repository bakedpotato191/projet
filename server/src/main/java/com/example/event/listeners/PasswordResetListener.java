package com.example.event.listeners;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.event.OnPasswordResetRequestedEvent;
import com.example.rest.model.User;
import com.example.service.AuthService;

@Component
public class PasswordResetListener {

	@Autowired
    private AuthService authService;
	
	@Autowired
    private MessageSource messages;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Value("${support.email}")
	private String supportEmail;

	@Async
	@EventListener
	public void onApplicationEvent(final OnPasswordResetRequestedEvent event) {
		this.confirmReset(event);
	}
	
	private void confirmReset(final OnPasswordResetRequestedEvent event) {
        var user = event.getUser();
        var token = UUID.randomUUID().toString();
        authService.createPasswordResetTokenForUser(user, token);
        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }

    //

    private SimpleMailMessage constructEmailMessage(final OnPasswordResetRequestedEvent event, final User user, final String token) {
        var recipientAddress = user.getEmail();
        var subject = "Password Reset";
        var confirmationUrl = "http://localhost:4200" + "/password_reset/" + token;
        var message = messages.getMessage("message.regSuccLink", null, "To update your password please click on the link below", event.getLocale());
        var email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(supportEmail);
        return email;
    }
}

