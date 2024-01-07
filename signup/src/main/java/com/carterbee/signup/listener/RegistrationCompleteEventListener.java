package com.carterbee.signup.listener;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.carterbee.signup.events.RegistrationCompleteEvent;
import com.carterbee.signup.user.User;
import com.carterbee.signup.user.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>{

    private final UserService userService;
    private final JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. Get the newly registered user
        User theUser = event.getUser();
        // 2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        // 3. Save the verification token for the user
        userService.saveUserVerificationToken(theUser, verificationToken);
        // 4. build the verification url to be sent to the user
        String url = event.getApplicationUrl() + "/register/verifyYourEmail?token=" + verificationToken;
        // 5. send the email.
        try {
            sendVerificationEmail(url, theUser);
        } catch (UnsupportedEncodingException  | MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        log.info("Click the link to verify your email: {}", url);
    }

    public void sendVerificationEmail(String url, User theUser) throws UnsupportedEncodingException, MessagingException{
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String content = "<p> Hi, "+ theUser.getFirstName()+ ", </p>"+
        "<p>Thank you for registering with us,"+"" +
        "Please, follow the link below to complete your registration.</p>"+
        "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
        "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("roysumit949@gmail.com", senderName);
        // messageHelper.setPassword("indeximage");
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        javaMailSender.send(message);
    }
    
}
