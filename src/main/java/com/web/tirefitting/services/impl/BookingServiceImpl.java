package com.web.tirefitting.services.impl;

import com.web.tirefitting.exception.ResourceConflictException;
import com.web.tirefitting.exception.ResourceNotFoundException;
import com.web.tirefitting.models.Booking;
import com.web.tirefitting.models.User;
import com.web.tirefitting.repositories.BookingRepository;
import com.web.tirefitting.repositories.UserRepository;
import com.web.tirefitting.services.BookingService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBooking() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(long id) {
        return bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking", "Id", id));
    }

    @Override
    public Map<String, List<Integer>> getAvailableTimeIntervals(long id, int weekdayDiff, Map<Integer, Integer> timeIntervals) {
        LocalDate currentDate = LocalDate.now();
//        LocalDateTime dt = LocalDateTime.parse(currentDate + "T13:00:00").plusDays(weekdayDiff);
        LocalTime currentTime = LocalTime.now();
        int currentWeekday = currentDate.getDayOfWeek().getValue();
        int selectedWeekday = currentWeekday + weekdayDiff;
        boolean[] isAvailableIntervals = new boolean[timeIntervals.size()];
        Collection<Booking> bookings = bookingRepository.findAllBookingsForIntervalNotExpiredNotBookedOnline(selectedWeekday);
        // Прохожусь по всем booking в выбранный день недели
        bookings.forEach((booking) -> {
                // Считаю разницу между текущим временем и временем booking
                LocalTime bookingTimeStart = booking.getTimeStampDateTimeStart().toLocalDateTime().toLocalTime();
                Duration timeDiff = Duration.between(currentTime, bookingTimeStart);
                long diffMinutes = timeDiff.toMinutes();
                // Если weekdayDiff == 0, то это означает, что мы выбрали дату в этот же день, а не на следующий
                if (weekdayDiff == 0) {
                    Hashtable<Integer, Integer> tempTimeIntervals = new Hashtable<>(timeIntervals);
                    int timeIntevalIndex = 0;
                    // Прохожусь по всем интервалам и смотрю попал ли соответсвующий booking в этот интервал
                    for (Integer timeIntervalStart : tempTimeIntervals.keySet()) {
                        if (timeIntervalStart <= bookingTimeStart.getHour()
                                && bookingTimeStart.getHour() < tempTimeIntervals.get(timeIntervalStart)) {
                            /*
                            ** Смотрю на разницу в минутах относительно текущего времени, чтобы нельзя было
                            ** в момент бронирования получить те промежутки времени, которые будут недоступны через
                            ** небольшое количество времени */
                            if (diffMinutes >= 2) {
                                isAvailableIntervals[timeIntevalIndex] = true;
                                timeIntervals.remove(timeIntervalStart);
                            }
                        }
                        timeIntevalIndex ++;
                    }
                } else {
                    Hashtable<Integer, Integer> tempTimeIntervals = new Hashtable<>(timeIntervals);
                    int timeIntevalIndex = 0;
                    for (Integer timeIntervalStart : tempTimeIntervals.keySet()) {
                        if (timeIntervalStart <= bookingTimeStart.getHour()
                                && bookingTimeStart.getHour() < tempTimeIntervals.get(timeIntervalStart)) {
                            isAvailableIntervals[timeIntevalIndex] = true;
                            timeIntervals.remove(timeIntervalStart);
                        }
                        timeIntevalIndex ++;
                    }
                }
        });
        int idx = 0;
        List<Integer> available_intervals_idx = new ArrayList<>();
        List<Integer> not_available_intervals_idx = new ArrayList<>();
        for (boolean isAvailableInterval : isAvailableIntervals) {
            if (isAvailableInterval) {
                available_intervals_idx.add(idx);
            } else {
                not_available_intervals_idx.add(idx);
            }
            idx ++;
        }
        Map<String, List<Integer>> response = new HashMap<>();
        response.put("available_intervals_idx", available_intervals_idx);
        response.put("not_available_intervals_idx", not_available_intervals_idx);
        return response;
    }

    @Override
    public List<Booking> getAvailableBookingsForTimeInterval(long id, int weekdayDiff, int from, int to) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        int currentWeekday = currentDate.getDayOfWeek().getValue();
        int selectedWeekday = currentWeekday + weekdayDiff;
        Collection<Booking> bookings = bookingRepository.findAllBookingsForIntervalNotExpiredNotBookedOnline(selectedWeekday);
        List<Booking> response = new ArrayList<>();
        // Прохожусь по всем booking в выбранный день недели
        bookings.forEach((booking) -> {
            // Считаю разницу между текущим временем и временем booking
            LocalTime bookingTimeStart = booking.getTimeStampDateTimeStart().toLocalDateTime().toLocalTime();
            Duration timeDiff = Duration.between(currentTime, bookingTimeStart);
            long diffMinutes = timeDiff.toMinutes();
            // Если weekdayDiff == 0, то это означает, что мы выбрали дату в этот же день, а не на следующий
            if (weekdayDiff == 0) {
                // Смотрю попал ли соответсвующий booking в этот интервал
                if (from <= bookingTimeStart.getHour()
                        && bookingTimeStart.getHour() < to) {
                    /*
                     ** Смотрю на разницу в часах и минутах относительно текущего времени, чтобы нельзя было
                     ** в момент бронирования получить те промежутки времени, которые будут недоступны через
                     ** небольшое количество времени */
                    if (diffMinutes >= 2) {
                        response.add(booking);
                    }
                }
            } else {
                if (from <= bookingTimeStart.getHour()
                        && bookingTimeStart.getHour() < to) {
                    response.add(booking);
                }
            }
        });
        return response;
    }

    @Override
    public List<Booking> getAllBookingsBookedToday(long id, int hoursVisibleForward) {
        if (hoursVisibleForward > 12 || hoursVisibleForward < 0) {
            throw new IllegalArgumentException("hoursVisibleForward must be <=12 and >=0 !");
        }
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        int selectedWeekday = currentDate.getDayOfWeek().getValue();
        Collection<Booking> bookings = bookingRepository.findAllBookingsForIntervalNotExpiredBooked(selectedWeekday);
        List<Booking> response = new ArrayList<>();
        // Прохожусь по всем booking в выбранный день недели
        bookings.forEach((booking) -> {
            // Считаю разницу между текущим временем и временем booking
            LocalTime bookingTimeStart = booking.getTimeStampDateTimeStart().toLocalDateTime().toLocalTime();
            Duration timeDiff = Duration.between(currentTime, bookingTimeStart);
            long diffMinutes = timeDiff.toMinutes();
            if (diffMinutes <= hoursVisibleForward*60) {
                response.add(booking);
            }
        });
        return response;
    }

    @Override
    public List<Booking> getAllBookingsNotBookedAllDays(long id) {
        LocalTime currentTime = LocalTime.now();
        Collection<Booking> bookings = bookingRepository.findAllBookingsNotExpiredNotBooked();
        List<Booking> response = new ArrayList<>();
        // Прохожусь по всем booking
        bookings.forEach((booking) -> {
            // Считаю разницу между текущим временем и временем booking
            LocalTime bookingTimeStart = booking.getTimeStampDateTimeStart().toLocalDateTime().toLocalTime();
            Duration timeDiff = Duration.between(currentTime, bookingTimeStart);
            long diffMinutes = timeDiff.toMinutes();
            if (diffMinutes >= 2) {
                response.add(booking);
            }
        });
        return response;
    }

    @Override
    public Booking updateBooking(Booking booking, long id) {
        // check whether Booking with given id is exist in DB or not
        Booking existingBooking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking", "Id", id));
        existingBooking.setTimeStampDateTimeStart(booking.getTimeStampDateTimeStart());
        existingBooking.setTimeStampDateTimeEnd(booking.getTimeStampDateTimeEnd());
        existingBooking.setWeekday(booking.getWeekday());
        existingBooking.setBooked(booking.isBooked());
        existingBooking.setOnline(booking.isOnline());
        existingBooking.setScanned(booking.isScanned());
        existingBooking.setInProgress(booking.isInProgress());
        existingBooking.setReady(booking.isReady());
        existingBooking.setSuccess(booking.isSuccess());
        existingBooking.setExpired(booking.isExpired());
        // save exsiting booking
        bookingRepository.save(existingBooking);
        return existingBooking;
    }

    @Override
    public Booking updateBookingTimeStampAndStatus(long id) {
        // check whether Booking with given id is exist in DB or not
        Booking existingBooking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking", "Id", id));
        long oneWeekMilliseconds = 7 * 24 * 60 * 60 * 1000;
        java.sql.Timestamp timeStampDateTimeStart = new java.sql.Timestamp(
                existingBooking.getTimeStampDateTimeStart().getTime() + oneWeekMilliseconds);
        java.sql.Timestamp timeStampDateTimeEnd = new java.sql.Timestamp(
                existingBooking.getTimeStampDateTimeEnd().getTime() + oneWeekMilliseconds);
        existingBooking.setTimeStampDateTimeStart(timeStampDateTimeStart);
        existingBooking.setTimeStampDateTimeEnd(timeStampDateTimeEnd);
        existingBooking.setBooked(false);
        existingBooking.setScanned(false);
        existingBooking.setInProgress(false);
        existingBooking.setReady(false);
        existingBooking.setSuccess(false);
        existingBooking.setExpired(false);
        // save exsiting booking
        bookingRepository.save(existingBooking);
        return existingBooking;
    }

    @Override
    public Booking updateBookingSetUser(long bookingId, long userId) {
        // check whether user and booking with given id are exist in DB or not
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "Id", userId));
        Booking existingBooking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking", "Id", bookingId));

        if (existingUser.getNumberBooked() > 0) {
            throw new ResourceConflictException("User", "NumberBooked", existingUser.getNumberBooked());
        }

        if (!checkBanTimeBookingPassing(existingUser.getBanTimeBookingPassing(),
                existingUser.getNumberOfViolationsBookingPassing())) {
            throw new ResourceConflictException("User", "BanTimeBookingPassing", existingUser.getBanTimeBookingPassing());
        }

        if (!checkBanTimeBookingCancel(existingUser.getBanTimeBookingCancel(),
                existingUser.getNumberOfViolationsBookingCancel())) {
            throw new ResourceConflictException("User", "BanTimeBookingCancel", existingUser.getBanTimeBookingCancel());
        }

        if (!validateDateAndTime(existingBooking.getTimeStampDateTimeStart())) {
            throw new ResourceConflictException("Booking", "LocalDateTimeStart", existingBooking.getTimeStampDateTimeStart());
        }

        if (existingBooking.isBooked()) {
            throw new ResourceConflictException("Booking", "IsBooked", existingBooking.isBooked());
        }

        existingUser.setNumberBooked(existingUser.getNumberBooked() + 1);
        existingBooking.setBooked(true);
        existingBooking.setUser(existingUser);

        // save exsiting user and booking
        userRepository.save(existingUser);
        bookingRepository.save(existingBooking);
        return existingBooking;
    }

    @Override
    public Booking updateBookingDeleteUser(long bookingId, long userId) {
        // check whether user and booking with given id are exist in DB or not
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "Id", userId));
        Booking existingBooking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking", "Id", bookingId));

        User user = existingBooking.getUser();
        if (user.getId() != existingUser.getId()) {
            throw new ResourceNotFoundException("Booking", "User", -1);
        }
        existingBooking.setUser(null);

        existingUser.setNumberBooked(existingUser.getNumberBooked() - 1);
        existingBooking.setBooked(false);

        // save exsiting user and booking
        userRepository.save(existingUser);
        bookingRepository.save(existingBooking);
        return existingBooking;
    }

    @Override
    public Booking updateChangeBooking(long id, java.sql.Timestamp timeStampDateTimeStart,
                                       java.sql.Timestamp timeStampDateTimeEnd,
                                       Integer weekday, Boolean isBooked, Boolean isOnline, Boolean isScanned,
                                       Boolean isInProgress, Boolean isReady, Boolean isSuccess, Boolean isExpired) {
        // check whether Booking with given id is exist in DB or not
        Booking existingBooking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking", "Id", id));
        if (timeStampDateTimeStart != null) {
            existingBooking.setTimeStampDateTimeStart(timeStampDateTimeStart);
        }
        if (timeStampDateTimeEnd != null) {
            existingBooking.setTimeStampDateTimeEnd(timeStampDateTimeEnd);
        }
        if (weekday != null) {
            existingBooking.setWeekday(weekday);
        }
        if (isBooked != null) {
            existingBooking.setBooked(isBooked);
        }
        if (isOnline != null) {
            existingBooking.setOnline(isOnline);
        }
        if (isScanned != null) {
            existingBooking.setScanned(isScanned);
        }
        if (isInProgress != null) {
            existingBooking.setInProgress(isInProgress);
        }
        if (isReady != null) {
            existingBooking.setReady(isReady);
        }
        if (isSuccess != null) {
            existingBooking.setSuccess(isSuccess);
        }
        if (isExpired != null) {
            existingBooking.setExpired(isExpired);
        }
        // save exsiting booking
        bookingRepository.save(existingBooking);
        return existingBooking;
    }

    @Override
    public void deleteBooking(long id) {
        // check whether Booking with given id is exist in DB or not
        bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking", "Id", id));
        bookingRepository.deleteById(id);
    }

    private boolean checkBanTimeBookingPassing(java.sql.Timestamp banTime, int numberOfViolations) {
        long currentMilliseconds = System.currentTimeMillis();
        long banTimeMilliseconds = banTime.getTime();
        long diff = currentMilliseconds - banTimeMilliseconds;
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return switch (numberOfViolations) {
            case 0 -> true;
            case 1, 2 -> diffHours >= 12;
            default -> diffDays >= 7;
        };
    }

    private boolean checkBanTimeBookingCancel(java.sql.Timestamp banTime, int numberOfViolations) {
        long currentMilliseconds = System.currentTimeMillis();
        long banTimeMilliseconds = banTime.getTime();
        long diff = currentMilliseconds - banTimeMilliseconds;
        long diffHours = diff / (60 * 60 * 1000);
        return switch (numberOfViolations) {
            case 0, 1, 2 -> true;
            case 3 -> diffHours >= 1;
            case 4 -> diffHours >= 3;
            default -> diffHours >= 12;
        };
    }

    private boolean validateDateAndTime(java.sql.Timestamp bookingDateTime) {
        long currentMilliseconds = System.currentTimeMillis();
        long bookingTime = bookingDateTime.getTime();
        long diff = currentMilliseconds - bookingTime;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays == 0) {
            if (diffHours > 0) {
                return true;
            } else if (diffHours == 0) {
                return diffMinutes >= 0;
            } else return false;
        } else return diffDays > 0;
    }
}
