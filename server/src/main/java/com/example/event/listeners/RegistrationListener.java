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

import com.example.event.OnRegistrationCompleteEvent;
import com.example.rest.model.User;
import com.example.service.AuthService;

@Component
public class RegistrationListener {

	@Autowired
    	private AuthService authService;
	
	@Autowired
    	private MessageSource messages;
	
	@Autowired
    	private JavaMailSender mailSender;

	@Value("${support.email}")
	private String supportEmail;
	
	@Value("${app.frontend.url}")
	private String url;
	
	@Async
	@EventListener
	public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);	
	}
	
	private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        	var user = event.getUser();
        	var token = UUID.randomUUID().toString();
        	authService.createVerificationTokenForUser(user, token);

        	final SimpleMailMessage email = constructEmailMessage(event, user, token);
        	mailSender.send(email);
    	}

    	private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        	var recipientAddress = user.getEmail();
        	var subject = "Registration Confirmation";
		var confirmationUrl = url + "/confirmation/" + token;
		var message = messages.getMessage("message.regSuccLink", null, "Votre compte a été enregistré avec succès. "
				+ "Pour confirmer votre email, veuillez cliquer sur le lien ci-dessous.", event.getLocale());
		var email = new SimpleMailMessage();

		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + " \r\n" + confirmationUrl);
		email.setFrom(supportEmail);
        
        	return email;
    	}
}
