package com.web.tirefitting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "time_stamp_date_time_start")
    private java.sql.Timestamp timeStampDateTimeStart;

    @Column(name = "time_stamp_date_time_end")
    private java.sql.Timestamp timeStampDateTimeEnd;

    @Column(name = "weekday")
    private int weekday;

    @Column(name = "is_booked", nullable = false)
    private boolean isBooked;

    @Column(name = "is_online", nullable = false)
    private boolean isOnline;

    @Column(name = "is_scanned", nullable = false)
    private boolean isScanned;

    @Column(name = "is_in_progress", nullable = false)
    private boolean isInProgress;

    @Column(name = "is_ready", nullable = false)
    private boolean isReady;

    @Column(name = "is_success", nullable = false)
    private boolean isSuccess;

    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Booking(long id, java.sql.Timestamp timeStampDateTimeStart, java.sql.Timestamp timeStampDateTimeEnd,
                   int weekday, boolean isBooked, boolean isOnline, boolean isScanned, boolean isInProgress,
                   boolean isReady, boolean isSuccess, boolean isExpired) {
        this.id = id;
        this.timeStampDateTimeStart = timeStampDateTimeStart;
        this.timeStampDateTimeEnd = timeStampDateTimeEnd;
        this.weekday = weekday;
        this.isBooked = isBooked;
        this.isOnline = isOnline;
        this.isScanned = isScanned;
        this.isInProgress = isInProgress;
        this.isReady = isReady;
        this.isSuccess = isSuccess;
        this.isExpired = isExpired;
    }

    public Booking(java.sql.Timestamp timeStampDateTimeStart, java.sql.Timestamp timeStampDateTimeEnd,
                   int weekday, boolean isBooked, boolean isOnline, boolean isScanned, boolean isInProgress,
                   boolean isReady, boolean isSuccess, boolean isExpired) {
        this.timeStampDateTimeStart = timeStampDateTimeStart;
        this.timeStampDateTimeEnd = timeStampDateTimeEnd;
        this.weekday = weekday;
        this.isBooked = isBooked;
        this.isOnline = isOnline;
        this.isScanned = isScanned;
        this.isInProgress = isInProgress;
        this.isReady = isReady;
        this.isSuccess = isSuccess;
        this.isExpired = isExpired;
    }

    protected Booking() {}
}
