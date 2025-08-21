package com.portfolio.pushpendra.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void receiveContactForm(String name, String fromEmail, String subject, String messageBody) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo("singhpushpendra9326@gmail.com");
            helper.setSubject("📬 New Contact Received from Portfolio: " + name);

            String content = "<div style='font-family:Segoe UI, sans-serif; background:#f9f9f9; padding:30px;'>"
                    + "<div style='max-width:600px; margin:auto; background:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 4px 10px rgba(0,0,0,0.1);'>"
                    + "<div style='background-color:#2c3e50; padding:20px;'>"
                    + "<h2 style='color:#ffffff; margin:0;'>📥 New Contact Message</h2></div>"
                    + "<div style='padding:20px; color:#333;'>"
                    + "<p><strong>👤 Name:</strong> " + name + "</p>"
                    + "<p><strong>📧 Email:</strong> " + fromEmail + "</p>"
                    + "<p><strong>📝 Subject:</strong> " + subject + "</p>"
                    + "<p><strong>💬 Message:</strong></p>"
                    + "<div style='background:#f0f0f0; padding:15px; border-left:5px solid #3498db; border-radius:5px;'>"
                    + messageBody.replaceAll("\n", "<br>") + "</div></div>"
                    + "<div style='background-color:#ecf0f1; padding:15px; text-align:center; font-size:13px; color:#777;'>"
                    + "This email was triggered from your portfolio contact form.</div></div></div>";

            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public void sendContactForm(String name, String toEmail, String subject, String messageBody) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject("✅ Confirmation: Message Received by Pushpendra Singh");

            String fullName = (name == null || name.isBlank()) ? "Guest" : name.trim();

            String content = "<div style='font-family:Segoe UI, sans-serif; background:#f4f6f8; padding:30px;'>"
                    + "<div style='max-width:600px; margin:auto; background:#fff; border-radius:10px; overflow:hidden; box-shadow:0 4px 10px rgba(0,0,0,0.1);'>"

                    // 🟦 HEADER
                    + "<div style='background:#2980b9; padding:20px; color:#fff;'>"
                    + "<h2 style='margin:0;'>Hello " + fullName + ", Thank You! 🙏</h2></div>"

                    // ✉️ BODY
                    + "<div style='padding:25px; color:#333;'>"
                    + "<p>Thank you for contacting me through my portfolio website.</p>"
                    + "<p>Here’s a copy of your message:</p>"
                    + "<blockquote style='background:#f4f4f4; border-left:5px solid #3498db; padding:15px; font-style:italic;'>"
                    + messageBody.replaceAll("\n", "<br>") + "</blockquote>"
                    + "<p>I’ll review your message and get back to you soon.</p>"

                    // 🔗 FOOTER CTA
                    + "<p>Let’s connect:</p>"
                    + "<p>"
                    + "<a href='https://github.com/pushpendrahub' style='color:#2980b9; text-decoration:none;' target='_blank'>GitHub</a> | "
                    + "<a href='https://www.linkedin.com/in/pushpendra-singh-4a88bb276/' style='color:#2980b9; text-decoration:none;' target='_blank'>LinkedIn</a>"
                    + "</p>"

                    + "<p style='color:#555;'>Warm regards,</p>"
                    + "<p style='color:#2980b9;'><strong>Pushpendra Singh</strong><br>Java Full Stack Developer</p></div>"

                    // 📄 FOOTER
                    + "<div style='background:#ecf0f1; padding:15px; text-align:center; font-size:12px; color:#777;'>"
                    + "This is an automated message sent from your portfolio site. Please do not reply directly.</div></div></div>";

            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
