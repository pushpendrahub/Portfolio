package com.portfolio.pushpendra.admin.service;

import com.portfolio.pushpendra.admin.model.VisitorLogModel;
import com.portfolio.pushpendra.admin.repository.VisitorLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisitorLogService {

    private final VisitorLogRepository visitorLogRepository;

    public VisitorLogService(VisitorLogRepository visitorLogRepository) {
        this.visitorLogRepository = visitorLogRepository;
    }

    // Existing method for chart data
    public Map<String, Integer> getDailyVisitorCounts() {
        List<Object[]> rawData = visitorLogRepository.countVisitorsPerDay();

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Object[] row : rawData) {
            String date = row[0].toString(); // e.g., "2025-07-29"
            Integer count = ((Number) row[1]).intValue();
            result.put(date, count);
        }
        return result;
    }

    // New method for formatted logs
    public List<Map<String, String>> getFormattedVisitorLogs() {
        List<VisitorLogModel> logs = visitorLogRepository.findAllByOrderByVisitTimeDesc();
        List<Map<String, String>> formattedLogs = new ArrayList<>();

        for (VisitorLogModel log : logs) {
            Map<String, String> entry = new LinkedHashMap<>();

            entry.put("location", formatLocation(log.getCountry(), log.getCity()));
            entry.put("device", formatDevice(log.getUserAgent()));
            entry.put("browser", formatBrowser(log.getUserAgent()));
            entry.put("page", log.getVisitedPage()); // fixed method name
            entry.put("timeAgo", formatVisitTime(log.getVisitTime())); // renamed key to match HTML
            entry.put("ip", log.getIpAddress());


            formattedLogs.add(entry);
        }
        return formattedLogs;
    }

    // -----------------------
    // Helper methods
    // -----------------------

    private String formatLocation(String country, String city) {
        String flag = getCountryFlag(country);
        return flag + " " + (city != null ? city : "Unknown") + ", " + (country != null ? country : "Unknown");
    }

    private String getCountryFlag(String country) {
        if (country == null) return "🌍";
        String code = country.substring(0, 2).toUpperCase();
        int firstChar = Character.codePointAt(code, 0) - 0x41 + 0x1F1E6;
        int secondChar = Character.codePointAt(code, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstChar)) + new String(Character.toChars(secondChar));
    }

    private String formatDevice(String userAgent) {
        if (userAgent == null) return "💻 Unknown";
        String ua = userAgent.toLowerCase();
        if (ua.contains("mobile")) return "📱 Mobile";
        if (ua.contains("tablet")) return "📱 Tablet";
        return "💻 Desktop";
    }

    private String formatBrowser(String userAgent) {
        if (userAgent == null) return "🌐 Unknown";
        String ua = userAgent.toLowerCase();
        if (ua.contains("chrome")) return "🌐 Chrome";
        if (ua.contains("firefox")) return "🦊 Firefox";
        if (ua.contains("safari") && !ua.contains("chrome")) return "🧭 Safari";
        if (ua.contains("edge")) return "🪟 Edge";
        return "🌐 Other";
    }

    private String formatVisitTime(LocalDateTime visitTime) {
        if (visitTime == null) return "Unknown time";
        Duration diff = Duration.between(visitTime, LocalDateTime.now());

        if (diff.toMinutes() < 60) {
            return diff.toMinutes() + " min ago";
        } else if (diff.toHours() < 24) {
            return diff.toHours() + " hours ago";
        } else if (diff.toDays() == 1) {
            return "Yesterday " + visitTime.format(DateTimeFormatter.ofPattern("h:mm a"));
        } else {
            return visitTime.format(DateTimeFormatter.ofPattern("dd MMM, h:mm a"));
        }
    }

}