package com.web.tirefitting.controllers;

import com.web.tirefitting.models.Booking;
import com.web.tirefitting.models.BookingHistory;
import com.web.tirefitting.services.BookingHistoryService;
import com.web.tirefitting.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking_history")
public class BookingHistoryController {
    private final BookingHistoryService bookingHistoryService;
    private final BookingService bookingService;

    public BookingHistoryController(BookingHistoryService bookingHistoryService, BookingService bookingService) {
        this.bookingHistoryService = bookingHistoryService;
        this.bookingService = bookingService;
    }

    // Добавить BookingHistory от Booking с booking_id, копируются поля от Booking.
    @PostMapping("booking/{booking_id}")
    public ResponseEntity<BookingHistory> saveBookingHistory(@PathVariable("booking_id") long booking_id) {

        Booking booking = bookingService.getBookingById(booking_id);
        BookingHistory bookingHistory = new BookingHistory(booking.getTimeStampDateTimeStart(),
                booking.getTimeStampDateTimeEnd(), booking.getWeekday(), booking.isOnline(), booking.isSuccess(),
                booking.getUser());
        return new ResponseEntity<>(bookingHistoryService.saveBookingHistory(bookingHistory), HttpStatus.CREATED);
    }

    // Получить список всех BookingHistory
    @GetMapping()
    public ResponseEntity<List<BookingHistory>> getAllBookingHistory() {
        return new ResponseEntity<>(bookingHistoryService.getAllBookingHistory(), HttpStatus.OK);
    }

    // Получить BookingHistory с данным id
    @GetMapping("{id}")
    public ResponseEntity<BookingHistory> getBookingHistoryById(@PathVariable("id") long id) {
        return new ResponseEntity<>(bookingHistoryService.getBookingHistoryById(id), HttpStatus.OK);
    }

    // Удалить BookingHistory с данным id из БД
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBookingHistory(@PathVariable("id") long id) {
        // delete BookingHistory from DB
        bookingHistoryService.deleteBookingHistory(id);
        return new ResponseEntity<>("BookingHistory deleted successfully!", HttpStatus.OK);
    }
}
