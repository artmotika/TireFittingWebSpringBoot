package com.web.tirefitting.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", length = 150, nullable = true, unique = true)
    private String email;

    @Column(name = "password", length = 150, nullable = false)
    private String password;

    @Column(name = "first_name", length = 150, nullable = false)
    private String firstName;

    @Column(name = "number_of_violations_booking_passing", nullable = false)
    private int numberOfViolationsBookingPassing;

    @Column(name = "number_of_violations_booking_cancel", nullable = false)
    private int numberOfViolationsBookingCancel;

    @Column(name = "ban_time_booking_passing", nullable = false)
    private java.sql.Timestamp banTimeBookingPassing;

    @Column(name = "ban_time_booking_cancel", nullable = false)
    private java.sql.Timestamp banTimeBookingCancel;

    @Column(name = "number_booked", nullable = false)
    private int numberBooked;

    public User(long id, String email, String password, String firstName, int numberOfViolationsBookingPassing,
                int numberOfViolationsBookingCancel, java.sql.Timestamp banTimeBookingPassing,
                java.sql.Timestamp banTimeBookingCancel, int numberBooked) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.password = password;
        this.numberOfViolationsBookingPassing = numberOfViolationsBookingPassing;
        this.numberOfViolationsBookingCancel = numberOfViolationsBookingCancel;
        this.banTimeBookingPassing = banTimeBookingPassing;
        this.banTimeBookingCancel = banTimeBookingCancel;
        this.numberBooked = numberBooked;
    }

    public User(String email, String password, String firstName, int numberOfViolationsBookingPassing,
                int numberOfViolationsBookingCancel, java.sql.Timestamp banTimeBookingPassing,
                java.sql.Timestamp banTimeBookingCancel, int numberBooked) {
        this.email = email;
        this.firstName = firstName;
        this.password = password;
        this.numberOfViolationsBookingPassing = numberOfViolationsBookingPassing;
        this.numberOfViolationsBookingCancel = numberOfViolationsBookingCancel;
        this.banTimeBookingPassing = banTimeBookingPassing;
        this.banTimeBookingCancel = banTimeBookingCancel;
        this.numberBooked = numberBooked;
    }

    protected User() {}
}
