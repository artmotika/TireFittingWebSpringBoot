package com.web.tirefitting;

import com.web.tirefitting.services.BookingHistoryService;
import com.web.tirefitting.services.BookingService;
import com.web.tirefitting.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApplicationController {

    private final UserService userService;
    private final BookingService bookingService;
    private final BookingHistoryService bookingHistoryService;

    public ApplicationController(UserService userService,
                                 BookingService bookingService,
                                 BookingHistoryService bookingHistoryService) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.bookingHistoryService = bookingHistoryService;
    }

    @GetMapping("/base")
    public ModelAndView goHome() {
        ModelAndView modelAndView = new ModelAndView("base");
//        modelAndView.addObject("CurBooking", bookingService.getBookingById(0));
        return modelAndView;
    }

    @GetMapping("/booking")
    public ModelAndView booking() {
        ModelAndView modelAndView = new ModelAndView("booking");
        return modelAndView;
    }

}
