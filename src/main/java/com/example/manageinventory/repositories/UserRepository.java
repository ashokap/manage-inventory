package com.example.manageinventory.repositories;

import com.example.manageinventory.models.Indent;
import com.example.manageinventory.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(int id);
}
