package com.web.tirefitting.repositories;

import com.web.tirefitting.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
