package com.example.manageinventory.repositories;

import com.example.manageinventory.models.Indent;
import com.example.manageinventory.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndentRepository extends JpaRepository<Indent, Integer> {
}
