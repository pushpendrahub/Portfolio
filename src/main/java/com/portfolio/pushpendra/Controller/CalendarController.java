package com.portfolio.pushpendra.Controller;

import com.portfolio.pushpendra.model.CalendarModel;
import com.portfolio.pushpendra.service.CalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }


    @PostMapping("/book-slot")
    public ResponseEntity<String> bookSlot(@RequestBody CalendarModel request) {
        boolean success = calendarService.createEvent(request);
        if (success)

            return ResponseEntity.ok("Slot booked successfully!");
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to book slot");
    }
}
