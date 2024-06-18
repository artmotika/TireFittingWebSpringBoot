package com.web.tirefitting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="booking_history")
public class BookingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "time_stamp_date_time_start")
    private java.sql.Timestamp timeStampDateTimeStart;

    @Column(name = "time_stamp_date_time_end")
    private java.sql.Timestamp timeStampDateTimeEnd;

    @Column(name = "weekday")
    private int weekday;

    @Column(name = "is_online", nullable = false)
    private boolean isOnline;

    @Column(name = "is_success", nullable = false)
    private boolean isSuccess;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public BookingHistory(long id, java.sql.Timestamp timeStampDateTimeStart, java.sql.Timestamp timeStampDateTimeEnd,
                   int weekday, boolean isOnline, boolean isSuccess, User user) {
        this.id = id;
        this.timeStampDateTimeStart = timeStampDateTimeStart;
        this.timeStampDateTimeEnd = timeStampDateTimeEnd;
        this.weekday = weekday;
        this.isOnline = isOnline;
        this.isSuccess = isSuccess;
        this.user = user;
    }

    public BookingHistory(java.sql.Timestamp timeStampDateTimeStart, java.sql.Timestamp timeStampDateTimeEnd,
                          int weekday, boolean isOnline, boolean isSuccess, User user) {
        this.timeStampDateTimeStart = timeStampDateTimeStart;
        this.timeStampDateTimeEnd = timeStampDateTimeEnd;
        this.weekday = weekday;
        this.isOnline = isOnline;
        this.isSuccess = isSuccess;
        this.user = user;
    }

    protected BookingHistory() {}
}