package com.web.tirefitting.services;

import com.web.tirefitting.models.BookingHistory;

import java.util.List;

public interface BookingHistoryService {
    BookingHistory saveBookingHistory(BookingHistory bookingHistory);
    List<BookingHistory> getAllBookingHistory();
    BookingHistory getBookingHistoryById(long id);
    BookingHistory updateBookingHistory(BookingHistory bookingHistory, long id);
    void deleteBookingHistory(long id);
}
