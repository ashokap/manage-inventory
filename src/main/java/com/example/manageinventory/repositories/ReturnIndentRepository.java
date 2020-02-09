package com.example.manageinventory.repositories;

import com.example.manageinventory.models.Indent;
import com.example.manageinventory.models.ReturnIndent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnIndentRepository extends JpaRepository<ReturnIndent, Integer> {
    ReturnIndent findReturnIndentById(int id);
}
