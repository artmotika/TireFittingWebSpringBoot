package com.web.tirefitting.controllers;

import com.web.tirefitting.models.User;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.web.tirefitting.services.UserService;

import java.util.List;

@Data
class UserWrapper {
    private String email;
    private String password;
    private String firstName;
    private int numberOfViolationsBookingPassing;
    private int numberOfViolationsBookingCancel;
    private String banTimeBookingPassing;
    private String banTimeBookingCancel;
    private int numberBooked;
}

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // В теле POST запроса передается User для добавления в БД
    @PostMapping()
    public ResponseEntity<User> saveUser(@RequestBody UserWrapper userWrapper) {
        // Timestamp format is yyyy-mm-dd hh:mm:ss
        java.sql.Timestamp banTimePassing = java.sql.Timestamp.valueOf(userWrapper.getBanTimeBookingPassing());
        java.sql.Timestamp banTimeCancel = java.sql.Timestamp.valueOf(userWrapper.getBanTimeBookingCancel());
        User user = new User(userWrapper.getEmail(), userWrapper.getPassword(), userWrapper.getFirstName(),
                userWrapper.getNumberOfViolationsBookingPassing(), userWrapper.getNumberOfViolationsBookingCancel(),
                banTimePassing, banTimeCancel, userWrapper.getNumberBooked());
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    // Получение всех User из БД
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    // Получение User по id из БД
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    // В теле PUT запроса передается User для изменения User с id в БД
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody UserWrapper userWrapper) {
        // Timestamp format is yyyy-mm-dd hh:mm:ss
        java.sql.Timestamp banTimePassing = java.sql.Timestamp.valueOf(userWrapper.getBanTimeBookingPassing());
        java.sql.Timestamp banTimeCancel = java.sql.Timestamp.valueOf(userWrapper.getBanTimeBookingCancel());
        User user = new User(userWrapper.getEmail(), userWrapper.getPassword(), userWrapper.getFirstName(),
                userWrapper.getNumberOfViolationsBookingPassing(), userWrapper.getNumberOfViolationsBookingCancel(),
                banTimePassing, banTimeCancel, userWrapper.getNumberBooked());
        return new ResponseEntity<>(userService.updateUser(user, id), HttpStatus.OK);
    }

    // Удаление User с id из БД
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        // delete User from DB
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }
}
