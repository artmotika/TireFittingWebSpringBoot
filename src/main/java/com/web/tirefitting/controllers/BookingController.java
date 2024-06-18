package com.web.tirefitting.controllers;

import com.web.tirefitting.models.Booking;
import com.web.tirefitting.models.User;
import com.web.tirefitting.services.BookingService;
import com.web.tirefitting.services.UserService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Data
class BookingWrapper {
    private String timeStampDateTimeStart;
    private String timeStampDateTimeEnd;
    private int weekday;
    private boolean isBooked;
    private boolean isOnline;
    private boolean isScanned;
    private boolean isInProgress;
    private boolean isReady;
    private boolean isSuccess;
    private boolean isExpired;
}

@Data
class BookingWrapperNullable {
    private String timeStampDateTimeStart;
    private String timeStampDateTimeEnd;
    private Integer weekday;
    private Boolean isBooked;
    private Boolean isOnline;
    private Boolean isScanned;
    private Boolean isInProgress;
    private Boolean isReady;
    private Boolean isSuccess;
    private Boolean isExpired;
}

@Data
class TimeIntervalsWrapper {
    private int from1;
    private int to1;
    private int from2;
    private int to2;
    private int from3;
    private int to3;
}

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    // В теле PUT запроса передается Booking для добавления в БД
    @PostMapping()
    public ResponseEntity<Booking> saveBooking(@RequestBody BookingWrapper bookingWrapper) {
        // Timestamp format is yyyy-mm-dd hh:mm:ss
        java.sql.Timestamp timeStampDateTimeStart = java.sql.Timestamp.valueOf(bookingWrapper.getTimeStampDateTimeStart());
        java.sql.Timestamp timeStampDateTimeEnd = java.sql.Timestamp.valueOf(bookingWrapper.getTimeStampDateTimeEnd());
        Booking booking = new Booking(timeStampDateTimeStart, timeStampDateTimeEnd,
                bookingWrapper.getWeekday(), bookingWrapper.isBooked(), bookingWrapper.isOnline(),
                bookingWrapper.isScanned(), bookingWrapper.isInProgress(), bookingWrapper.isReady(),
                bookingWrapper.isSuccess(), bookingWrapper.isExpired());

        return new ResponseEntity<>(bookingService.saveBooking(booking), HttpStatus.CREATED);
    }

    // Получение всех Booking из БД
    @GetMapping()
    public ResponseEntity<List<Booking>> getAllBookings() {
        return new ResponseEntity<>(bookingService.getAllBooking(), HttpStatus.OK);
    }

    // Получение Booking по id из БД
    @GetMapping("{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("id") long id) {
        return new ResponseEntity<>(bookingService.getBookingById(id), HttpStatus.OK);
    }

    // Проверка на доступность в интевалах времени свободных Booking. Интервалы подаются в теле GET запроса.
    // Также подается разница в количестве дней от текущего числа, нужно для удобства поиска,
    // так как забронировать Booking можно только на неделю вперед.
    @GetMapping("{id}/time_intervals/{weekdayDiff}")
    public ResponseEntity<Map<String, List<Integer>>> getAvailableTimeIntervals(@PathVariable("id") long id,
                                    @PathVariable("weekdayDiff") int weekdayDiff,
                                    @RequestBody TimeIntervalsWrapper timeIntervalsWrapper) {
        Map<Integer, Integer> timeIntervals = new Hashtable<>();
        timeIntervals.put(timeIntervalsWrapper.getFrom1(), timeIntervalsWrapper.getTo1());
        timeIntervals.put(timeIntervalsWrapper.getFrom2(), timeIntervalsWrapper.getTo2());
        timeIntervals.put(timeIntervalsWrapper.getFrom3(), timeIntervalsWrapper.getTo3());
        return new ResponseEntity<>(bookingService.getAvailableTimeIntervals(id, weekdayDiff, timeIntervals), HttpStatus.OK);
    }

    // Получение свободных Booking в заданном интервале времени, с конкретной разницей относительно текущего дня.
    @GetMapping("{id}/time_intervals/bookings/{weekdayDiff}/{from}/{to}")
    public ResponseEntity<List<Booking>> getAvailableBookingsForTimeInterval(@PathVariable("id") long id,
                                                                @PathVariable("weekdayDiff") int weekdayDiff,
                                                                @PathVariable("from") int from,
                                                                @PathVariable("to") int to) {
        return new ResponseEntity<>(bookingService.getAvailableBookingsForTimeInterval(id, weekdayDiff, from, to), HttpStatus.OK);
    }

    // Получение занятых Booking вперед на hoursVisibleForward кол-во часов. Нужно для отображения очереди на ТВ.
    @GetMapping("{id}/bookings/booked/{hoursVisibleForward}")
    public ResponseEntity<List<Booking>> getAllBookingsBookedToday(@PathVariable("id") long id,
                                                 @PathVariable("hoursVisibleForward") int hoursVisibleForward) {
        return new ResponseEntity<>(bookingService.getAllBookingsBookedToday(id, hoursVisibleForward), HttpStatus.OK);
    }

    // Получение всех свободных Booking, нужно для админа для более быстрого поиска.
    @GetMapping("{id}/bookings/not_booked")
    public ResponseEntity<List<Booking>> getAllBookingsNotBookedAllDays(@PathVariable("id") long id) {
        return new ResponseEntity<>(bookingService.getAllBookingsNotBookedAllDays(id), HttpStatus.OK);
    }

    // В теле PUT запроса передается Booking для изменения Booking с id в БД
    @PutMapping("{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable("id") long id,
                                                 @RequestBody BookingWrapper bookingWrapper) {
        // Timestamp format is yyyy-mm-dd hh:mm:ss
        java.sql.Timestamp timeStampDateTimeStart = java.sql.Timestamp.valueOf(bookingWrapper.getTimeStampDateTimeStart());
        java.sql.Timestamp timeStampDateTimeEnd = java.sql.Timestamp.valueOf(bookingWrapper.getTimeStampDateTimeEnd());
        Booking booking = new Booking(timeStampDateTimeStart, timeStampDateTimeEnd,
                bookingWrapper.getWeekday(), bookingWrapper.isBooked(), bookingWrapper.isOnline(),
                bookingWrapper.isScanned(), bookingWrapper.isInProgress(), bookingWrapper.isReady(),
                bookingWrapper.isSuccess(), bookingWrapper.isExpired());

        return new ResponseEntity<>(bookingService.updateBooking(booking, id), HttpStatus.OK);
    }

    // Сбросить Booking с данным id, а именно переставить дату на неделю вперед, и снять все статусы заказа
    @PutMapping("{id}/drop")
    public ResponseEntity<Booking> updateBookingTimeStampAndStatus(@PathVariable("id") long id) {

        return new ResponseEntity<>(bookingService.updateBookingTimeStampAndStatus(id), HttpStatus.OK);
    }

    // Привязать к Booking с данным id User с данным id
    @PutMapping("{booking_id}/user/{user_id}/add")
    public ResponseEntity<Booking> updateBookingSetUser(@PathVariable("booking_id") long bookingId,
                                                     @PathVariable("user_id") long userId) {

        return new ResponseEntity<>(bookingService.updateBookingSetUser(bookingId, userId), HttpStatus.OK);
    }

    // Отвязать от Booking с данным id User с данным id
    @PutMapping("{booking_id}/user/{user_id}/delete")
    public ResponseEntity<Booking> updateBookingDeleteUser(@PathVariable("booking_id") long bookingId,
                                                        @PathVariable("user_id") long userId) {

        return new ResponseEntity<>(bookingService.updateBookingDeleteUser(bookingId, userId), HttpStatus.OK);
    }

    // Отвязать от Booking с данным id User с данным id + добавить одно ограничение пользователю на cancel Booking
    @PutMapping("{booking_id}/user/{user_id}/delete/cancel")
    public ResponseEntity<Booking> updateBookingDeleteUserCancel(@PathVariable("booking_id") long bookingId,
                                                                 @PathVariable("user_id") long userId) {

        userService.updateUserAddViolationsBookingCancel(userId);
        Booking booking = bookingService.updateBookingDeleteUser(bookingId, userId);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    // В теле PUT запроса передается Booking для изменения Booking с id в БД, отличие от простого update в том,
    // что можно указать null возле поля и тогда оно скопируется от Booking с данным id
    @PutMapping("{id}/change")
    public ResponseEntity<Booking> updateChangeBooking(@PathVariable("id") long id,
                                                       @RequestBody BookingWrapperNullable bookingWrapper) {
        // Timestamp format is yyyy-mm-dd hh:mm:ss
        java.sql.Timestamp timeStampDateTimeStart = java.sql.Timestamp.valueOf(bookingWrapper.getTimeStampDateTimeStart());
        java.sql.Timestamp timeStampDateTimeEnd = java.sql.Timestamp.valueOf(bookingWrapper.getTimeStampDateTimeEnd());
        Booking booking = bookingService.updateChangeBooking(id, timeStampDateTimeStart, timeStampDateTimeEnd,
                bookingWrapper.getWeekday(), bookingWrapper.getIsBooked(), bookingWrapper.getIsOnline(),
                bookingWrapper.getIsScanned(), bookingWrapper.getIsInProgress(), bookingWrapper.getIsReady(),
                bookingWrapper.getIsSuccess(), bookingWrapper.getIsExpired());

        return new ResponseEntity<>(booking, HttpStatus.OK);
    }


    // В теле PUT запроса передается Booking для изменения Booking с id в БД, отличие от простого update в том,
    // что можно указать null возле поля и тогда оно скопируется от Booking с данным id.
    // Так же данное api нужно использовать при обновлении статуса Booking, так как после успешного заказа
    // у пользователя должны сброситься ограничения или наоборот добавиться, если заказ не успешен.
    @PutMapping("{id}/change/status")
    public ResponseEntity<Booking> updateChangeBookingStatus(@PathVariable("id") long id,
                                                       @RequestBody BookingWrapperNullable bookingWrapper) {
        // Timestamp format is yyyy-mm-dd hh:mm:ss
        Booking booking = bookingService.updateChangeBooking(id, null, null,
                null, bookingWrapper.getIsBooked(), null, bookingWrapper.getIsScanned(),
                bookingWrapper.getIsInProgress(), bookingWrapper.getIsReady(), bookingWrapper.getIsSuccess(),
                bookingWrapper.getIsExpired());

        if (bookingWrapper.getIsReady() != null && bookingWrapper.getIsSuccess() != null) {
            if (bookingWrapper.getIsReady()) {
                User user = booking.getUser();
                if (user != null) {
                    if (bookingWrapper.getIsSuccess()) {
                        userService.updateUserRemoveViolationsBookingPassing(user.getId());
                        userService.updateUserRemoveViolationsBookingCancel(user.getId());
                    } else {
                        userService.updateUserAddViolationsBookingPassing(user.getId());
                    }
                } else {
                    throw new IllegalArgumentException("user is null in Booking object");
                }
            }
        }

        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    // Удалить Booking с данным id из БД
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable("id") long id) {
        // delete Booking from DB
        bookingService.deleteBooking(id);
        return new ResponseEntity<>("Booking deleted successfully!", HttpStatus.OK);
    }
}
