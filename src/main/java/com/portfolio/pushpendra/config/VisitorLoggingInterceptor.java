package com.portfolio.pushpendra.config;

import com.portfolio.pushpendra.admin.model.VisitorLogModel;
import com.portfolio.pushpendra.admin.repository.VisitorLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Scanner;

@Component
public class VisitorLoggingInterceptor implements HandlerInterceptor {

    private final VisitorLogRepository visitorLogRepository;

    public VisitorLoggingInterceptor(VisitorLogRepository visitorLogRepository) {
        this.visitorLogRepository = visitorLogRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = getClientIp(request); // handles proxy cases

        // If running locally, fetch public IP for testing
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            try {
                URL ipService = new URL("https://api.ipify.org");
                HttpURLConnection conn = (HttpURLConnection) ipService.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                try (Scanner sc = new Scanner(conn.getInputStream())) {
                    if (sc.hasNext()) {
                        ip = sc.nextLine().trim();
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to get public IP for local testing: " + e.getMessage());
                ip = "8.8.8.8"; // fallback to Google DNS IP for testing
            }
        }

        String userAgent = request.getHeader("User-Agent");
        String browser = detectBrowser(request);
        String page = request.getRequestURI();

        // Only log allowed pages
        if (!isPageToLog(page)) {
            return true;
        }

        // Fetch geolocation
        String city = "Unknown";
        String country = "Unknown";
        try {
            URL url = new URL("http://ip-api.com/json/" + ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            StringBuilder inline = new StringBuilder();
            try (Scanner sc = new Scanner(conn.getInputStream())) {
                while (sc.hasNext()) {
                    inline.append(sc.nextLine());
                }
            }

            String json = inline.toString();
            if (json.contains("\"country\"")) {
                country = json.split("\"country\":\"")[1].split("\"")[0];
                city = json.split("\"city\":\"")[1].split("\"")[0];
            }
        } catch (Exception e) {
            System.out.println("IP lookup failed: " + e.getMessage());
        }

        // Save to DB
        VisitorLogModel model = new VisitorLogModel();
        model.setIpAddress(ip);
        model.setCity(city);
        model.setCountry(country);
        model.setUserAgent(browser);
        model.setVisitedPage(page);
        model.setVisitTime(LocalDateTime.now());

        visitorLogRepository.save(model);

        return true;
    }

    // Detect client IP considering proxies/load balancers
    private String getClientIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    // Pages we want to log
    private boolean isPageToLog(String page) {
        return page.equals("/") ||
                page.equals("/index") ||
                page.equals("/index#contact") ||
                page.equals("/index#about") ||
                page.equals("/index#portfolio") ||
                page.equals("/index#services");
    }

    private String detectBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String secChUa = request.getHeader("sec-ch-ua"); // extra header Brave sends

        if (secChUa != null && secChUa.toLowerCase().contains("brave")) {
            return "Brave";
        }

        if (userAgent == null) return "Unknown";
        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("edg/")) {
            return "Edge";
        } else if (userAgent.contains("chrome/")) {
            return "Chrome";
        } else if (userAgent.contains("firefox/")) {
            return "Firefox";
        } else if (userAgent.contains("safari/")) {
            return "Safari";
        } else {
            return "Other";
        }
    }


}
