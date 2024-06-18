package com.web.tirefitting.services;

import com.web.tirefitting.models.Booking;

import java.util.List;
import java.util.Map;

public interface BookingService {
    Booking saveBooking(Booking booking);
    List<Booking> getAllBooking();
    Booking getBookingById(long id);
    Map<String, List<Integer>> getAvailableTimeIntervals(long id, int weekdayDiff, Map<Integer, Integer> timeIntervals);
    List<Booking> getAvailableBookingsForTimeInterval(long id, int weekdayDiff, int from, int to);
    List<Booking> getAllBookingsBookedToday(long id, int hoursVisibleForward);
    List<Booking> getAllBookingsNotBookedAllDays(long id);
    Booking updateBooking(Booking booking, long id);
    Booking updateBookingTimeStampAndStatus(long id);
    Booking updateBookingSetUser(long bookingId, long userId);
    Booking updateBookingDeleteUser(long bookingId, long userId);
    Booking updateChangeBooking(long id, java.sql.Timestamp timeStampDateTimeStart,
                                java.sql.Timestamp timeStampDateTimeEnd,
                                Integer weekday, Boolean isBooked, Boolean isOnline, Boolean isScanned,
                                Boolean isInProgress, Boolean isReady, Boolean isSuccess, Boolean isExpired);
    void deleteBooking(long id);
}
