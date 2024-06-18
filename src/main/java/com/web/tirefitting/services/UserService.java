package com.web.tirefitting.services;

import com.web.tirefitting.models.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    List<User> getAllUser();
    User getUserById(long id);
    User updateUser(User user, long id);
    User updateUserAddViolationsBookingPassing(long id);
    User updateUserAddViolationsBookingCancel(long id);
    User updateUserRemoveViolationsBookingPassing(long id);
    User updateUserRemoveViolationsBookingCancel(long id);
    void deleteUser(long id);
}
