package com.portfolio.pushpendra.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @PostMapping("/message")
    public String getResponse(@RequestBody String userMessage) {
        userMessage = userMessage.toLowerCase();
        if (userMessage.contains("hello") || userMessage.contains("hi")) {
            return "👋 Hey there! I’m Pushpendra’s Portfolio Assistant. You can ask me about his <b>skills</b>, <b>projects</b>, <b>email</b>,<b>Linkedin</b>,<b>Github</b> or <b>resume</b>.";
        } else if (userMessage.contains("email")) {
            return "📩 You can reach Pushpendra at:<br><a href='mailto:pushpendra.singh@example.com'>singhpushpendra9326@gmail.com</a>";
        } else if (userMessage.contains("skills")) {
            return "🛠 <b>Technical Skills:</b><br>• Java & Spring Boot<br>• MySQL & JDBC<br>• HTML, CSS & JavaScript<br>• REST APIs & Security<br>• Git & Maven";
        } else if (userMessage.contains("project")) {
            return "💻 <b>Featured Project – ClassConnect</b><br>A Spring Boot + MySQL platform for managing teachers & students with secure login and email notifications.<br>🔗 <a href='http://class.connect.edu.in:8080/'>View Project</a>";
        } else if (userMessage.contains("resume")) {
            return "📄 You can download my resume here: <a href='https://drive.google.com/uc?export=download&id=1IKt657ZNJtHhRxe9tNvC4HRttXQve6uu' target='_blank'>📥 Download Resume</a>";
        } else if (userMessage.contains("contact") || userMessage.contains("mobile") || userMessage.contains("phone")) {
            return "📞 You can contact Pushpendra at:<br>📧 Email: <a href='singhpushpendra9326@gmail.com'>singhpushpendra9326@gmail.com</a><br>📱 Mobile: +91-9876543210";
        } else if (userMessage.contains("about") || userMessage.contains("yourself") || userMessage.contains("intro")) {
            return "👨‍💻 I'm Pushpendra Singh, a passionate Java Full Stack Developer with hands-on experience in <b>Java, Spring Boot, MySQL, and modern web technologies</b>. I enjoy building scalable applications and continuously learning new skills 🚀.";
        }else if (userMessage.contains("linkedin")) {
            return "🔗 You can connect with me on LinkedIn: <a href='https://www.linkedin.com/in/pushpendra-singh-4a88bb276/' target='_blank'>linkedin.com/in/pushpendra-singh</a>";
        } else if (userMessage.contains("github")) {
            return "💻 Check out my GitHub projects: <a href='https://github.com/pushpendrahub' target='_blank'>github.com/pushpendrahub</a>";
        } else {
            return "🤔 I didn’t quite get that.<br>Try asking about <b>email</b>, <b>skills</b>, <b>projects</b>, or <b>resume</b>.";
        }

    }
}