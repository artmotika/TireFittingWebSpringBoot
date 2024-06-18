package com.web.tirefitting.services.impl;

import com.web.tirefitting.exception.ResourceNotFoundException;
import com.web.tirefitting.models.User;
import com.web.tirefitting.repositories.UserRepository;
import com.web.tirefitting.services.UserService;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "Id", id));
    }

    @Override
    public User updateUser(User user, long id) {
        // check whether user with given id is exist in DB or not
        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "Id", id));
        existingUser.setEmail(user.getEmail());
//        existingUser.setPassword(encoder.encode(user.getPassword()));
        existingUser.setPassword(user.getPassword());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setNumberOfViolationsBookingPassing(user.getNumberOfViolationsBookingPassing());
        existingUser.setNumberOfViolationsBookingCancel(user.getNumberOfViolationsBookingCancel());
        existingUser.setBanTimeBookingPassing(user.getBanTimeBookingPassing());
        existingUser.setBanTimeBookingCancel(user.getBanTimeBookingCancel());
        existingUser.setNumberBooked(user.getNumberBooked());
        // save exsiting user
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public User updateUserAddViolationsBookingPassing(long id) {
        // check whether user with given id is exist in DB or not
        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "Id", id));

        existingUser.setNumberOfViolationsBookingPassing(existingUser.getNumberOfViolationsBookingPassing() + 1);
        existingUser.setBanTimeBookingPassing(new java.sql.Timestamp(System.currentTimeMillis()));
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public User updateUserAddViolationsBookingCancel(long id) {
        // check whether user with given id is exist in DB or not
        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "Id", id));

        existingUser.setNumberOfViolationsBookingCancel(existingUser.getNumberOfViolationsBookingCancel() + 1);
        existingUser.setBanTimeBookingCancel(new java.sql.Timestamp(System.currentTimeMillis()));
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public User updateUserRemoveViolationsBookingPassing(long id) {
        // check whether user with given id is exist in DB or not
        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "Id", id));

        existingUser.setNumberOfViolationsBookingPassing(0);
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public User updateUserRemoveViolationsBookingCancel(long id) {
        // check whether user with given id is exist in DB or not
        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "Id", id));

        existingUser.setNumberOfViolationsBookingCancel(0);
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public void deleteUser(long id) {
        // check whether User with given id is exist in DB or not
        userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "Id", id));
        userRepository.deleteById(id);
    }
}
