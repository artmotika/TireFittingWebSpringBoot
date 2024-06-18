package com.web.tirefitting.repositories;

import com.web.tirefitting.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(
        value = "SELECT * FROM booking WHERE is_expired=0 and is_booked=0 and weekday=?1 and is_online=1",
        nativeQuery = true)
    Collection<Booking> findAllBookingsForIntervalNotExpiredNotBookedOnline(int selectedWeekday);

    @Query(
        value = "SELECT * FROM booking WHERE is_expired=0 and is_booked=1 and weekday=?1",
        nativeQuery = true)
    Collection<Booking> findAllBookingsForIntervalNotExpiredBooked(int selectedWeekday);

    @Query(
            value = "SELECT * FROM booking WHERE is_expired=0 and is_booked=0",
            nativeQuery = true)
    Collection<Booking> findAllBookingsNotExpiredNotBooked();
}
