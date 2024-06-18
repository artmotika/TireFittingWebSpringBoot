package com.web.tirefitting.services.impl;

import com.web.tirefitting.exception.ResourceNotFoundException;
import com.web.tirefitting.models.BookingHistory;
import com.web.tirefitting.repositories.BookingHistoryRepository;
import com.web.tirefitting.services.BookingHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingHistoryServiceImpl implements BookingHistoryService {

    private final BookingHistoryRepository bookingHistoryRepository;

    public BookingHistoryServiceImpl(BookingHistoryRepository bookingHistoryRepository) {
        this.bookingHistoryRepository = bookingHistoryRepository;
    }

    @Override
    public BookingHistory saveBookingHistory(BookingHistory bookingHistory) {
        return bookingHistoryRepository.save(bookingHistory);
    }

    @Override
    public List<BookingHistory> getAllBookingHistory() {
        return bookingHistoryRepository.findAll();
    }

    @Override
    public BookingHistory getBookingHistoryById(long id) {
        return bookingHistoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BookingHistory", "Id", id));
    }

    @Override
    public BookingHistory updateBookingHistory(BookingHistory bookingHistory, long id) {
        // check whether BookingHistory with given id is exist in DB or not
        BookingHistory existingBookingHistory = bookingHistoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BookingHistory", "Id", id));
        existingBookingHistory.setTimeStampDateTimeStart(bookingHistory.getTimeStampDateTimeStart());
        existingBookingHistory.setTimeStampDateTimeEnd(bookingHistory.getTimeStampDateTimeEnd());
        existingBookingHistory.setWeekday(bookingHistory.getWeekday());
        existingBookingHistory.setOnline(bookingHistory.isOnline());
        existingBookingHistory.setSuccess(bookingHistory.isSuccess());
        // save exsiting bookingHistory
        bookingHistoryRepository.save(existingBookingHistory);
        return existingBookingHistory;
    }

    @Override
    public void deleteBookingHistory(long id) {
        // check whether BookingHistory with given id is exist in DB or not
        bookingHistoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BookingHistory", "Id", id));
        bookingHistoryRepository.deleteById(id);
    }
}
