package com.example.manageinventory.repositories;

import com.example.manageinventory.models.Indent;
import com.example.manageinventory.models.IndentLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndentLineRepository extends JpaRepository<IndentLine, Integer> {
    IndentLine findIndentLineById(int id);
}
