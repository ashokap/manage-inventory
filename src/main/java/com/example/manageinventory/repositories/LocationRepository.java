package com.example.manageinventory.repositories;


import com.example.manageinventory.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Location findLocationById(int id);

}
